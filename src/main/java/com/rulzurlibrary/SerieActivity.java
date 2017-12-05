package com.rulzurlibrary;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.rulzurlibrary.adapters.BookAdapter;
import com.rulzurlibrary.common.RulzUrLibraryService;
import com.rulzurlibrary.common.Serie;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SerieActivity extends AppCompatActivity {
    private final String TAG = "SerieActivity";
    private ListView listView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_serie);
        setActionBar();

        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, String.format("clicking book at position: %d", position));
            }
        });

        RulzUrLibraryService.client.getSerie(getIntent().getIntExtra("serie", 0)).enqueue(new Callback<Serie>() {
            @Override
            public void onResponse(@NonNull Call<Serie> call, @NonNull Response<Serie> response) {
                if (response.isSuccessful()) {
                    Serie serie = response.body();
                    assert serie != null;
                    listView.setAdapter(new BookAdapter(SerieActivity.this, serie));
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
}
