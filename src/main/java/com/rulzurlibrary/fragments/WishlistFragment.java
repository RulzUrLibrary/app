package com.rulzurlibrary.fragments;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rulzurlibrary.R;
import com.rulzurlibrary.adapters.WishlistAdapter;
import com.rulzurlibrary.common.RulzUrLibraryService;
import com.rulzurlibrary.common.Wishlist;
import com.rulzurlibrary.common.Wishlists;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishlistFragment extends Fragment {
    private final String TAG = "WishlistFragment";
    private List<Wishlist> bookList;
    private WishlistAdapter adapter;
    private ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /**
         * Array List for Binding Data from JSON to this List
         */
        final View view = inflater.inflate(R.layout.wishlist_fragment, container, false);

        listView = view.findViewById(R.id.listView);

        RulzUrLibraryService.client.getWishlists().enqueue(new Callback<Wishlists>() {
            @Override
            public void onResponse(@NonNull Call<Wishlists> call, @NonNull Response<Wishlists> response) {
                if (response.isSuccessful()) {
                    // user object available
                    Wishlists wishlists = response.body();
                    assert wishlists != null;
                    try {
                        adapter = new WishlistAdapter(getContext(), wishlists.wishlists);

                        listView.setAdapter(adapter);
                    } catch (NullPointerException e) {
                        Log.e(TAG, e.toString());
                    }
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

        return view;
    }


}
