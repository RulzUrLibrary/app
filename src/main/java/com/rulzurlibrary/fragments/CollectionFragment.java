package com.rulzurlibrary.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rulzurlibrary.R;
import com.rulzurlibrary.SerieActivity;
import com.rulzurlibrary.adapters.SerieAdapter;
import com.rulzurlibrary.common.RulzUrLibraryService;
import com.rulzurlibrary.common.Serie;
import com.rulzurlibrary.common.Series;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionFragment extends Fragment {

    private final String TAG = "CollectionFragment";
    private ArrayList<Serie> serieList;
    private SerieAdapter adapter;
    private ListView listView;
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collection_fragment, container, false);

        context = getContext();
        listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new SerieClickListener());

        RulzUrLibraryService.client.getSeries().enqueue(new Callback<Series>() {
            @Override
            public void onResponse(@NonNull Call<Series> call, @NonNull Response<Series> response) {
                if (response.isSuccessful()) {
                    // user object available
                    Series series = response.body();
                    assert series != null;
                    try {
                        adapter = new SerieAdapter(context, series.series);
                        serieList = new ArrayList<>(series.series);
                        listView.setAdapter(adapter);
                    } catch (NullPointerException e) {
                        Log.d("foo", e.toString());
                    }
                    for (Serie serie : series.series) {
                        Log.d("success", serie.title());
                    }
                } else {
                    Log.d("error", response.toString());
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(@NonNull Call<Series> call, @NonNull Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


        return view;
    }

    private class SerieClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, String.format("launching serie activity at position: %d", position));
            Intent intent = new Intent(context, SerieActivity.class);
            intent.putExtra("serie", serieList.get(position).id);
            context.startActivity(intent);
        }
    }
}
