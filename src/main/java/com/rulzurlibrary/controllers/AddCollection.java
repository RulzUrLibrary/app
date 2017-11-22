package com.rulzurlibrary.controllers;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rulzurlibrary.R;
import com.rulzurlibrary.RulzUrLibraryService;
import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.Isbns;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddCollection implements View.OnClickListener {
    private final String TAG = "AddCollection";
    private Book book;

    public AddCollection(Book book) {
        this.book = book;
    }

    @Override
    public void onClick(View view) {
        final Button button = (Button) view;
        // Do something in response to button click
        RulzUrLibraryService.client.putBook(new Isbns(book.isbn)).enqueue(new Callback<Isbns>() {
            @Override
            public void onResponse(@NonNull Call<Isbns> call, @NonNull Response<Isbns> response) {
                if (response.isSuccessful()) {
                    Isbns isbns = response.body();
                    assert isbns != null;
                    Log.d(TAG, String.format("added: %d", isbns.added));
                    button.setEnabled(false);
                    button.setText(R.string.in_collection);
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
