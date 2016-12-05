package com.hg.photoshare;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import vn.app.base.activity.BaseActivity;

import com.hg.photoshare.fragment.HomeMenuFragment;
import com.hg.photoshare.fragment.LoginFragment;

public class MainActivity extends BaseActivity {
    private Toolbar mToolbar;
    private HomeMenuFragment mHomeMenu;

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

//    private void setUpDrawer() {
//        mToolbar = (Toolbar) findViewById(R.id.tool_bar_home);
//        setSupportActionBar(mToolbar);
//        mHomeMenu = (HomeMenuFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
//        mHomeMenu.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
//    }

    @Override
    public void initData() {

    }

}

