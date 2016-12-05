package com.hg.photoshare.api.request;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.hg.photoshare.api.respones.ImageListResponse;
import com.hg.photoshare.contants.Constant;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import vn.app.base.api.volley.core.BaseApiRequest;
import vn.app.base.api.volley.core.ObjectApiRequest;
import vn.app.base.constant.APIConstant;
import vn.app.base.constant.ApiParam;
import vn.app.base.util.SharedPrefUtils;

import static vn.app.base.constant.APIConstant.REQUEST_URL_IMAGE_LIST;

/**
 * Created by GMORUNSYSTEM on 12/1/2016.
 */


public class ImageListRequest extends ObjectApiRequest<ImageListResponse> {

    private String userName;
    private String userId;

    public ImageListRequest(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    @Override
    public boolean isRequiredAuthorization() {
        return false;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.REQUEST_URL_IMAGE_LIST;
    }

    @Override
    public boolean isRequiredAccessToken() {
        return false;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        if (SharedPrefUtils.getString(Constant.KEY_USER_NAME, "").equalsIgnoreCase(userName))
            return null;
        else
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
    public Class<ImageListResponse> getResponseClass() {
        return ImageListResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
