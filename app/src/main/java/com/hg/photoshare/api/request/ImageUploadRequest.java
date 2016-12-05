package com.hg.photoshare.api.request;

import com.android.volley.Response;
import com.hg.photoshare.api.respones.ImageUploadRespones;
import java.io.File;
import java.util.Map;
import vn.app.base.api.volley.core.MultiPartRequest;

/**
 * Created by Nart on 28/10/2016.
 */
public class ImageUploadRequest extends MultiPartRequest<ImageUploadRespones> {
    public ImageUploadRequest(int method, String url, Response.ErrorListener listener, Class<ImageUploadRespones> mClass, Map<String, String> mHeader, Response.Listener<ImageUploadRespones> mListener, Map<String, String> mStringParts, Map<String, File> mFileParts) {
        super(method, url, listener, mClass, mHeader, mListener, mStringParts, mFileParts);
    }
}
