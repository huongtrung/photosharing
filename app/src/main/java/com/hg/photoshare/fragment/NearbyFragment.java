package com.hg.photoshare.fragment;

import android.os.Bundle;
import android.view.View;

import com.hg.photoshare.R;

import vn.app.base.fragment.BaseFragment;

/**
 * Created by Nart on 26/10/2016.
 */
public class NearbyFragment extends BaseFragment {
    public static NearbyFragment newInstance() {
        NearbyFragment fragment = new NearbyFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_near_by;
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
