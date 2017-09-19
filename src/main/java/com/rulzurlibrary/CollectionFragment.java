package com.rulzurlibrary;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rulzurlibrary.common.Serie;
import com.rulzurlibrary.common.Series;
import com.rulzurlibrary.common.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */

        RulzUrLibraryService rulzUrLibraryService =
                ServiceGenerator.createService(RulzUrLibraryService.class, "foo", "bar");
        Call<Series> call = rulzUrLibraryService.getSeries();
        call.enqueue(new Callback<Series>() {
            @Override
            public void onResponse(@NonNull Call<Series> call, @NonNull Response<Series> response) {
                if (response.isSuccessful()) {
                    // user object available
                    Series series = response.body();
                    assert series != null;
                    for (Serie serie: series.series) {
                        Log.d("success", serie.Title());
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
        return inflater.inflate(R.layout.collection_fragment, container, false);
    }


}
