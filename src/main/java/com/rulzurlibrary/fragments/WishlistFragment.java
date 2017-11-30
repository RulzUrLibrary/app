package com.rulzurlibrary.fragments;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rulzurlibrary.R;
import com.rulzurlibrary.adapters.WishlistAdapter;
import com.rulzurlibrary.common.Wishlist;

import java.util.List;

public class WishlistFragment extends Fragment {
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
        return view;
    }
}
