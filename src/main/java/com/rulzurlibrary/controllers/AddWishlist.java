package com.rulzurlibrary.controllers;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.rulzurlibrary.R;
import com.rulzurlibrary.WishlistActivity;

public class AddWishlist extends android.support.v7.widget.AppCompatButton implements View.OnClickListener  {
    private final String TAG = "AddWishlist";

    public AddWishlist(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setText(R.string.add_wishlist);
        this.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        // Do something in response to button click
        Log.d(TAG, "launching wishlist activity");
        Intent intent = new Intent(getContext(), WishlistActivity.class);
        getContext().startActivity(intent);
    }
}
