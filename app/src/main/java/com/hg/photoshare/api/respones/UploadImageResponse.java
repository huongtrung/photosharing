package com.hg.photoshare.api.respones;

import com.google.gson.annotations.SerializedName;
import com.hg.photoshare.bean.ProfileBean;
import com.hg.photoshare.data.TutorialData;

import vn.app.base.api.response.BaseResponse;

/**
 * Created by Nart on 26/10/2016.
 */
public class UploadImageResponse extends BaseResponse {
    public ProfileBean data;
}
