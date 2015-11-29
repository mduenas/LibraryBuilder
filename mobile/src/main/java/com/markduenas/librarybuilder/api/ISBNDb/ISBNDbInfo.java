package com.markduenas.librarybuilder.api.ISBNDb;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by markduenas on 11/25/15.
 */

public class ISBNDbInfo {

    @SerializedName("index_searched")
    @Expose
    public String indexSearched;
    @SerializedName("data")
    @Expose
    public List<Datum> data = new ArrayList<Datum>();
    @SerializedName("current_page")
    @Expose
    public Integer currentPage;
    @SerializedName("result_count")
    @Expose
    public Integer resultCount;
    @SerializedName("page_count")
    @Expose
    public Integer pageCount;

}
