package com.rulzurlibrary.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.rulzurlibrary.R;
import com.rulzurlibrary.common.RulzUrLibraryService;
import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.Isbns;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddCollection extends android.support.v7.widget.AppCompatButton implements View.OnClickListener  {
    private final String TAG = "AddCollection";
    private Book book;

    public AddCollection(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setText(R.string.add_collection);
        this.setOnClickListener(this);
    }


    public void setBook(Book book) {
        this.book = book;
        setOwned(book.owned);
    }

    private void setOwned(boolean owned) {
        book.owned = owned;
        if (owned) {
            setEnabled(false);
            setText(R.string.in_collection);
        }
    }

    @Override
    public void onClick(View view) {
        // Do something in response to button click
        RulzUrLibraryService.client.putBook(new Isbns(book.isbn)).enqueue(new Callback<Isbns>() {
            @Override
            public void onResponse(@NonNull Call<Isbns> call, @NonNull Response<Isbns> response) {
                if (response.isSuccessful()) {
                    Isbns isbns = response.body();
                    assert isbns != null;
                    Log.d(TAG, String.format("added: %d", isbns.added));
                    setOwned(true);
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
}
