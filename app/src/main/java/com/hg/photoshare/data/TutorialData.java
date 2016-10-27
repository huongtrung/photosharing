package com.hg.photoshare.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hg.photoshare.bean.ProfileBean;
import com.hg.photoshare.bean.TutorialBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nart on 25/10/2016.
 */
public class TutorialData {
        @SerializedName("tutorial")
        @Expose
        public List<TutorialBean> tutorial = new ArrayList<>();

        @SerializedName("user")
        @Expose
        public ProfileBean user;

}
