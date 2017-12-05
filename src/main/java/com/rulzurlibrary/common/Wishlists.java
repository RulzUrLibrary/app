package com.rulzurlibrary.common;

import java.util.List;

public class Wishlists {

    public List<Wishlist> wishlists;

    public static class Post {
        public List<String> wishlists;

        public Post(List<String> wishlists) { this.wishlists = wishlists; }
    }
}