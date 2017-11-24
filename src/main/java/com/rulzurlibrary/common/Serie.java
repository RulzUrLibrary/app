package com.rulzurlibrary.common;

import java.util.ArrayList;

public class Serie {
    public int id;
    public String name;
    public String isbn;
    public String title;
    public String description;
    public ArrayList<Author> authors;
    public ArrayList<Book> volumes;

    public String title() {
        if (this.name == null) {
            return this.title;
        }
        return this.name;
    }

    public String getThumbName() {
        String isbn = (this.isbn == null ? volumes.get(0).isbn : this.isbn) + ".jpg";
        return RulzUrLibraryService.endpoint + "thumbs/" + isbn;
    }

    private int owned() {
        int count = 0;
        for (Book volume: this.volumes) {
            if (volume.owned) {
                count++;
            }
        }
        return count;
    }

    public boolean isBook() {
        return volumes == null;
    }

    public float ratio() {
        return this.owned() / this.volumes.size();
    }

    public String authors() {
        String res = "";

        if ((null == authors || authors.size() == 0)) {
            return res;
        } else {
            for (Author author : authors) {
                res += author.name + ", ";
            }
            return res.substring(0, res.length() - 2);
        }
    }

    public String volumes() {
        return String.format("%d / %d", owned(), this.volumes.size());
    }

}
