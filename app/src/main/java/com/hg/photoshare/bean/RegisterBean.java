package com.hg.photoshare.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nart on 21/10/2016.
 */
public class RegisterBean {
    @SerializedName("_id")
    public String id;
    @SerializedName("token")
    public String token;
    @SerializedName("username")
    public String username;
    @SerializedName("email")
    public String email;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("create_date")
    public Integer createDate;
}
