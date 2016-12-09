package com.hg.photoshare.api.request;

import com.android.volley.Request;
import com.hg.photoshare.api.respones.NearByRespones;

import java.util.HashMap;
import java.util.Map;

import vn.app.base.api.volley.core.ObjectApiRequest;
import vn.app.base.constant.APIConstant;
import vn.app.base.constant.ApiParam;
import vn.app.base.util.SharedPrefUtils;

/**
 * Created by GMORUNSYSTEM on 12/9/2016.
 */

public class NearByRequest extends ObjectApiRequest<NearByRespones> {

    private String currentLat;
    private String currentLong;

    public NearByRequest(String currentLong, String currentLat) {
        this.currentLong = currentLong;
        this.currentLat = currentLat;
    }

    @Override
    public boolean isRequiredAuthorization() {
        return false;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.REQUEST_URL_NEAR_BY;
    }

    @Override
    public boolean isRequiredAccessToken() {
        return false;
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        Map<String, String> header = new HashMap<>();
        header.put(ApiParam.TOKEN, SharedPrefUtils.getAccessToken());
        return header;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.LATITUDE,currentLong);
        params.put(APIConstant.LONGTITUDE,currentLat);
        return params;
    }

    @Override
    public Class<NearByRespones> getResponseClass() {
        return NearByRespones.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
