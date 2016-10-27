package com.hg.photoshare.data;

import android.media.Image;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hg.photoshare.bean.ImageBean;
import com.hg.photoshare.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nart on 25/10/2016.
 */
public class HomeData {
    @SerializedName("user")
    public UserBean user;
    @SerializedName("image")
    public ImageBean image;
}
