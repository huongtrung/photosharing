package com.hg.photoshare.api.request;

import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.hg.photoshare.api.respones.RegisterResponse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import vn.app.base.api.volley.callback.SimpleRequestCallBack;
import vn.app.base.api.volley.core.ObjectApiRequest;
import vn.app.base.api.volley.core.UploadBinaryApiRequest;
import vn.app.base.constant.APIConstant;

/**
 * Created by Nart on 21/10/2016.
 */
public class RegisterRequest extends ObjectApiRequest<RegisterResponse> {
    String username;
    String email;
    String password;

    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
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
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.FULLNAME, username);
        params.put(APIConstant.EMAIL, email);
        params.put(APIConstant.PASSWORD, password);
        return params;
    }

    @Override
    public Class<RegisterResponse> getResponseClass() {
        return RegisterResponse.class;
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
