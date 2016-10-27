package com.hg.photoshare.fragment;

import android.os.Bundle;
import android.view.View;

import com.hg.photoshare.R;

import vn.app.base.fragment.BaseFragment;

/**
 * Created by Nart on 25/10/2016.
 */
public class HomeNewFragment extends BaseFragment {

    public static HomeNewFragment newInstance() {
        HomeNewFragment fragment  = new HomeNewFragment();
        Bundle bundle=new Bundle();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_new;
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
}
