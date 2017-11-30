package com.rulzurlibrary;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.rulzurlibrary.adapters.NotationAdapter;
import com.rulzurlibrary.adapters.WishlistAdapter;
import com.rulzurlibrary.common.Author;
import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.RulzUrLibraryService;
import com.rulzurlibrary.common.Wishlists;
import com.rulzurlibrary.controllers.AddCollection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishlistActivity extends AppCompatActivity {
    private final String TAG = "WishlistActivity";
    private Wishlists wishlists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlists);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);

        RulzUrLibraryService.client.getWishlists().enqueue(new Callback<Wishlists>() {
            @Override
            public void onResponse(@NonNull Call<Wishlists> call, @NonNull Response<Wishlists> response) {
                if (response.isSuccessful()) {
                    // user object available
                    wishlists = response.body();
                } else {
                    Log.e(TAG, response.toString());
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(@NonNull Call<Wishlists> call, @NonNull Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
            }
        });

    }
}
