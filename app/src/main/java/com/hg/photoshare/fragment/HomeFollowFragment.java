package com.hg.photoshare.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hg.photoshare.R;
import com.hg.photoshare.adapter.HomeNewAdapter;
import com.hg.photoshare.api.request.HomeRequest;
import com.hg.photoshare.api.respones.HomeResponse;
import com.hg.photoshare.contants.Constant;

import butterknife.BindView;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;

import static android.R.attr.type;

/**
 * Created by Nart on 25/10/2016.
 */
public class HomeFollowFragment extends BaseFragment {
    @BindView(R.id.rc_home_follow)
    RecyclerView rcFollow;
    @BindView(R.id.swipe_home)
    SwipeRefreshLayout swipeHome;

    private int typeIndex = 1;
    private int num = 10;
    private HomeNewAdapter mHomeNewAdapter;

    public static HomeFollowFragment newInstance(int type) {
        HomeFollowFragment fragment = new HomeFollowFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.TYPE_INDEX_FOLLOW, type);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_follow;
    }

    @Override
    protected void initView(View root) {
        mHomeNewAdapter = new HomeNewAdapter(getContext());
        showCoverNetworkLoading();
        getHomeData();
        swipeHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHomeData();
            }
        });
        swipeHome.setColorSchemeResources(android.R.color.holo_blue_bright);
    }

    private void getHomeData() {
        HomeRequest homeRequest = new HomeRequest(typeIndex, num);
        homeRequest.setRequestCallBack(new ApiObjectCallBack<HomeResponse>() {
            @Override
            public void onSuccess(HomeResponse responses) {
                if (swipeHome.isRefreshing())
                    swipeHome.setRefreshing(false);
                hideCoverNetworkLoading();
                if (responses.data != null && responses.data.size() > 0) {
                    handleHomeData(responses);
                } else
                    DialogUtil.showOkBtnDialog(getContext(), "Error", "No data");
            }

            @Override
            public void onFail(int failCode, String message) {
                if (swipeHome.isRefreshing())
                    swipeHome.setRefreshing(false);
                hideCoverNetworkLoading();
                DialogUtil.showOkBtnDialog(getContext(), "Error : " + failCode, message);
            }
        });
        homeRequest.execute();
    }

    private void handleHomeData(HomeResponse response) {
        mHomeNewAdapter.setHomeDataList(response.data);
        rcFollow.setAdapter(mHomeNewAdapter);
        rcFollow.setLayoutManager(new LinearLayoutManager(getContext()));
        mHomeNewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void getArgument(Bundle bundle) {
        typeIndex = bundle.getInt(Constant.TYPE_INDEX_FOLLOW);
    }

    @Override
    protected void initData() {

    }
}
