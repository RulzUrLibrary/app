package com.rulzurlibrary.common;

import com.rulzurlibrary.RulzUrLibraryService;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by max on 9/19/17.
 */

public class Serie {
    public int id;
    public String name;
    public String isbn;
    public String title;
    public String description;
    public ArrayList<Author> authors;
    public ArrayList<Book> volumes;

    public String Title() {
        if (this.name == null) {
            return this.title;
        }
        return this.name;
    }

    public String getThumbName() {
        String isbn = (this.isbn == null ? volumes.get(0).isbn : this.isbn) + ".jpg";
        return RulzUrLibraryService.endpoint + "/thumbs/" + isbn;
    }
}
