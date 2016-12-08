package com.hg.photoshare.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hg.photoshare.R;
import com.hg.photoshare.adapter.HomeAdapter;
import com.hg.photoshare.data.HomeData;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import vn.app.base.customview.endlessrecycler.EndlessRecyclerOnScrollListener;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.FragmentUtil;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.vp_home)
    ViewPager vpHome;
    HomeAdapter homeAdapter;
    @BindView(R.id.tl_home)
    TabLayout tlHome;
    @BindView(R.id.fab_home)
    FloatingActionButton fab;

    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View root) {
        homeAdapter = new HomeAdapter(getChildFragmentManager());
        vpHome.setAdapter(homeAdapter);
        tlHome.setupWithViewPager(vpHome);
    }
    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.fab_home)
    public void goPost() {
        FragmentUtil.replaceFragment(getActivity(), ImageUploadFragment.newInstance(), null);
    }
}

