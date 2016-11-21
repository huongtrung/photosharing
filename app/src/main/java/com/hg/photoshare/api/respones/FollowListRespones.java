package com.hg.photoshare.api.respones;

import com.hg.photoshare.bean.UserBean;

import java.util.List;

import vn.app.base.api.response.BaseResponse;
import vn.app.base.api.volley.core.BaseApiRequest;

/**
 * Created by GMORUNSYSTEM on 11/17/2016.
 */

public class FollowListRespones extends BaseResponse {
    public List<UserBean> data;
}
