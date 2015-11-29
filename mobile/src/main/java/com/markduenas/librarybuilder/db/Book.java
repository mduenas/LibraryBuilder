package com.markduenas.librarybuilder.db;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by markduenas on 11/28/15.
 */

@DatabaseTable
public class Book implements Parcelable {

    @DatabaseField
    public String title;
    @DatabaseField
    public String author;
    @DatabaseField
    public String publisher;
    @DatabaseField
    public String publication;
    @DatabaseField
    public String isbn13;
    @DatabaseField
    public String isbn10;
    @DatabaseField
    public String description;
    @DatabaseField
    public String category;
    @DatabaseField
    public String language;
    @DatabaseField
    public int pages;
    @DatabaseField
    public String format;

    @DatabaseField
    public String dateAdded;
    @DatabaseField
    public boolean read;
    @DatabaseField
    public boolean reading;
    @DatabaseField
    public int rating;
    @DatabaseField
    public String dateStarted;
    @DatabaseField
    public String dateFinished;
    @DatabaseField
    public String note;

    @DatabaseField
    public boolean loaned;
    @DatabaseField
    public String loanedTo;
    @DatabaseField
    public String dateLoaned;

    @DatabaseField
    public byte[] thumbNail;

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle val = new Bundle();
        val.putString("title", this.title);
        val.putString("author", this.author);
        val.putString("publisher", this.publisher);
        val.putString("publication", this.publication);
        val.putString("isbn13", this.isbn13);
        val.putString("isbn10", this.isbn10);
        val.putString("description", this.description);
        val.putString("category", this.category);
        val.putString("language", this.language);
        val.putInt("pages", this.pages);
        val.putString("format", this.format);
        val.putString("dateAdded", this.dateAdded);
        val.putBoolean("read", this.read);
        val.putBoolean("reading", this.reading);
        val.putString("dateStarted", this.dateStarted);
        val.putString("dateFinished", this.dateFinished);
        val.putString("note", this.note);
        val.putBoolean("loaned", this.loaned);
        val.putString("loanedTo", this.loanedTo);
        val.putByteArray("thumbNail", this.thumbNail);
        dest.writeBundle(val);
    }

    public Book() {
    }

    public Book(Parcel in) {

        Bundle val = in.readBundle();
        this.title = val.getString("title");
        this.author = val.getString("author");
        this.publisher = val.getString("publisher");
        this.publication = val.getString("publication");
        this.isbn13 = val.getString("isbn13");
        this.isbn10 = val.getString("isbn10");
        this.description = val.getString("description");
        this.category = val.getString("category");
        this.language = val.getString("language");
        this.pages = val.getInt("pages");
        this.format = val.getString("format");
        this.dateAdded = val.getString("dateAdded");
        this.read = val.getBoolean("read");
        this.reading = val.getBoolean("reading");
        this.dateStarted = val.getString("dateStarted");
        this.dateFinished = val.getString("dateFinished");
        this.note = val.getString("note");
        this.loaned = val.getBoolean("loaned");
        this.loanedTo = val.getString("loanedTo");
        this.thumbNail = val.getByteArray("thumbNail");
    }
}
