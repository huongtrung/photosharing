package com.hg.photoshare.fragment;

import android.os.Bundle;
import android.view.View;

import com.hg.photoshare.R;
import com.hg.photoshare.api.request.FollowListRequest;
import com.hg.photoshare.api.respones.FollowListRespones;

import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;

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
        setUpToolBarView(true,"Follow",true,"",false);
        requestFollowList();
    }

    private void requestFollowList(){
        FollowListRequest followListRequest =new FollowListRequest();
        followListRequest.setRequestCallBack(new ApiObjectCallBack<FollowListRespones>() {
            @Override
            public void onSuccess(FollowListRespones data) {
                hideCoverNetworkLoading();
                initialResponseHandled();
            }

            @Override
            public void onFail(int failCode, String message) {
                hideCoverNetworkLoading();
                DialogUtil.showOkBtnDialog(getContext(),"Error"+failCode,message);
            }
        });
    }


    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    protected void initData() {

    }
}
