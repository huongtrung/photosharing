package com.hg.photoshare.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nart on 25/10/2016.
 */
public class UserBean {
    @SerializedName("_id")
    public String id;
    @SerializedName("username")
    public String username;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("is_following")
    public Boolean isFollowing;
}
