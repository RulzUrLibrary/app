package com.rulzurlibrary.common;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;

import com.rulzurlibrary.RulzUrLibraryService;

import java.util.ArrayList;

public class Book implements Parcelable {
    public String isbn;
    public String title;
    public int number;
    public String serie;
    public boolean owned;
    public String description;
    public ArrayList<Author> authors;
    public ArrayList<Notation> notations;


    public Book(String isbn) {
        this.isbn = isbn;
    }

    protected Book(Parcel in) {
        isbn = in.readString();
        title = in.readString();
        description = in.readString();
        serie = in.readString();
        number = in.readInt();
        owned = in.readByte() != 0;
        authors = in.createTypedArrayList(Author.CREATOR);
        notations = in.createTypedArrayList(Notation.CREATOR);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

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
        return RulzUrLibraryService.endpoint + "thumbs/" + this.isbn + ".jpg";
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(isbn);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(serie);
        parcel.writeInt(number);
        parcel.writeByte((byte) (owned ? 1 : 0));
        parcel.writeTypedList(authors);
        parcel.writeTypedList(notations);
    }
}
