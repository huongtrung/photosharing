package com.hg.photoshare.api.request;

import com.android.volley.Request;
import com.hg.photoshare.contants.Constant;

import java.util.HashMap;
import java.util.Map;

import vn.app.base.api.response.BaseResponse;
import vn.app.base.api.volley.core.ObjectApiRequest;
import vn.app.base.constant.APIConstant;
import vn.app.base.constant.ApiParam;
import vn.app.base.util.SharedPrefUtils;

/**
 * Created by Nart on 07/12/2016.
 */
public class DeleteImageRequest extends ObjectApiRequest<BaseResponse> {
    private String imageId;

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public boolean isRequiredAuthorization() {
        return false;
    }
    @Override
    public String getRequestURL() {
        return APIConstant.REQUEST_URL_DEL_IMAGE;
    }

    @Override
    public boolean isRequiredAccessToken() {
        return false;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.IMAGE_ID,imageId);
        return params;
    }
    @Override
    public Map<String, String> getRequestHeaders() {
        Map<String, String> header = new HashMap<>();
        header.put(ApiParam.TOKEN, SharedPrefUtils.getAccessToken());
        return header;
    }

    @Override
    public Class<BaseResponse> getResponseClass() {
        return BaseResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
