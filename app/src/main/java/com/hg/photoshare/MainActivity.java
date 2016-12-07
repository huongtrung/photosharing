package com.hg.photoshare;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import vn.app.base.activity.BaseActivity;
import vn.app.base.util.SharedPrefUtils;

import com.hg.photoshare.fragment.HomeMenuFragment;
import com.hg.photoshare.fragment.LoginFragment;

public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

