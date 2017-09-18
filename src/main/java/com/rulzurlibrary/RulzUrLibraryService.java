package com.rulzurlibrary;

import com.rulzurlibrary.common.Book;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RulzUrLibraryService {
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.rulz.xyz/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST("books/")
    Call<Book> postIsbn(@Body Book book);
}