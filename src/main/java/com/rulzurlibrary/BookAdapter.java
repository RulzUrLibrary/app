package com.rulzurlibrary;

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

import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

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
        final com.rulzurlibrary.BookAdapter.ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.book_layout, parent, false);
            vh = com.rulzurlibrary.BookAdapter.ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (com.rulzurlibrary.BookAdapter.ViewHolder) convertView.getTag();
        }

        Book item = getItem(position);

        assert item != null;
        vh.textViewName.setText(item.title());

        if (item.owned) {
            vh.textViewEmail.setText("✔");
            vh.textViewEmail.setBackgroundColor(context.getColor(R.color.success));
        } else {
            vh.textViewEmail.setText("+");
            vh.textViewEmail.setBackgroundColor(context.getColor(R.color.primary));
        }
        Picasso p = Picasso.with(context);
        //p.setLoggingEnabled(true);
        p.load(item.getThumbName()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(vh.imageView);

        return vh.rootView;
    }

    private static class ViewHolder {
        final RelativeLayout rootView;
        final ImageView imageView;
        final TextView textViewName;
        final TextView textViewEmail;

        private ViewHolder(RelativeLayout rootView, ImageView imageView, TextView textViewName, TextView textViewEmail) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textViewName = textViewName;
            this.textViewEmail = textViewEmail;
        }

        static com.rulzurlibrary.BookAdapter.ViewHolder create(RelativeLayout rootView) {
            ImageView imageView = rootView.findViewById(R.id.imageView);
            TextView textViewName = rootView.findViewById(R.id.textViewName);
            TextView textViewEmail = rootView.findViewById(R.id.textViewEmail);
            return new com.rulzurlibrary.BookAdapter.ViewHolder(rootView, imageView, textViewName, textViewEmail);
        }
    }
}
