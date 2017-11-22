package com.rulzurlibrary;

import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.Books;
import com.rulzurlibrary.common.Isbns;
import com.rulzurlibrary.common.Serie;
import com.rulzurlibrary.common.Series;
import com.rulzurlibrary.common.ServiceGenerator;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RulzUrLibraryService {
    String endpoint = "https://api.rulz.xyz/";
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(RulzUrLibraryService.endpoint)
            .client(new OkHttpClient.Builder().addInterceptor(interceptor).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    RulzUrLibraryService client = ServiceGenerator.createService(RulzUrLibraryService.class, "foo", "bar");

    @Headers({"Content-Type: application/json"})
    @POST("/books/")
    Call<Book> postIsbn(@Body Book book);

    @Headers({"Content-Type: application/json"})
    @PUT("/books/")
    Call<Isbns> putBook(@Body Isbns isbns);

    @GET("/series/")
    Call<Series> getSeries();

    @GET("/series/{id}")
    Call<Serie> getSerie(@Path("id") int serieId);

    @GET("/books/")
    Call<Books> search(@Query("search") String pattern);
}