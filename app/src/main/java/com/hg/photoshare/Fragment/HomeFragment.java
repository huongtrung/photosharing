package com.hg.photoshare.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Toast;

import com.hg.photoshare.R;
import com.hg.photoshare.adapter.HomeAdapter;
import com.hg.photoshare.api.request.HomeRequest;
import com.hg.photoshare.api.respones.HomeResponse;
import com.hg.photoshare.data.HomeData;
import com.hg.photoshare.fragment.HomeFragment;

import java.util.List;

import butterknife.BindView;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.vp_home)
    ViewPager vpHome;
    HomeAdapter homeAdapter;
    @BindView(R.id.tl_home)
    TabLayout tlHome;
    DrawerLayout drawerLayout;
    List<HomeData> homeData;
    HomeMenuFragment homeMenuFragment;
    private int type;
    private int num=10;

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
        drawerLayout = (DrawerLayout) root.findViewById(R.id.drawer_layout);
        homeAdapter = new HomeAdapter(getActivity().getSupportFragmentManager(), homeData);
        vpHome.setAdapter(homeAdapter);
        vpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
                type = position;
                if (homeData == null) {
                    getHomeData();
                } else {
                    handleHomeData(homeData);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tlHome.setupWithViewPager(vpHome);
        homeMenuFragment = (HomeMenuFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
//        homeMenuFragment.setUp(R.id.navigation_drawer, drawerLayout);
    }

    private void getHomeData() {
        HomeRequest homeRequest = new HomeRequest(type, num);
        homeRequest.setRequestCallBack(new ApiObjectCallBack<HomeResponse>() {
            @Override
            public void onSuccess(HomeResponse responses) {
                initialResponseHandled();
                handleHomeData(responses.data);
            }

            @Override
            public void onFail(int failCode, String message) {
                hideCoverNetworkLoading();
                DialogUtil.showOkBtnDialog(getContext(), "Error : " +failCode, message);
            }
        });
        homeRequest.execute();
    }

    private void handleHomeData(List<HomeData> getData) {
        HomeAdapter homeAdapter = new HomeAdapter(getChildFragmentManager(), getData);
        vpHome.setAdapter(homeAdapter);

    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    protected void initData() {
        if (homeData == null) {
            getHomeData();
        } else {
            handleHomeData(homeData);
        }
    }
}

