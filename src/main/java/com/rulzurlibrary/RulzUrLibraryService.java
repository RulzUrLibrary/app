package com.rulzurlibrary;

import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.Books;
import com.rulzurlibrary.common.Serie;
import com.rulzurlibrary.common.Series;

import java.util.List;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RulzUrLibraryService {
    String endpoint = "https://api.rulz.xyz/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(RulzUrLibraryService.endpoint)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Headers({"Content-Type: application/json"})
    @POST("/books/")
    Call<Book> postIsbn(@Body Book book);

    @GET("/series/")
    Call<Series> getSeries();

    @GET("/series/{id}")
    Call<Serie> getSerie(@Path("id") int serieId);

    @GET("/books/")
    Call<Books> search(@Query("search") String pattern);
}