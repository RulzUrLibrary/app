package com.rulzurlibrary;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.rulzurlibrary.common.Book;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {
    RulzUrLibraryService rulzUrLibraryService = RulzUrLibraryService.retrofit.create(RulzUrLibraryService.class);
    private static final String TAG = "SearchFragment";

    private ArrayList<Book> bookList;
    private BookAdapter adapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        SearchView searchView = view.findViewById(R.id.action_search);
        searchView.setOnQueryTextListener(this);

        listView = view.findViewById(R.id.listView);
        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        final Call<List<Book>> call = rulzUrLibraryService.search(s);
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(@NonNull Call<List<Book>> call, @NonNull Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    // user object available
                    List<Book> books = response.body();
                    assert books != null;
                    adapter = new BookAdapter(getContext(), books);
                    bookList = new ArrayList<>(books);
                    listView.setAdapter(adapter);
                } else {
                    Log.d(TAG, response.toString());
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                // something went completely south (like no internet connection)
                Log.d(TAG, t.getMessage());
            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
