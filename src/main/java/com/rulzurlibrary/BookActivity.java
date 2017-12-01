package com.rulzurlibrary;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rulzurlibrary.R;
import com.rulzurlibrary.adapters.NotationAdapter;
import com.rulzurlibrary.common.Author;
import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.controllers.AddCollection;

public class BookActivity extends AppCompatActivity {
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        setActionBar();

        book = getIntent().getParcelableExtra("book");

        TextView title = (TextView) findViewById(R.id.bookName);
        title.setText(book.title);

        TextView authors = (TextView) findViewById(R.id.bookAuthors);
        authors.setText(authors());

        ListView notations = (ListView) findViewById(R.id.bookNotations);
        notations.setAdapter(new NotationAdapter(this, book.notations));

        TextView description = (TextView) findViewById(R.id.bookDescription);
        description.setText(book.description);

        AddCollection addCollection = (AddCollection) findViewById(R.id.buttonCollection);
        addCollection.setBook(book);

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
