package com.hg.photoshare.api.request;

import com.android.volley.Request;
import com.hg.photoshare.api.respones.UploadImageResponse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import vn.app.base.api.volley.core.ObjectApiRequest;
import vn.app.base.constant.APIConstant;
import vn.app.base.constant.ApiParam;

/**
 * Created by Nart on 26/10/2016.
 */
public class UploadImageRequest extends ObjectApiRequest<UploadImageResponse> {
    File imageFile;

    public UploadImageRequest(File file) {
        file = imageFile;
    }

    @Override
    public boolean isRequiredAuthorization() {
        return true;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.REQUEST_URL_REGISTER;
    }

    @Override
    public boolean isRequiredAccessToken() {
        return false;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String,String> params =new HashMap<>();
        params.put(APIConstant.UPLOAD_IMAGE,APIConstant.IMAGE_BASE64_PREFIX+imageFile);
        return params;
    }

    @Override
    public Class<UploadImageResponse> getResponseClass() {
        return UploadImageResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
