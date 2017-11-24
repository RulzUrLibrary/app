package com.rulzurlibrary.fragments;

/**
 * Created by max on 8/28/17.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rulzurlibrary.BookActivity;
import com.rulzurlibrary.PermissionsDelegate;
import com.rulzurlibrary.R;
import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.RulzUrLibraryService;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.util.HashMap;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.error.CameraErrorCallback;
import io.fotoapparat.hardware.CameraException;
import io.fotoapparat.parameter.LensPosition;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.preview.Frame;
import io.fotoapparat.preview.FrameProcessor;
import io.fotoapparat.view.CameraView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.fotoapparat.log.Loggers.fileLogger;
import static io.fotoapparat.log.Loggers.logcat;
import static io.fotoapparat.log.Loggers.loggers;
import static io.fotoapparat.parameter.selector.AspectRatioSelectors.standardRatio;
import static io.fotoapparat.parameter.selector.FlashSelectors.autoRedEye;
import static io.fotoapparat.parameter.selector.FlashSelectors.off;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.autoFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.continuousFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.fixed;
import static io.fotoapparat.parameter.selector.LensPositionSelectors.lensPosition;
import static io.fotoapparat.parameter.selector.Selectors.firstAvailable;
import static io.fotoapparat.parameter.selector.SizeSelectors.biggestSize;

public class ScanFragment extends Fragment {
    private CameraView cameraView;
    private TextView resultView;
    private Handler isbnHandler;
    private Handler bookHandler;
    private boolean hasCameraPermission;

    private ImageScanner scanner;
    private HashMap<String, Book> gathered;
    private Fotoapparat fotoapparat;
    private static final String TAG = "ScanFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scan_fragment, container, false);

        cameraView = view.findViewById(R.id.camera_view);
        resultView = view.findViewById(R.id.result);
        PermissionsDelegate permissionsDelegate = new PermissionsDelegate(this.getActivity());
        hasCameraPermission = permissionsDelegate.hasCameraPermission();

        if (hasCameraPermission) {
            cameraView.setVisibility(View.VISIBLE);
        } else {
            permissionsDelegate.requestCameraPermission();
        }

        fotoapparat = createFotoapparat(LensPosition.BACK, view.getContext());
        focusOnClick(cameraView);
        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        gathered = new HashMap<>();
        isbnHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                resultView.setText(message.obj.toString());
            }
        };
        bookHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                Book book = (Book) message.obj;
                Intent intent = new Intent(getContext(), BookActivity.class);
                intent.putExtra("book", book);
                startActivity(intent);
                showAlertDialog(String.format("Isbn: %s,\nTitle: %s", book.isbn, book.title()));
            }
        };

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (hasCameraPermission) {
            fotoapparat.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (hasCameraPermission) {
            fotoapparat.stop();
        }
    }
    private void focusOnClick(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fotoapparat.autoFocus();
            }
        });
    }

    private void showAlertDialog(String message) {

        new AlertDialog.Builder(this.getContext())
                .setTitle(getResources().getString(R.string.app_name))
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private Fotoapparat createFotoapparat(LensPosition position, final Context context) {
        return Fotoapparat
                .with(context)
                .into(cameraView)
                .previewScaleType(ScaleType.CENTER_CROP)
                .photoSize(standardRatio(biggestSize()))
                .lensPosition(lensPosition(position))
                .focusMode(firstAvailable(
                        continuousFocus(),
                        autoFocus(),
                        fixed()
                ))
                .flash(firstAvailable(
                        autoRedEye(),
                        //autoFlash(),
                        //torch(),
                        off()
                ))
                .frameProcessor(new SampleFrameProcessor())
                .logger(loggers(
                        logcat(),
                        fileLogger(context)
                ))
                .cameraErrorCallback(new CameraErrorCallback() {
                    @Override
                    public void onError(CameraException e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    }
                })
                .build();
    }

    private class SampleFrameProcessor implements FrameProcessor {




        @Override
        public void processFrame(Frame frame) {
            // Perform frame processing, if needed
            String isbn;
            Image barcode = new Image(frame.size.width, frame.size.height, "Y800");
            barcode.setData(frame.image);

            if (scanner.scanImage(barcode) != 0) {

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    isbn = sym.getData().trim();
                    Log.i("<<<<<<Asset Code>>>>> ", "<<<<Bar Code>>> " + isbn);

                    if (gathered.containsKey(isbn)) {
                        continue;
                    }
                    final Call<Book> call = RulzUrLibraryService.client.postIsbn(new Book(isbn));

                    Message m = isbnHandler.obtainMessage();
                    m.obj = String.format("Scanned: %s", isbn);
                    m.sendToTarget();

                    call.enqueue(new Callback<Book>() {
                        @Override
                        public void onResponse(@NonNull Call<Book> call, @NonNull Response<Book> response) {
                            if (response.isSuccessful()) {
                                Message m = bookHandler.obtainMessage();
                                m.obj = response.body();
                                m.sendToTarget();
                            } else {
                                Log.e(TAG, response.toString());
                                Message m = isbnHandler.obtainMessage();
                                m.obj = String.format("API call went wrong: %s", response.toString());
                                m.sendToTarget();
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<Book> call, @NonNull Throwable t) {
                            Message m = isbnHandler.obtainMessage();
                            m.obj = String.format("Something went wrong: %s", t.getMessage());
                            m.sendToTarget();
                        }
                    });
                    gathered.put(isbn, null);
                }
            }
        }

    }
}
