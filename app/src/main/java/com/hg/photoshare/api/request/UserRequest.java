package com.hg.photoshare.api.request;

import com.android.volley.Request;
import com.hg.photoshare.api.respones.ProfileUserResponse;

import java.util.HashMap;
import java.util.Map;

import vn.app.base.api.volley.core.ObjectApiRequest;
import vn.app.base.constant.APIConstant;
import vn.app.base.constant.ApiParam;
import vn.app.base.util.SharedPrefUtils;

/**
 * Created by GMORUNSYSTEM on 12/1/2016.
 */

public class UserRequest extends ObjectApiRequest<ProfileUserResponse> {
    private String userId;

    public UserRequest(String userId) {
        this.userId = userId;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.REQUEST_URL_PROFILE_USER;
    }

    @Override
    public boolean isRequiredAccessToken() {
        return false;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.USER_ID, userId);
        return params;
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        Map<String, String> header = new HashMap<>();
        header.put(ApiParam.TOKEN, SharedPrefUtils.getAccessToken());
        return header;
    }

    @Override
    public Class<ProfileUserResponse> getResponseClass() {
        return ProfileUserResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public boolean isRequiredAuthorization() {
        return false;
    }
}
