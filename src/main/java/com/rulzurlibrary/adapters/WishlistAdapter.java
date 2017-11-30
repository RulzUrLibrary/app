package com.rulzurlibrary.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.rulzurlibrary.R;
import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.Wishlist;

import java.util.List;

public class WishlistAdapter extends BaseExpandableListAdapter {
    private final String TAG = "WishlistAdapter";
    private List<Wishlist> wishlists;
    private LayoutInflater mInflater;

    public WishlistAdapter(Context context, List<Wishlist> wishlists) {
        this.mInflater = LayoutInflater.from(context);
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
        wishlistName.setText(wishlists.get(i).name);
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
        bookName.setText(book.title());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
