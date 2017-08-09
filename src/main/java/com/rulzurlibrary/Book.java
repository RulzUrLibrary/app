package com.rulzurlibrary;

/**
 * Created by max on 8/9/17.
 */

public class Book {
    public final String Isbn;
    public final String Title;
    public final String Description;

    public Book(String isbn, String title, String description) {
        Isbn = isbn;
        Title = title;
        Description = description;
    }
}
