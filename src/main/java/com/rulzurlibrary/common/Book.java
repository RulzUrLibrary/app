package com.rulzurlibrary.common;

/**
 * Created by max on 8/9/17.
 */

public class Book {
    public String isbn;
    public String title;
    public int number;
    public boolean owned;
    String description;

    public Book(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return isbn + " " + title;
    }
}
