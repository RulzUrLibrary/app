package com.rulzurlibrary.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.rulzurlibrary.BookActivity;
import com.rulzurlibrary.R;
import com.rulzurlibrary.ScanActivity;
import com.rulzurlibrary.SearchActivity;
import com.rulzurlibrary.SerieActivity;
import com.rulzurlibrary.adapters.BookAdapter;
import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.Books;
import com.rulzurlibrary.common.RulzUrLibraryService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        context = getContext();

        Button buttonSearch = view.findViewById(R.id.buttonSearch);
        Button buttonScan = view.findViewById(R.id.buttonScan);
        Button buttonType = view.findViewById(R.id.buttonType);

        buttonSearch.setOnClickListener(new SearchClickListener());
        buttonScan.setOnClickListener(new ScanClickListener());
        buttonType.setOnClickListener(new TypeClickListener());
        return view;
    }

    private class SearchClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "launch search activity");
            SearchFragment.this.startActivity(new Intent(context, SearchActivity.class));
        }
    }

    private class ScanClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "launch scan activity");
            SearchFragment.this.startActivity(new Intent(context, ScanActivity.class));
        }
    }

    private class TypeClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "launch type activity");

        }
    }
}