package com.hg.photoshare.api.request;

import com.android.volley.Request;
import com.hg.photoshare.api.respones.LoginReponse;

import java.util.HashMap;
import java.util.Map;

import vn.app.base.api.volley.core.ObjectApiRequest;
import vn.app.base.constant.APIConstant;

/**
 * Created by Nart on 21/10/2016.
 */
public class LoginRequest extends ObjectApiRequest<LoginReponse> {

    String userId;
    String pass;

    public LoginRequest(String userId, String pass) {
        this.userId = userId;
        this.pass = pass;
    }

    @Override
    public boolean isRequiredAuthorization() {
        return false;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.REQUEST_URL_LOGIN;
    }

    @Override
    public boolean isRequiredAccessToken() {
        return false;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String,String>params =new HashMap<>();
        params.put(APIConstant.FULLNAME,userId);
        params.put(APIConstant.PASSWORD,pass);
        return params;
    }

    @Override
    public Class<LoginReponse> getResponseClass() {
        return LoginReponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
