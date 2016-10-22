package com.hg.photoshare.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nart on 21/10/2016.
 */
public class ProfileBean {
    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("avatar")
    @Expose
    public String avatar;
    @SerializedName("createDate")
    @Expose
    public Integer createDate;
}
