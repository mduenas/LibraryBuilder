package com.markduenas.librarybuilder.api.GoogleBooks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by markduenas on 11/25/15.
 */

public class AccessInfo {

    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("viewability")
    @Expose
    public String viewability;
    @SerializedName("embeddable")
    @Expose
    public Boolean embeddable;
    @SerializedName("publicDomain")
    @Expose
    public Boolean publicDomain;
    @SerializedName("textToSpeechPermission")
    @Expose
    public String textToSpeechPermission;
    @SerializedName("epub")
    @Expose
    public Epub epub;
    @SerializedName("pdf")
    @Expose
    public Pdf pdf;
    @SerializedName("webReaderLink")
    @Expose
    public String webReaderLink;
    @SerializedName("accessViewStatus")
    @Expose
    public String accessViewStatus;
    @SerializedName("quoteSharingAllowed")
    @Expose
    public Boolean quoteSharingAllowed;

}
