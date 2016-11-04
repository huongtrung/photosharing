package com.hg.photoshare.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.hg.photoshare.R;
import com.hg.photoshare.api.request.LoginRequest;
import com.hg.photoshare.api.respones.LoginReponse;
import com.hg.photoshare.manage.UserManage;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.OnClick;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.KeyboardUtil;
import vn.app.base.util.SharedPrefUtils;
import vn.app.base.util.StringUtil;

/**
 * Created by on 10/14/2016.
 */

public class LoginFragment extends BaseFragment {

    private static final String KEY_USER_NAME = "KEY_USER_NAME";
    @BindView(R.id.etLogin)
    EditText etLogin;

    @BindView(R.id.etPassword)
    EditText etPass;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.btnCreateAccount)
    Button btnCreate;

    private String userId = "";
    private String pass = "";

    LoginReponse loginReponse;
    VolleyError volleyError;

    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView(View root) {

    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.btnCreateAccount)
    public void goToRegisterFragment() {
        FragmentUtil.replaceFragment(getActivity(), RegisterFragment.newInstance(), null);
//        FragmentUtil.pushFragment(getActivity(), ImageUploadFragment.newInstance(), null);
    }

    @OnClick(R.id.btnLogin)
    public void doLogin() {
        userId = etLogin.getText().toString().trim();
        pass = etPass.getText().toString().trim();
        if (!StringUtil.checkStringValid(userId) || !StringUtil.checkStringValid(pass)) {
            DialogUtil.showOkBtnDialog(getContext(), "Required fields", "Please re-enter again");
            return;
        }
        try {
            LoginRequest loginRequest = new LoginRequest(userId, SHA1(pass));
            loginRequest.setRequestCallBack(new ApiObjectCallBack<LoginReponse>() {
                @Override
                public void onSuccess(LoginReponse data) {
                    loginReponse = data;
                    handleLoginSuccess(loginReponse);
                    hideCoverNetworkLoading();
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideCoverNetworkLoading();
                    DialogUtil.showOkBtnDialog(getContext(), "Error : " + failCode, message);

                }
            });
            loginRequest.execute();
            KeyboardUtil.hideKeyboard(getActivity());
            showCoverNetworkLoading();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void handleLoginSuccess(LoginReponse loginReponse) {
        if (loginReponse.data != null) {
            UserManage.saveCurrentUser(loginReponse.data);
            SharedPrefUtils.saveAccessToken(loginReponse.data.token);
            SharedPrefUtils.putString(KEY_USER_NAME,loginReponse.data.username);
            Log.e("user:", loginReponse.data.token);
            Intent i;
            boolean isAgreeTutorial = SharedPrefUtils.getBoolean(ToturialFragment.KEY_AGREE, false);
            if (isAgreeTutorial) {
                FragmentUtil.replaceFragment(getActivity(), HomeFragment.newInstance(), null);
            }
            else {
                FragmentUtil.replaceFragment(getActivity(),ToturialFragment.newInstance(),null);

            }
        }
    }
}
