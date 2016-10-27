package com.hg.photoshare.api.respones;

import com.google.gson.annotations.SerializedName;
import com.hg.photoshare.bean.ProfileBean;
import com.hg.photoshare.bean.TutorialBean;
import com.hg.photoshare.data.TutorialData;

import java.util.ArrayList;
import java.util.List;

import vn.app.base.api.response.BaseResponse;

/**
 * Created by Nart on 24/10/2016.
 */
public class TutorialResponse extends BaseResponse {
    @SerializedName("data")
    public TutorialData data;
}
