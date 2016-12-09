package com.hg.photoshare.api.respones;

import com.google.gson.annotations.SerializedName;
import com.hg.photoshare.data.CommentListData;

import java.util.ArrayList;
import java.util.List;

import vn.app.base.api.response.BaseResponse;

/**
 * Created by GMORUNSYSTEM on 12/8/2016.
 */

public class CommentListResponse extends BaseResponse {
    @SerializedName("data")
    public List<CommentListData> data = new ArrayList<>();
}
