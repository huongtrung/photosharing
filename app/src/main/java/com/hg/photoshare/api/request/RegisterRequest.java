package com.hg.photoshare.api.request;

import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hg.photoshare.api.respones.RegisterResponse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import vn.app.base.api.volley.callback.SimpleRequestCallBack;
import vn.app.base.api.volley.core.MultiPartRequest;
import vn.app.base.api.volley.core.ObjectApiRequest;
import vn.app.base.api.volley.core.UploadBinaryApiRequest;
import vn.app.base.constant.APIConstant;

/**
 * Created by Nart on 21/10/2016.
 */
public class RegisterRequest extends MultiPartRequest<RegisterResponse> {

    public RegisterRequest(int method, String url, Response.ErrorListener listener, Class<RegisterResponse> mClass, Map<String, String> mHeader, Response.Listener<RegisterResponse> mListener, Map<String, String> mStringParts, Map<String, File> mFileParts) {
        super(method, url, listener, mClass, mHeader, mListener, mStringParts, mFileParts);
    }
}
