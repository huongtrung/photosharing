package com.hg.photoshare.data;

import com.google.gson.annotations.SerializedName;
import com.hg.photoshare.bean.ImageBean;
import com.hg.photoshare.bean.UserBean;

/**
 * Created by Nart on 28/10/2016.
 */
public class ImageUploadData {
    @SerializedName("user")
    public UserBean user;
    @SerializedName("image")
    public ImageBean image;
}
