package com.hg.photoshare.api.respones;

import com.google.gson.annotations.SerializedName;
import com.hg.photoshare.data.HomeData;
import com.hg.photoshare.data.ImageUploadData;

import java.util.ArrayList;
import java.util.List;

import vn.app.base.api.response.BaseResponse;

/**
 * Created by Nart on 28/10/2016.
 */
public class ImageUploadRespones extends BaseResponse {
    @SerializedName("data")
    public ImageUploadData data;
}
