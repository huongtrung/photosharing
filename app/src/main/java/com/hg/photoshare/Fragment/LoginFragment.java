package com.hg.photoshare.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.hg.photoshare.HomeActivity;
import com.hg.photoshare.MainActivity;
import com.hg.photoshare.R;
import com.hg.photoshare.api.request.LoginRequest;
import com.hg.photoshare.api.respones.LoginReponse;
import com.hg.photoshare.contants.Constant;
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

import static com.hg.photoshare.contants.Constant.KEY_IMAGE_USER;

/**
 * Created by on 10/14/2016.
 */

public class LoginFragment extends BaseFragment {

    @BindView(R.id.etLogin)
    EditText etLogin;

    @BindView(R.id.etPassword)
    EditText etPass;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.btnCreateAccount)
    Button btnCreate;

    private String userId;
    private String pass;

    LoginReponse loginReponse;

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
        replaceFragment(R.id.container, RegisterFragment.newInstance());
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
            SharedPrefUtils.putString(Constant.KEY_USER_NAME, loginReponse.data.username);
            SharedPrefUtils.putString(Constant.KEY_USER_ID, loginReponse.data._id);
            SharedPrefUtils.putString(Constant.KEY_IMAGE_USER, loginReponse.data.avatar);
            Log.e("user:", loginReponse.data._id);
            boolean isAgreeTutorial = SharedPrefUtils.getBoolean(ToturialFragment.KEY_AGREE, false);
            if (isAgreeTutorial) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            } else {
                replaceFragment(R.id.container, ToturialFragment.newInstance());

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
