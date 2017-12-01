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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rulzurlibrary.BookActivity;
import com.rulzurlibrary.R;
import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.Serie;
import com.rulzurlibrary.controllers.AddCollection;
import com.rulzurlibrary.controllers.AddWishlist;
import com.squareup.picasso.Picasso;

import java.util.List;


public class BookAdapter extends ArrayAdapter<Book> {

    private final String TAG = "BookAdapter";
    private Serie serie;
    private Context context;
    private LayoutInflater mInflater;

    // Constructors
    public BookAdapter(Context context, Serie serie) {
        super(context, 0, serie.volumes);
        this.serie = serie;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public BookAdapter(Context context, List<Book> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public Book getItem(int position) {
        Book book = serie.volumes.get(position);
        if (this.serie != null ) { book.serie = serie.name; }
        return book;
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
        Log.d(TAG, book.title());

        vh.textViewName.setText(book.title());
        vh.addCollection.setBook(book);
        vh.addWishlist.setBook(book);

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
        final AddWishlist addWishlist;

        private ViewHolder(RelativeLayout rootView) {
            this.rootView = rootView;
            this.imageView = rootView.findViewById(R.id.imageView);
            this.textViewName = rootView.findViewById(R.id.textViewName);
            this.addCollection = rootView.findViewById(R.id.buttonCollection);
            this.addWishlist = rootView.findViewById(R.id.buttonWishlist);
        }

        static BookAdapter.ViewHolder create(RelativeLayout rootView) { return new BookAdapter.ViewHolder(rootView); }
    }
}
