package com.rulzurlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.rulzurlibrary.common.Author;
import com.rulzurlibrary.common.Book;

public class BookActivity extends AppCompatActivity {
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        book = getIntent().getParcelableExtra("book");

        TextView title = (TextView) findViewById(R.id.bookName);
        title.setText(book.title);

//        TextView authors = (TextView) findViewById(R.id.bookAuthors);
//        authors.setText(authors());
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
