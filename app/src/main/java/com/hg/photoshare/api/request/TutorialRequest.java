package com.hg.photoshare.api.request;

import com.android.volley.Request;
import com.hg.photoshare.api.respones.TutorialResponse;

import java.util.HashMap;
import java.util.Map;

import vn.app.base.api.volley.core.ObjectApiRequest;
import vn.app.base.constant.APIConstant;
import vn.app.base.constant.ApiParam;
import vn.app.base.util.SharedPrefUtils;

/**
 * Created by Nart on 24/10/2016.
 */
public class TutorialRequest extends ObjectApiRequest<TutorialResponse>{

    @Override
    public boolean isRequiredAuthorization() {
        return true;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.REQUEST_URL_TUTORIAL;
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
        Map<String, String> header = new HashMap<>();
        header.put(ApiParam.TOKEN,SharedPrefUtils.getAccessToken());
        return header;
    }

    @Override
    public Class<TutorialResponse> getResponseClass() {
        return TutorialResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
