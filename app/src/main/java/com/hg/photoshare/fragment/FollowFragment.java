package com.hg.photoshare.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hg.photoshare.R;
import com.hg.photoshare.adapter.FollowListAdapter;
import com.hg.photoshare.api.request.FollowListRequest;
import com.hg.photoshare.api.respones.FollowListRespones;

import java.util.List;

import butterknife.BindView;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;

import static android.R.attr.data;

/**
 * Created by Nart on 26/10/2016.
 */
public class FollowFragment extends BaseFragment {

    @BindView(R.id.rc_follow)
    RecyclerView rcFollow;

    private FollowListAdapter mFollowListAdapter;

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
        mFollowListAdapter = new FollowListAdapter(getActivity());
        requestFollowList();
    }

    private void requestFollowList() {
        showCoverNetworkLoading();
        FollowListRequest followListRequest = new FollowListRequest();
        followListRequest.setRequestCallBack(new ApiObjectCallBack<FollowListRespones>() {
            @Override
            public void onSuccess(FollowListRespones respones) {
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
