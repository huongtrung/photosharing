package com.hg.photoshare.api.request;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.hg.photoshare.api.respones.ProfileUserResponse;
import com.hg.photoshare.bean.ProfileUserBean;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import vn.app.base.api.volley.callback.SimpleRequestCallBack;
import vn.app.base.api.volley.core.UploadBinaryApiRequest;
import vn.app.base.constant.APIConstant;
import vn.app.base.constant.ApiParam;
import vn.app.base.util.SharedPrefUtils;

/**
 * Created by GMORUNSYSTEM on 11/29/2016.
 */

public class UpdateProfileRequest extends UploadBinaryApiRequest<ProfileUserBean> {
    private SimpleRequestCallBack mSimpleRequestCallBack;

    public UpdateProfileRequest(File file, SimpleRequestCallBack mSimpleRequestCallBack) {
        Map<String, File> fileMap = new HashMap<>();
        fileMap.put("avatar", file);
        setRequestFiles(fileMap);
        this.mSimpleRequestCallBack = mSimpleRequestCallBack;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.REQUEST_URL_UPDATE_PROFILE;
    }

    @Override
    public boolean isRequiredAccessToken() {
        return false;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> header = new HashMap<>();
        header.put(ApiParam.TOKEN, SharedPrefUtils.getAccessToken());
        return header;
    }

    @Override
    public Class<ProfileUserBean> getResponseClass() {
        return ProfileUserBean.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public void onRequestSuccess(ProfileUserBean response) {

    }

    @Override
    public void onRequestError(VolleyError error) {

    }
}
