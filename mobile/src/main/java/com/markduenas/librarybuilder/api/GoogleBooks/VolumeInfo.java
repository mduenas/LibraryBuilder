package com.markduenas.librarybuilder.api.GoogleBooks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markduenas on 11/25/15.
 */
public class VolumeInfo {

    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("authors")
    @Expose
    public List<String> authors = new ArrayList<String>();
    @SerializedName("publisher")
    @Expose
    public String publisher;
    @SerializedName("publishedDate")
    @Expose
    public String publishedDate;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("industryIdentifiers")
    @Expose
    public List<IndustryIdentifier> industryIdentifiers = new ArrayList<IndustryIdentifier>();
    @SerializedName("readingModes")
    @Expose
    public ReadingModes readingModes;
    @SerializedName("pageCount")
    @Expose
    public Integer pageCount;
    @SerializedName("printType")
    @Expose
    public String printType;
    @SerializedName("categories")
    @Expose
    public List<String> categories = new ArrayList<String>();
    @SerializedName("averageRating")
    @Expose
    public Double averageRating;
    @SerializedName("ratingsCount")
    @Expose
    public Integer ratingsCount;
    @SerializedName("maturityRating")
    @Expose
    public String maturityRating;
    @SerializedName("allowAnonLogging")
    @Expose
    public Boolean allowAnonLogging;
    @SerializedName("contentVersion")
    @Expose
    public String contentVersion;
    @SerializedName("imageLinks")
    @Expose
    public ImageLinks imageLinks;
    @SerializedName("language")
    @Expose
    public String language;
    @SerializedName("previewLink")
    @Expose
    public String previewLink;
    @SerializedName("infoLink")
    @Expose
    public String infoLink;
    @SerializedName("canonicalVolumeLink")
    @Expose
    public String canonicalVolumeLink;

}

