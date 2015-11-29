package com.markduenas.librarybuilder.api.GoogleBooks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by markduenas on 11/25/15.
 */

public class SaleInfo {

    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("saleability")
    @Expose
    public String saleability;
    @SerializedName("isEbook")
    @Expose
    public Boolean isEbook;

}
