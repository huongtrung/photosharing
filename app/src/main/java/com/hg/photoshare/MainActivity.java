package com.hg.photoshare;

import vn.app.base.activity.BaseActivity;

import com.hg.photoshare.fragment.LoginFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected String getNoConnectionMessage() {
        return null;
    }

    @Override
    protected String getErrorAPIMessage() {
        return null;
    }

    @Override
    protected String getTimeOutMessage() {
        return null;
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        LoginFragment fragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }

    @Override
    public void initData() {

    }

}

