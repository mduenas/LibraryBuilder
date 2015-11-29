package com.markduenas.librarybuilder.api.GoogleBooks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by markduenas on 11/25/15.
 */

public class ReadingModes {

    @SerializedName("text")
    @Expose
    public Boolean text;
    @SerializedName("image")
    @Expose
    public Boolean image;

}
