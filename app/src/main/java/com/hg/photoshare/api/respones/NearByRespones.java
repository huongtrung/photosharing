package com.hg.photoshare.api.respones;

import com.google.gson.annotations.SerializedName;
import com.hg.photoshare.data.ImageListData;
import com.hg.photoshare.data.NearByData;

import java.util.ArrayList;
import java.util.List;

import vn.app.base.api.response.BaseResponse;

/**
 * Created by GMORUNSYSTEM on 12/9/2016.
 */

public class NearByRespones extends BaseResponse {
    @SerializedName("data")
    public List<NearByData> data = new ArrayList<>();
}
