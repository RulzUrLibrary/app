package com.rulzurlibrary;

/**
 * Created by max on 8/9/17.
 */

public class Book {
    String isbn;
    String title;
    String description;

    public Book(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return isbn + " " + title;
    }
}
