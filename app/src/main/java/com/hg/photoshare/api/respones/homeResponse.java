package com.hg.photoshare.api.respones;

import com.google.gson.annotations.SerializedName;
import com.hg.photoshare.data.HomeData;
import com.hg.photoshare.data.TutorialData;
import com.hg.photoshare.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;

import vn.app.base.api.response.BaseResponse;

/**
 * Created by Nart on 25/10/2016.
 */
public class HomeResponse extends BaseResponse {
    @SerializedName("data")
    public List<HomeData> data=new ArrayList<>();
}
