package com.rulzurlibrary.common;

import java.util.ArrayList;
import java.util.List;

public class Isbns {

    public List<String> isbns;
    public int added;
    public int deleted;

    public Isbns(List<String> isbns) {
        this.isbns = isbns;
    }

    public Isbns(String isbn) {
        this.isbns = new ArrayList<>();
        this.isbns.add(isbn);
    }
}
