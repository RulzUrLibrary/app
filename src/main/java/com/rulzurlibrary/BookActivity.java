package com.rulzurlibrary;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rulzurlibrary.R;
import com.rulzurlibrary.adapters.NotationAdapter;
import com.rulzurlibrary.common.Author;
import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.RulzUrLibraryService;
import com.rulzurlibrary.controllers.AddCollection;
import com.rulzurlibrary.controllers.AddWishlist;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookActivity extends AppCompatActivity {
    private final String TAG = "BookActivity";
    private Book book;

    private TextView title;
    private TextView authors;
    private TextView description;
    private ListView notations;
    private AddCollection addCollection;
    private AddWishlist addWishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        setActionBar();


        this.title = (TextView) findViewById(R.id.bookName);
        this.authors = (TextView) findViewById(R.id.bookAuthors);
        this.notations = (ListView) findViewById(R.id.bookNotations);
        this.description = (TextView) findViewById(R.id.bookDescription);
        this.addCollection = (AddCollection) findViewById(R.id.buttonCollection);
        this.addWishlist = (AddWishlist) findViewById(R.id.buttonWishlist);


        Intent intent = getIntent();
        if (intent.hasExtra("book")) {
            setBook((Book) intent.getParcelableExtra("book"));
        }
        if (intent.hasExtra("isbn")) {
            RulzUrLibraryService.client.getBook(intent.getStringExtra("isbn")).enqueue(new Callback<Book>() {
                @Override
                public void onResponse(Call<Book> call, Response<Book> response) {
                    if (response.isSuccessful()) {
                        Book book = response.body();
                        assert book != null;
                        setBook(book);
                    } else {
                        Log.e(TAG, response.toString());
                    }
                }

                @Override
                public void onFailure(Call<Book> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        }
    }

    public void setBook(Book book) {
        this.book = book;
        this.title.setText(book.title);
        this.authors.setText(authors());
        this.description.setText(book.description);
        this.notations.setAdapter(new NotationAdapter(this, book.notations));
        this.addCollection.setBook(book);
        this.addWishlist.setBook(book);
    }

    public void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
    }

    public String authors() {
        String res = "";

        if ((null == book.authors || book.authors.size() == 0)) {
            return res;
        } else {
            for (Author author : book.authors) {
                res += author.name + ", ";
            }
            return res.substring(0, res.length() - 2);
        }
    }
}
