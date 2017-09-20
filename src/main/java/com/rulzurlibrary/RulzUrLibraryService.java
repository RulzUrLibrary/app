package com.rulzurlibrary;

import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.Serie;
import com.rulzurlibrary.common.Series;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RulzUrLibraryService {
    public static final String endpoint = "https://api.rulz.xyz/";
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(RulzUrLibraryService.endpoint)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST("/books/")
    Call<Book> postIsbn(@Body Book book);

    @GET("/series/")
    Call<Series> getSeries();

    @GET("/series/{id}")
    Call<Serie> getSerie(@Path("id") int serieId);
}