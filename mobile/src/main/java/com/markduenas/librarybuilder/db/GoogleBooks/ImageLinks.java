package com.markduenas.librarybuilder.db.GoogleBooks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by markduenas on 11/25/15.
 */

public class ImageLinks {

    @SerializedName("smallThumbnail")
    @Expose
    public String smallThumbnail;
    @SerializedName("thumbnail")
    @Expose
    public String thumbnail;

}
