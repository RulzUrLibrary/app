package com.rulzurlibrary.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rulzurlibrary.R;
import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.controllers.AddCollection;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        vh.addCollection.setBook(book);

        Picasso p = Picasso.with(context);
        //p.setLoggingEnabled(true);
        p.load(book.getThumbName()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(vh.imageView);

        return vh.rootView;
    }

    private static class ViewHolder {
        final RelativeLayout rootView;
        final ImageView imageView;
        final TextView textViewName;
        final AddCollection addCollection;

        private ViewHolder(RelativeLayout rootView, ImageView imageView, TextView textViewName, AddCollection addCollection) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textViewName = textViewName;
            this.addCollection = addCollection;
        }

        static BookAdapter.ViewHolder create(RelativeLayout rootView) {
            ImageView imageView = rootView.findViewById(R.id.imageView);
            TextView textViewName = rootView.findViewById(R.id.textViewName);
            AddCollection addCollection = rootView.findViewById(R.id.buttonCollection);
            return new BookAdapter.ViewHolder(rootView, imageView, textViewName, addCollection);
        }
    }
}
