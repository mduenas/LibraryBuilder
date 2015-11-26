package com.markduenas.librarybuilder.db.ISBNDb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markduenas on 11/25/15.
 */

public class Datum {

    @SerializedName("publisher_id")
    @Expose
    public String publisherId;
    @SerializedName("awards_text")
    @Expose
    public String awardsText;
    @SerializedName("dewey_normal")
    @Expose
    public String deweyNormal;
    @SerializedName("marc_enc_level")
    @Expose
    public String marcEncLevel;
    @SerializedName("language")
    @Expose
    public String language;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("title_latin")
    @Expose
    public String titleLatin;
    @SerializedName("summary")
    @Expose
    public String summary;
    @SerializedName("book_id")
    @Expose
    public String bookId;
    @SerializedName("notes")
    @Expose
    public String notes;
    @SerializedName("isbn10")
    @Expose
    public String isbn10;
    @SerializedName("lcc_number")
    @Expose
    public String lccNumber;
    @SerializedName("title_long")
    @Expose
    public String titleLong;
    @SerializedName("dewey_decimal")
    @Expose
    public String deweyDecimal;
    @SerializedName("edition_info")
    @Expose
    public String editionInfo;
    @SerializedName("isbn13")
    @Expose
    public String isbn13;
    @SerializedName("subject_ids")
    @Expose
    public List<String> subjectIds = new ArrayList<String>();
    @SerializedName("publisher_name")
    @Expose
    public String publisherName;
    @SerializedName("urls_text")
    @Expose
    public String urlsText;
    @SerializedName("author_data")
    @Expose
    public List<AuthorDatum> authorData = new ArrayList<AuthorDatum>();
    @SerializedName("publisher_text")
    @Expose
    public String publisherText;
    @SerializedName("physical_description_text")
    @Expose
    public String physicalDescriptionText;

}