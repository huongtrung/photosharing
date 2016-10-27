package com.hg.photoshare.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nart on 24/10/2016.
 */
public class TutorialBean {
    @SerializedName("title")
    public String title;
    @SerializedName("image")
    public String image;
    @SerializedName("show_avatar")
    public Boolean showAvatar;
}
