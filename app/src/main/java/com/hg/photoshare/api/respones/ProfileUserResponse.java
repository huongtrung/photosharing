package com.hg.photoshare.api.respones;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hg.photoshare.bean.ProfileUserBean;

import vn.app.base.api.response.BaseResponse;

/**
 * Created by GMORUNSYSTEM on 11/29/2016.
 */

public class ProfileUserResponse extends BaseResponse{
    @SerializedName("data")
    public ProfileUserBean data;
}
