package com.rulzurlibrary;

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

import com.rulzurlibrary.adapters.SerieAdapter;
import com.rulzurlibrary.common.Serie;
import com.rulzurlibrary.common.Series;
import com.rulzurlibrary.common.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionFragment extends Fragment {

    private ArrayList<Serie> serieList;
    private SerieAdapter adapter;
    private ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /**
         * Array List for Binding Data from JSON to this List
         */
        View view = inflater.inflate(R.layout.collection_fragment, container, false);


        listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                Fragment fragment = new SerieFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("serieId", serieList.get(position).id);
                fragment.setArguments(bundle);
                trans.replace(R.id.root_frame, fragment);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
            }
        });

		/* Inflate the layout for this fragment */


        RulzUrLibraryService.client.getSeries().enqueue(new Callback<Series>() {
            @Override
            public void onResponse(@NonNull Call<Series> call, @NonNull Response<Series> response) {
                if (response.isSuccessful()) {
                    // user object available
                    Series series = response.body();
                    assert series != null;
                    try {
                        adapter = new SerieAdapter(getContext(), series.series);
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
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
            }
        });


        return view;
    }


}
