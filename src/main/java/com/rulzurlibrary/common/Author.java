package com.rulzurlibrary.common;

import android.os.Parcel;
import android.os.Parcelable;

public class Author implements Parcelable{
    public String name;

    protected Author(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Author> CREATOR = new Creator<Author>() {
        @Override
        public Author createFromParcel(Parcel in) {
            Author author = new Author(in);
            return author;
        }

        @Override
        public Author[] newArray(int size) {
            return new Author[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
    }
}
