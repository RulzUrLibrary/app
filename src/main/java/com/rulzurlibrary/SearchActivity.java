package com.rulzurlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.rulzurlibrary.adapters.BookAdapter;
import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.Books;
import com.rulzurlibrary.common.RulzUrLibraryService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "SearchFragment";
    private List<Book> books;

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setActionBar();

        SearchView searchView = (SearchView) findViewById(R.id.action_search);
        searchView.setOnQueryTextListener(this);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new BookClickListener());
    }


    public void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
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
                    listView.setAdapter(new BookAdapter(SearchActivity.this, books));
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
            Intent intent = new Intent(SearchActivity.this, BookActivity.class);
            intent.putExtra("isbn", books.get(position).isbn);
            SearchActivity.this.startActivity(intent);
        }
    }
}
