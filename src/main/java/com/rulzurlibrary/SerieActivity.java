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

import com.rulzurlibrary.adapters.BookAdapter;
import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.RulzUrLibraryService;
import com.rulzurlibrary.common.Serie;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SerieActivity extends AppCompatActivity {
    private final String TAG = "SerieActivity";
    private List<Book> books;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_serie);
        setActionBar();

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new SerieClickListener());

        RulzUrLibraryService.client.getSerie(getIntent().getIntExtra("serie", 0)).enqueue(new Callback<Serie>() {
            @Override
            public void onResponse(@NonNull Call<Serie> call, @NonNull Response<Serie> response) {
                if (response.isSuccessful()) {
                    Serie serie = response.body();
                    assert serie != null;
                    listView.setAdapter(new BookAdapter(SerieActivity.this, serie));
                    books = serie.volumes;
                    setTitle(serie.name);
                } else {
                    Log.e(TAG, response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Serie> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
    }

    private class SerieClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, String.format("launching book activity at position: %d", position));
            Intent intent = new Intent(SerieActivity.this, BookActivity.class);
            intent.putExtra("isbn", books.get(position).isbn);
            SerieActivity.this.startActivity(intent);
        }
    }
}
