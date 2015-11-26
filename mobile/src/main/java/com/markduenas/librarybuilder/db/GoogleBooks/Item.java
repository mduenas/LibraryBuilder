package com.markduenas.librarybuilder.db.GoogleBooks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by markduenas on 11/25/15.
 */

public class Item {

    @SerializedName("kind")
    @Expose
    public String kind;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("etag")
    @Expose
    public String etag;
    @SerializedName("selfLink")
    @Expose
    public String selfLink;
    @SerializedName("volumeInfo")
    @Expose
    public VolumeInfo volumeInfo;
    @SerializedName("saleInfo")
    @Expose
    public SaleInfo saleInfo;
    @SerializedName("accessInfo")
    @Expose
    public AccessInfo accessInfo;
    @SerializedName("searchInfo")
    @Expose
    public SearchInfo searchInfo;

}
