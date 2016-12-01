package com.hg.photoshare.api.request;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hg.photoshare.api.respones.ImageUploadRespones;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.app.base.api.volley.callback.SimpleRequestCallBack;
import vn.app.base.api.volley.core.UploadBinaryApiRequest;
import vn.app.base.constant.APIConstant;
import vn.app.base.constant.ApiParam;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.SharedPrefUtils;

/**
 * Created by Nart on 28/10/2016.
 */
public class ImageUploadRequest extends UploadBinaryApiRequest<ImageUploadRespones> {
    public String caption;
    public String location;
    public double latitude;
    public double longtitude;
    public String hashtag;
    private SimpleRequestCallBack simpleRequestCallBack;

    public ImageUploadRequest(File file, String caption, String location, double latitude, double longtitude, String hashtag, SimpleRequestCallBack simpleRequestCallBack) {
        Map<String, File> fileMap = new HashMap<>();
        fileMap.put("image", file);
        setRequestFiles(fileMap);
        this.caption = caption;
        this.location = location;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.hashtag = hashtag;
        this.simpleRequestCallBack = simpleRequestCallBack;

    }

    @Override
    public String getRequestURL() {
        return APIConstant.REQUEST_URL_IMAGE_UPLOAD;
    }

    @Override
    public boolean isRequiredAccessToken() {
        return true;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiParam.TOKEN, SharedPrefUtils.getAccessToken());
        params.put(APIConstant.CAPTION, caption);
        params.put(APIConstant.LOCATION, location);
        params.put(APIConstant.LATITUDE, String.valueOf(latitude));
        params.put(APIConstant.LONGTITUDE, String.valueOf(longtitude));
        params.put(APIConstant.HASHTAG, hashtag);
        return params;
    }

    @Override
    public Class<ImageUploadRespones> getResponseClass() {
        return ImageUploadRespones.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public void onRequestSuccess(ImageUploadRespones response) {
//        simpleRequestCallBack.onResponse(response.status,response.message)
    }

    @Override
    public void onRequestError(VolleyError error) {

    }
}
