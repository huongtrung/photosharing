package com.hg.photoshare.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hg.photoshare.bean.UserBean;

/**
 * Created by GMORUNSYSTEM on 12/8/2016.
 */

public class CommentListData {
    @SerializedName("user")
    public UserBean user;
    @SerializedName("comment")
    public String comment;
    @SerializedName("created_at")
    public long createdAt;
}
