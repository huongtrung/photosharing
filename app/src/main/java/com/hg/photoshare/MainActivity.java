package com.hg.photoshare;



import butterknife.BindView;
import vn.app.base.activity.BaseActivity;
import vn.app.base.callback.FragmentListener;
import vn.app.base.util.ImagePickerUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.camera.CropImageIntentBuilder;
import com.hg.photoshare.fragment.LoginFragment;
import com.hg.photoshare.fragment.RegisterFragment;

public class MainActivity extends BaseActivity {

    protected FragmentListener fragmentListener;

    public void setFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }
    ImagePickerUtil imagePickerUtil = new ImagePickerUtil();
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
