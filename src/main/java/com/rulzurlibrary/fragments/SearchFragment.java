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
import android.widget.ListView;
import android.widget.SearchView;

import com.rulzurlibrary.BookActivity;
import com.rulzurlibrary.R;
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

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static final String TAG = "SearchFragment";
    private List<Book> books;

    private ListView listView;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        SearchView searchView = view.findViewById(R.id.action_search);
        searchView.setOnQueryTextListener(this);

        context = getContext();
        listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new BookClickListener());
        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        RulzUrLibraryService.client.search(s).enqueue(new Callback<Books>() {
            @Override
            public void onResponse(@NonNull Call<Books> call, @NonNull Response<Books> response) {
                if (response.isSuccessful()) {
                    Books body = response.body();
                    assert body != null;
                    books = body.books; //unwrap
                    listView.setAdapter(new BookAdapter(getContext(), books));
                } else {
                    Log.d(TAG, response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Books> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    private class BookClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, String.format("launching book activity at position: %d", position));
            Intent intent = new Intent(context, BookActivity.class);
            intent.putExtra("isbn", books.get(position).isbn);
            SearchFragment.this.startActivity(intent);
        }
    }
}