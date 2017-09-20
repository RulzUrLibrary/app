package com.rulzurlibrary.common;

import com.rulzurlibrary.RulzUrLibraryService;

import java.util.ArrayList;

public class Book {
    public String isbn;
    public String title;
    public int number;
    public String serie;
    public String thumbnail;
    public boolean owned;
    public String description;
    public ArrayList<Author> authors;


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

    public String getThumbName() {
        return RulzUrLibraryService.endpoint + this.thumbnail;
    }
}
