package com.rulzurlibrary.common;

import java.util.ArrayList;

/**
 * Created by max on 9/19/17.
 */

public class Serie {
    public int id;
    public String name;
    public String title;
    public ArrayList<Author> authors;
    public ArrayList<Book> volumes;

    public String Title() {
        if (this.name == null) {
            return this.title;
        }
        return this.name;
    }
}
