package com.hg.photoshare.fragment;

import android.os.Bundle;
import android.view.View;

import com.hg.photoshare.R;

import vn.app.base.fragment.BaseFragment;

/**
 * Created by Nart on 26/10/2016.
 */
public class UserFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    protected void initView(View root) {
        setUpToolBarView(true,"User",true,"",false);
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    protected void initData() {

    }
}
