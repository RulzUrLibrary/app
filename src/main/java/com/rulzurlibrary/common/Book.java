package com.rulzurlibrary.common;

public class Book {
    public String isbn;
    public String title;
    public int number;
    public String serie;
    public boolean owned;
    public String description;

    public Book(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return isbn + " " + title;
    }

    public String title() {
        if (serie == null) {
            return title;
        } else if (title == null) {
            return String.format("%s - %d", serie, number);
        } else {
            return String.format("%s - %d: %s", serie, number, title);
        }
    }
}
