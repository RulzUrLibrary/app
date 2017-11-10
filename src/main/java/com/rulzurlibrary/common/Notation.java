package com.rulzurlibrary.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by max on 11/10/17.
 */

public class Notation implements Parcelable {
    public  String provider;
    public Float note;
    public int max;

    protected Notation(Parcel in) {
        provider = in.readString();
        if (in.readByte() == 0) {
            note = null;
        } else {
            note = in.readFloat();
        }
        max = in.readInt();
    }

    public static final Creator<Notation> CREATOR = new Creator<Notation>() {
        @Override
        public Notation createFromParcel(Parcel in) {
            return new Notation(in);
        }

        @Override
        public Notation[] newArray(int size) {
            return new Notation[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(provider);
        if (note == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(note);
        }
        parcel.writeInt(max);
    }
}
