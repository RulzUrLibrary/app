package com.rulzurlibrary.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.rulzurlibrary.BookActivity;
import com.rulzurlibrary.R;
import com.rulzurlibrary.SearchActivity;
import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.Wishlist;

import java.util.List;

public class WishlistAdapter extends BaseExpandableListAdapter {
    private final String TAG = "WishlistAdapter";
    private List<Wishlist> wishlists;
    private LayoutInflater mInflater;
    private Context mContext;

    public WishlistAdapter(Context context, List<Wishlist> wishlists) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.wishlists = wishlists;
    }

    @Override
    public int getGroupCount() {
        return wishlists.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return wishlists.get(i).books.size();
    }

    @Override
    public Object getGroup(int i) {
        return wishlists.get(i);
    }

    @Override
    public Object getChild(int i, int j) {
        return wishlists.get(i).books.get(j);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int j) {
        return j;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mInflater.inflate(R.layout.wishlist_layout, viewGroup, false);
        }
        TextView wishlistName = view.findViewById(R.id.wishlistName);
        wishlistName.setTypeface(null, Typeface.BOLD);

        Wishlist wishlist = wishlists.get(i);
        wishlistName.setText(String.format("%s (%d books)", wishlist.name.toUpperCase(), wishlist.books.size()));
        return view;
    }

    @Override
    public View getChildView(int i, int j, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mInflater.inflate(R.layout.book_partial_layout, viewGroup, false);
        }
        Wishlist wishlist = wishlists.get(i);
        Book book = wishlist.books.get(j);
        Log.d(TAG, String.format("%s: %s", wishlist.name, book.title()));
        TextView bookName = view.findViewById(R.id.bookName);
        view.setOnClickListener(new BookClickListener(book));
        bookName.setText(book.title());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private class BookClickListener implements View.OnClickListener {
        private Book book;

        BookClickListener(Book book) {
            this.book = book;
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, String.format("launching book activity with isbn: %s", book.isbn));
            Intent intent = new Intent(WishlistAdapter.this.mContext, BookActivity.class);
            intent.putExtra("isbn", book.isbn);
            WishlistAdapter.this.mContext.startActivity(intent);
        }
    }
}
