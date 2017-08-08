package com.rulzurlibrary;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.io.File;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.FotoapparatSwitcher;
import io.fotoapparat.error.CameraErrorCallback;
import io.fotoapparat.hardware.CameraException;
import io.fotoapparat.parameter.LensPosition;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.photo.BitmapPhoto;
import io.fotoapparat.preview.Frame;
import io.fotoapparat.preview.FrameProcessor;
import io.fotoapparat.result.PendingResult;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.view.CameraView;

import static io.fotoapparat.log.Loggers.fileLogger;
import static io.fotoapparat.log.Loggers.logcat;
import static io.fotoapparat.log.Loggers.loggers;
import static io.fotoapparat.parameter.selector.AspectRatioSelectors.standardRatio;
import static io.fotoapparat.parameter.selector.FlashSelectors.autoFlash;
import static io.fotoapparat.parameter.selector.FlashSelectors.autoRedEye;
import static io.fotoapparat.parameter.selector.FlashSelectors.off;
import static io.fotoapparat.parameter.selector.FlashSelectors.torch;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.autoFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.continuousFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.fixed;
import static io.fotoapparat.parameter.selector.LensPositionSelectors.lensPosition;
import static io.fotoapparat.parameter.selector.Selectors.firstAvailable;
import static io.fotoapparat.parameter.selector.SizeSelectors.biggestSize;
import static io.fotoapparat.result.transformer.SizeTransformers.scaled;

public class MainActivity extends AppCompatActivity {

	private final PermissionsDelegate permissionsDelegate = new PermissionsDelegate(this);
	private boolean hasCameraPermission;
	private CameraView cameraView;

	private FotoapparatSwitcher fotoapparatSwitcher;
	private Fotoapparat frontFotoapparat;
	private Fotoapparat backFotoapparat;

	private ImageScanner scanner;
    private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		cameraView = (CameraView) findViewById(R.id.camera_view);
		hasCameraPermission = permissionsDelegate.hasCameraPermission();

		if (hasCameraPermission) {
			cameraView.setVisibility(View.VISIBLE);
		} else {
			permissionsDelegate.requestCameraPermission();
		}

		setupFotoapparat();

		takePictureOnClick(cameraView);
		focusOnLongClick(cameraView);

		setupSwitchCameraButton();
        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                showAlertDialog((String) message.obj);
            }
        };
	}

	private void setupFotoapparat() {
		frontFotoapparat = createFotoapparat(LensPosition.FRONT);
		backFotoapparat = createFotoapparat(LensPosition.BACK);
		fotoapparatSwitcher = FotoapparatSwitcher.withDefault(backFotoapparat);
	}

	private void setupSwitchCameraButton() {
		View switchCameraButton = findViewById(R.id.switchCamera);
		switchCameraButton.setVisibility(
				canSwitchCameras()
						? View.VISIBLE
						: View.GONE
		);
		switchCameraOnClick(switchCameraButton);
	}

	private void switchCameraOnClick(View view) {
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switchCamera();
			}
		});
	}

	private void focusOnLongClick(View view) {
		view.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				fotoapparatSwitcher.getCurrentFotoapparat().autoFocus();

				return true;
			}
		});
	}

	private void takePictureOnClick(View view) {
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				takePicture();
			}
		});
	}

	private boolean canSwitchCameras() {
		return frontFotoapparat.isAvailable() == backFotoapparat.isAvailable();
	}

	private Fotoapparat createFotoapparat(LensPosition position) {
		return Fotoapparat
				.with(this)
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
						autoFlash(),
						torch(),
						off()
				))
				.frameProcessor(new SampleFrameProcessor())
				.logger(loggers(
						logcat(),
						fileLogger(this)
				))
				.cameraErrorCallback(new CameraErrorCallback() {
					@Override
					public void onError(CameraException e) {
						Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
					}
				})
				.build();
	}

	private void takePicture() {
		PhotoResult photoResult = fotoapparatSwitcher.getCurrentFotoapparat().takePicture();

		photoResult.saveToFile(new File(
				getExternalFilesDir("photos"),
				"photo.jpg"
		));

		photoResult
				.toBitmap(scaled(0.25f))
				.whenAvailable(new PendingResult.Callback<BitmapPhoto>() {
					@Override
					public void onResult(BitmapPhoto result) {
						ImageView imageView = (ImageView) findViewById(R.id.result);

						imageView.setImageBitmap(result.bitmap);
						imageView.setRotation(-result.rotationDegrees);
					}
				});
	}

	private void switchCamera() {
		if (fotoapparatSwitcher.getCurrentFotoapparat() == frontFotoapparat) {
			fotoapparatSwitcher.switchTo(backFotoapparat);
		} else {
			fotoapparatSwitcher.switchTo(frontFotoapparat);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (hasCameraPermission) {
			fotoapparatSwitcher.start();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (hasCameraPermission) {
			fotoapparatSwitcher.stop();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (permissionsDelegate.resultGranted(requestCode, permissions, grantResults)) {
			fotoapparatSwitcher.start();
			cameraView.setVisibility(View.VISIBLE);
		}
	}

	private class SampleFrameProcessor implements FrameProcessor {

		@Override
		public void processFrame(Frame frame) {
			// Perform frame processing, if needed
            Image barcode = new Image(frame.size.width, frame.size.height, "Y800");
            barcode.setData(frame.image);
            int result = scanner.scanImage(barcode);

            if (result != 0) {

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {

                    Log.i("<<<<<<Asset Code>>>>> ",
                            "<<<<Bar Code>>> " + sym.getData());
                    Message msg = Message.obtain(); // Creates an new Message instance
                    msg.obj = sym.getData().trim(); // Put the string into Message, into "obj" field.
                    msg.setTarget(mHandler); // Set the Handler
                    msg.sendToTarget(); //Send the message
                }
            }
		}

	}

    private void showAlertDialog(String message) {

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.app_name))
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}