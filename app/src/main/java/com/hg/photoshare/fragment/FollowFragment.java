package com.hg.photoshare.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hg.photoshare.R;
import com.hg.photoshare.adapter.FollowListAdapter;
import com.hg.photoshare.api.request.FollowListRequest;
import com.hg.photoshare.api.respones.FollowListRespones;
import com.hg.photoshare.contants.Constant;
import com.hg.photoshare.inter.OnItemClickListener;

import butterknife.BindView;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.SharedPrefUtils;

import static android.R.id.message;

/**
 * Created by Nart on 26/10/2016.
 */
public class FollowFragment extends BaseFragment {

    @BindView(R.id.rc_follow)
    RecyclerView rcFollow;
    @BindView(R.id.swipe_follow)
    SwipeRefreshLayout swipeFollow;

    private FollowListAdapter mFollowListAdapter;
    private String mUserId;

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
        setUpToolBarView(true, "Follow", true, "", false);
        mUserId=SharedPrefUtils.getString(Constant.KEY_ID_NAME,"");
        mFollowListAdapter = new FollowListAdapter(getActivity(), new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String userID) {
                if (userID.equalsIgnoreCase(mUserId))
                    FragmentUtil.replaceFragment(getActivity(), ProfileFragment.newInstance(), null);
                else
                    FragmentUtil.replaceFragment(getActivity(), UserFragment.newInstance(userID), null);
            }
        });
        requestFollowList();
        swipeFollow.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestFollowList();
                hideCoverNetworkLoading();
            }
        });
        swipeFollow.setColorSchemeResources(android.R.color.holo_blue_bright);
    }

    private void requestFollowList() {
        showCoverNetworkLoading();
        FollowListRequest followListRequest = new FollowListRequest();
        followListRequest.setRequestCallBack(new ApiObjectCallBack<FollowListRespones>() {
            @Override
            public void onSuccess(FollowListRespones respones) {
                if (swipeFollow.isRefreshing())
                    swipeFollow.setRefreshing(false);
                hideCoverNetworkLoading();
                if (respones != null)
                    setUpList(respones);
                else
                    DialogUtil.showOkBtnDialog(getContext(), "Error ", "No data");
            }

            @Override
            public void onFail(int failCode, String message) {
                hideCoverNetworkLoading();
                DialogUtil.showOkBtnDialog(getContext(), "Error " + failCode, message);
                if (swipeFollow.isRefreshing())
                    swipeFollow.setRefreshing(false);
            }
        });
        followListRequest.execute();
    }

    private void setUpList(FollowListRespones response) {
        mFollowListAdapter.setUserList(response.data);
        rcFollow.setAdapter(mFollowListAdapter);
        rcFollow.setLayoutManager(new LinearLayoutManager(getContext()));
        mFollowListAdapter.notifyDataSetChanged();
    }


    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    protected void initData() {

    }
}
