package com.hg.photoshare.manage;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hg.photoshare.bean.ProfileBean;
import com.hg.photoshare.bean.RegisterBean;
import com.hg.photoshare.contants.Constant;

import vn.app.base.constant.AppConstant;
import vn.app.base.util.SharedPrefUtils;
import vn.app.base.util.StringUtil;

/**
 * Created by Nart on 24/10/2016.
 */
public class UserManage {
    private static Gson gson = new Gson();

    public static final String USER_DATA = "USER_DATA";

    public static void saveCurrentUser(ProfileBean userProfileBean) {
        String userData = gson.toJson(userProfileBean, ProfileBean.class);
        SharedPrefUtils.putString(USER_DATA, userData);
    }
    public static ProfileBean getCurrentUser() {
        String userData = SharedPrefUtils.getString(USER_DATA, null);
        if (StringUtil.checkStringValid(userData)) {
            try {
                return gson.fromJson(userData, ProfileBean.class);
            } catch (JsonSyntaxException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static void clearUserData() {
        SharedPrefUtils.removeKey(USER_DATA);
        SharedPrefUtils.removeKey(AppConstant.ACCESS_TOKEN);
        SharedPrefUtils.removeKey(Constant.KEY_USER_NAME);
        SharedPrefUtils.removeKey(Constant.KEY_IMAGE_USER);
    }
}
