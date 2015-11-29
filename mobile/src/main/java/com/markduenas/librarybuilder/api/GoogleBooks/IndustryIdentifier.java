package com.markduenas.librarybuilder.api.GoogleBooks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by markduenas on 11/25/15.
 */

public class IndustryIdentifier {

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("identifier")
    @Expose
    public String identifier;

}
