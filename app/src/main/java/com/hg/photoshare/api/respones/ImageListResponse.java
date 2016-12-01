package com.hg.photoshare.api.respones;

import com.google.gson.annotations.SerializedName;

import com.hg.photoshare.data.ImageListData;

import java.util.ArrayList;
import java.util.List;

import vn.app.base.api.response.BaseResponse;

/**
 * Created by GMORUNSYSTEM on 12/1/2016.
 */

public class ImageListResponse extends BaseResponse {
    @SerializedName("data")
    public List<ImageListData> data = new ArrayList<>();
}
