package com.rulzurlibrary.common;

import java.util.List;

public class Wishlist {
    public String name;
    public String uuid;
    public List<Book> books;

    public Wishlist(String uuid) {
        this.uuid = uuid;
    }
}
