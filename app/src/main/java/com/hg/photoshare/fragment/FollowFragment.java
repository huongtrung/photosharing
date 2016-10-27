package com.hg.photoshare.fragment;

import android.os.Bundle;
import android.view.View;

import com.hg.photoshare.R;

import vn.app.base.fragment.BaseFragment;

/**
 * Created by Nart on 26/10/2016.
 */
public class FollowFragment extends BaseFragment {
    public static FollowFragment newInstance() {
        FollowFragment fragment = new FollowFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_follow;
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
