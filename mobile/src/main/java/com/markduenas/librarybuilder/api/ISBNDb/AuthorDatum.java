package com.markduenas.librarybuilder.api.ISBNDb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by markduenas on 11/25/15.
 */

public class AuthorDatum {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;

}
