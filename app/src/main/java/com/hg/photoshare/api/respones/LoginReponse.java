package com.hg.photoshare.api.respones;

import com.hg.photoshare.bean.ProfileBean;

import vn.app.base.api.response.BaseResponse;

/**
 * Created by Nart on 21/10/2016.
 */
public class LoginReponse extends BaseResponse {
    public String token;
    public ProfileBean profileBean;
}
