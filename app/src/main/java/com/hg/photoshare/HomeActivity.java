package com.hg.photoshare;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.hg.photoshare.fragment.HomeFragment;
import com.hg.photoshare.fragment.HomeMenuFragment;
import com.hg.photoshare.fragment.LoginFragment;

import vn.app.base.activity.BaseActivity;

import static android.R.attr.drawable;
import static android.R.attr.fragment;

public class HomeActivity extends BaseActivity {
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
        return R.layout.activity_home;
    }

    @Override
    public void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_home);
        HomeFragment fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        setUpDrawer();
    }

    private void setUpDrawer() {
        setSupportActionBar(mToolbar);
        mHomeMenu = (HomeMenuFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mHomeMenu.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
    }

    @Override
    public void initData() {

    }
}
