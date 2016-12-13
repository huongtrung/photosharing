package com.hg.photoshare.api.request;

import android.util.Log;

import com.android.volley.Request;
import com.hg.photoshare.api.respones.HomeResponse;

import java.util.HashMap;
import java.util.Map;

import vn.app.base.api.volley.core.BaseApiRequest;
import vn.app.base.api.volley.core.ObjectApiRequest;
import vn.app.base.constant.APIConstant;
import vn.app.base.constant.ApiParam;
import vn.app.base.util.SharedPrefUtils;

/**
 * Created by Nart on 25/10/2016.
 */
public class HomeRequest extends ObjectApiRequest<HomeResponse> {
    private int type;
    private int num;
    private long last_query_timestamp;

    public HomeRequest(int type, int num, long last_query_timestamp) {
        this.type = type;
        this.num = num;
        this.last_query_timestamp = last_query_timestamp;
    }

    public HomeRequest(int type) {
        this.type = type;
    }

    @Override
    public boolean isRequiredAuthorization() {
        return false;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.REQUEST_URL_HOME;
    }

    @Override
    public boolean isRequiredAccessToken() {
        return false;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.TYPE, String.valueOf(type));
        if (num != 0)
            params.put(APIConstant.NUM, String.valueOf(num));
        if (last_query_timestamp != 0)
            params.put(APIConstant.LAST_QUERY_TIMESTAMP, String.valueOf(last_query_timestamp));
        return params;
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        Map<String, String> header = new HashMap<>();
        header.put(ApiParam.TOKEN, SharedPrefUtils.getAccessToken());
        return header;
    }

    @Override
    public Class<HomeResponse> getResponseClass() {
        return HomeResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
