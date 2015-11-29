package com.markduenas.librarybuilder.api.GoogleBooks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markduenas on 11/25/15.
 */

public class GoogleBookInfo {

    @SerializedName("kind")
    @Expose
    public String kind;
    @SerializedName("totalItems")
    @Expose
    public Integer totalItems;
    @SerializedName("items")
    @Expose
    public List<Item> items = new ArrayList<Item>();

}


