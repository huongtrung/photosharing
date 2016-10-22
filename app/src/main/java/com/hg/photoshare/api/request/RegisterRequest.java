package com.hg.photoshare.api.request;

import android.graphics.Bitmap;

import com.android.volley.Request;
import com.hg.photoshare.api.respones.RegisterResponse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import vn.app.base.api.volley.core.ObjectApiRequest;
import vn.app.base.constant.APIConstant;

/**
 * Created by Nart on 21/10/2016.
 */
public class RegisterRequest extends ObjectApiRequest<RegisterResponse> {
    String username;
    String email;
    String password;
    Bitmap avatar;

    public RegisterRequest(String username, String email, String password, Bitmap avatar) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
    }

    @Override
    public boolean isRequiredAuthorization() {
        return false;
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
}
