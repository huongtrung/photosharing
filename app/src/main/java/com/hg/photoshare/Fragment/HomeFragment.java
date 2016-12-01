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
import com.hg.photoshare.api.request.HomeRequest;
import com.hg.photoshare.api.respones.HomeResponse;
import com.hg.photoshare.api.respones.ImageListResponse;
import com.hg.photoshare.data.HomeData;
import com.hg.photoshare.fragment.HomeFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.customview.endlessrecycler.EndlessRecyclerOnScrollListener;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;

import static android.R.id.message;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.vp_home)
    ViewPager vpHome;
    HomeAdapter homeAdapter;
    @BindView(R.id.tl_home)
    TabLayout tlHome;
    @BindView(R.id.fab_home)
    FloatingActionButton fab;

    List<HomeData> homeData;
    private int type;
    private int num = 10;
    private long last_timestamp;

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

        homeAdapter = new HomeAdapter(getActivity().getSupportFragmentManager(), homeData);
        vpHome.setAdapter(homeAdapter);
        vpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
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

    }

    private void getHomeData() {
        showCoverNetworkLoading();
        HomeRequest homeRequest = new HomeRequest(type, num);
        homeRequest.setRequestCallBack(new ApiObjectCallBack<HomeResponse>() {
            @Override
            public void onSuccess(HomeResponse responses) {
                hideCoverNetworkLoading();
                if (responses.data != null && responses.data.size() > 0){
                    handleHomeData(responses.data);
                }
                else
                    DialogUtil.showOkBtnDialog(getContext(), "Error", "No data");
            }

            @Override
            public void onFail(int failCode, String message) {
                hideCoverNetworkLoading();
                DialogUtil.showOkBtnDialog(getContext(), "Error : " + failCode, message);
            }
        });
        homeRequest.execute();
    }

    private void handleHomeData(List<HomeData> getData) {
        HomeAdapter homeAdapter = new HomeAdapter(getChildFragmentManager(), getData);
        vpHome.setAdapter(homeAdapter);

    }

    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore(int currentPage) {

        }
    };

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

    @OnClick(R.id.fab_home)
    public void goPost() {
        FragmentUtil.pushFragment(getActivity(), ImageUploadFragment.newInstance(), null);
    }

    private OnAcceptListHomeListener mOnAcceptListHome;

    public void setmOnAcceptListHome(OnAcceptListHomeListener mOnAcceptListHome) {
        this.mOnAcceptListHome = mOnAcceptListHome;
    }

    public interface OnAcceptListHomeListener {
        void onAccept(List<HomeData> homeDatas);
    }
}

