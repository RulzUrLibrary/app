package com.rulzurlibrary.common;

public class Book {
    public String isbn;
    public String title;
    public int number;
    public boolean owned;
    public String description;

    public Book(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return isbn + " " + title;
    }
}
