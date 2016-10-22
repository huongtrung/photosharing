package com.hg.photoshare.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hg.photoshare.R;
import com.hg.photoshare.api.request.LoginRequest;
import com.hg.photoshare.api.respones.LoginReponse;

import butterknife.BindView;
import butterknife.OnClick;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.KeyboardUtil;
import vn.app.base.util.StringUtil;

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

    private String userId = "";
    private String pass = "";

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
        FragmentUtil.pushFragment(getActivity(), RegisterFragment.newInstance(), null);
    }

    @OnClick(R.id.btnLogin)
    public void doLogin() {
        checkLogin();
        LoginRequest loginRequest=new LoginRequest(userId,pass);
        loginRequest.setRequestCallBack(new ApiObjectCallBack<LoginReponse>() {
            @Override
            public void onSuccess(LoginReponse data) {
                loginReponse=data;
                handleLoginSuccess(loginReponse);
            }

            @Override
            public void onFail(int failCode, String message) {
                hideCoverNetworkLoading();
            }
        });
        loginRequest.execute();
        KeyboardUtil.hideKeyboard(getActivity());
        showCoverNetworkLoading();
    }

    private void handleLoginSuccess(LoginReponse loginReponse){
        if (loginReponse.profileBean!=null){
            Toast.makeText(getContext(),"aaaaaaaa",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkLogin() {
        userId = etLogin.getText().toString().trim();
        pass = etPass.getText().toString().trim();
        if (!StringUtil.checkStringValid(userId) || !StringUtil.checkStringValid(pass)) {
            DialogUtil.showOkBtnDialog(getContext(), "Required fields", "Please re-enter again");
            return;
        }
    }
}
