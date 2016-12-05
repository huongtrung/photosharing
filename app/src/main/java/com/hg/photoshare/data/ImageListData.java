package com.hg.photoshare.data;

import com.google.gson.annotations.SerializedName;
import com.hg.photoshare.bean.ImageBean;
import com.hg.photoshare.bean.UserBean;

/**
 * Created by GMORUNSYSTEM on 12/1/2016.
 */

public class ImageListData {
    @SerializedName("user")
    public UserBean user;
    @SerializedName("image")
    public ImageBean image;
}
