package com.rulzurlibrary.controllers;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.rulzurlibrary.R;
import com.rulzurlibrary.WishlistActivity;
import com.rulzurlibrary.common.Book;

public class AddWishlist extends android.support.v7.widget.AppCompatButton implements View.OnClickListener  {
    private final String TAG = "AddWishlist";
    private Book book;

    public AddWishlist(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setText(R.string.add_wishlist);
        this.setOnClickListener(this);
    }


    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public void onClick(View view) {
        // Do something in response to button click
        Log.d(TAG, "launching wishlist activity");
        Intent intent = new Intent(getContext(), WishlistActivity.class);
        intent.putExtra("book", book);
        getContext().startActivity(intent);
    }
}
