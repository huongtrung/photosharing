package com.hg.photoshare.api.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hg.photoshare.api.respones.ProfileUserResponse;
import com.hg.photoshare.bean.ProfileUserBean;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import vn.app.base.api.volley.callback.SimpleRequestCallBack;
import vn.app.base.api.volley.core.MultiPartRequest;
import vn.app.base.api.volley.core.UploadBinaryApiRequest;
import vn.app.base.constant.APIConstant;
import vn.app.base.constant.ApiParam;
import vn.app.base.util.SharedPrefUtils;

/**
 * Created by GMORUNSYSTEM on 11/29/2016.
 */

public class UpdateProfileRequest extends MultiPartRequest<ProfileUserResponse> {

    public UpdateProfileRequest(int method, String url, Response.ErrorListener listener, Class<ProfileUserResponse> mClass, Map<String, String> mHeader, Response.Listener<ProfileUserResponse> mListener, Map<String, String> mStringParts, Map<String, File> mFileParts) {
        super(method, url, listener, mClass, mHeader, mListener, mStringParts, mFileParts);
    }
}
