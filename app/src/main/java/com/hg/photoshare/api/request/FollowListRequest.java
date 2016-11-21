package com.hg.photoshare.api.request;

import com.hg.photoshare.api.respones.FollowListRespones;

import java.util.HashMap;
import java.util.Map;
import com.android.volley.Request;
import vn.app.base.api.volley.core.BaseApiRequest;
import vn.app.base.api.volley.core.ObjectApiRequest;
import vn.app.base.constant.APIConstant;
import vn.app.base.constant.ApiParam;
import vn.app.base.util.SharedPrefUtils;

/**
 * Created by GMORUNSYSTEM on 11/17/2016.
 */

public class FollowListRequest extends ObjectApiRequest<FollowListRespones>{
    @Override
    public boolean isRequiredAuthorization() {
        return false;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.REQUEST_URL_FOLLOW_LIST;
    }

    @Override
    public boolean isRequiredAccessToken() {
        return false;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        Map<String,String> header=new HashMap<>();
        header.put(ApiParam.TOKEN, SharedPrefUtils.getAccessToken());
        return header;
    }

    @Override
    public Class<FollowListRespones> getResponseClass() {
        return FollowListRespones.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
