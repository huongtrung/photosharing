package com.hg.photoshare.api.request;

import com.bumptech.glide.request.Request;
import com.hg.photoshare.api.respones.ImageListResponse;

import java.util.HashMap;
import java.util.Map;

import vn.app.base.api.volley.core.ObjectApiRequest;
import vn.app.base.constant.APIConstant;
import vn.app.base.constant.ApiParam;
import vn.app.base.util.SharedPrefUtils;

import static com.hg.photoshare.contants.Constant.KEY_ID_NAME;

/**
 * Created by GMORUNSYSTEM on 12/5/2016.
 */

public class FavouriteListRequest extends ObjectApiRequest<ImageListResponse> {

    @Override
    public boolean isRequiredAuthorization() {
        return false;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.REQUEST_URL_FAVOURITE_LIST;
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
        header.put(ApiParam.TOKEN, SharedPrefUtils.getAccessToken());
        return header;
    }

    @Override
    public Class<ImageListResponse> getResponseClass() {
        return ImageListResponse.class;
    }

    @Override
    public int getMethod() {
        return com.android.volley.Request.Method.POST;
    }
}
