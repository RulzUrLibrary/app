package com.rulzurlibrary;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.rulzurlibrary.adapters.BookAdapter;
import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.Serie;
import com.rulzurlibrary.common.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SerieFragment extends Fragment{
    private ArrayList<Book> bookList;
    private BookAdapter adapter;
    private ListView listView;
    TextView serieName;
    TextView serieAuthors;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /**
         * Array List for Binding Data from JSON to this List
         */
        final View view = inflater.inflate(R.layout.serie_layout, container, false);
        Bundle bundle = this.getArguments();

        listView = view.findViewById(R.id.listView);
        serieName = view.findViewById(R.id.serieName);
        serieAuthors = view.findViewById(R.id.serieAuthors);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("foo", String.format("%d", position));
            }
        });

		/* Inflate the layout for this fragment */

        RulzUrLibraryService rulzUrLibraryService =
                ServiceGenerator.createService(RulzUrLibraryService.class, "foo", "bar");
        final Call<Serie> call = rulzUrLibraryService.getSerie(bundle.getInt("serieId"));


        call.enqueue(new Callback<Serie>() {
            @Override
            public void onResponse(@NonNull Call<Serie> call, @NonNull Response<Serie> response) {
                if (response.isSuccessful()) {
                    // user object available
                    Serie serie = response.body();
                    assert serie != null;
                    adapter = new BookAdapter(getContext(), serie.volumes);
                    bookList = new ArrayList<>(serie.volumes);
                    listView.setAdapter(adapter);

                    serieName.setText(serie.name);
                    serieAuthors.setText(serie.authors());
                } else {
                    Log.d("error", response.toString());
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(@NonNull Call<Serie> call, @NonNull Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
            }
        });


        return view;
    }


}
