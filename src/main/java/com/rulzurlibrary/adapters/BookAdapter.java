package com.rulzurlibrary.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rulzurlibrary.R;
import com.rulzurlibrary.RulzUrLibraryService;
import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.Isbns;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookAdapter extends ArrayAdapter<Book> {

    private final String TAG = "BookAdapter";
    private List<Book> bookList;
    private Context context;
    private LayoutInflater mInflater;

    // Constructors
    public BookAdapter(Context context, List<Book> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        bookList = objects;
    }

    @Override
    public Book getItem(int position) {
        return bookList.get(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final BookAdapter.ViewHolder vh;
        final Book book = getItem(position);

        if (convertView == null) {
            View view = mInflater.inflate(R.layout.book_layout, parent, false);
            vh = BookAdapter.ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (BookAdapter.ViewHolder) convertView.getTag();
        }

        assert book != null;
        vh.textViewName.setText(book.title());

        if (book.owned) {
            vh.buttonCollection.setEnabled(false);
            vh.buttonCollection.setText(R.string.in_collection);

        } else {
            vh.buttonCollection.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Do something in response to button click
                    RulzUrLibraryService.client.putBook(new Isbns(book.isbn)).enqueue(new Callback<Isbns>() {
                        @Override
                        public void onResponse(@NonNull Call<Isbns> call, @NonNull Response<Isbns> response) {
                            if (response.isSuccessful()) {
                                Isbns isbns = response.body();
                                assert isbns != null;
                                Log.d(TAG, String.format("added: %d", isbns.added));
                                vh.buttonCollection.setEnabled(false);
                                vh.buttonCollection.setText(R.string.in_collection);
                            } else {
                                Log.e(TAG, response.toString());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Isbns> call, @NonNull Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });
                }
            });
        }
        Picasso p = Picasso.with(context);
        //p.setLoggingEnabled(true);
        p.load(book.getThumbName()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(vh.imageView);

        return vh.rootView;
    }

    private static class ViewHolder {
        final RelativeLayout rootView;
        final ImageView imageView;
        final TextView textViewName;
        final Button buttonCollection;

        private ViewHolder(RelativeLayout rootView, ImageView imageView, TextView textViewName, Button buttonCollection) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textViewName = textViewName;
            this.buttonCollection = buttonCollection;
        }

        static BookAdapter.ViewHolder create(RelativeLayout rootView) {
            ImageView imageView = rootView.findViewById(R.id.imageView);
            TextView textViewName = rootView.findViewById(R.id.textViewName);
            Button buttonCollection = rootView.findViewById(R.id.buttonCollection);
            return new BookAdapter.ViewHolder(rootView, imageView, textViewName, buttonCollection);
        }
    }
}
