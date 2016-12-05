package com.hg.photoshare.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hg.photoshare.R;
import com.hg.photoshare.adapter.HomeNewAdapter;
import com.hg.photoshare.api.request.HomeRequest;
import com.hg.photoshare.api.respones.HomeResponse;
import com.hg.photoshare.data.HomeData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;

import static android.R.attr.data;
import static android.R.attr.type;

/**
 * Created by Nart on 25/10/2016.
 */
public class HomeNewFragment extends BaseFragment {

    @BindView(R.id.rc_home_new)
    RecyclerView rcHomeNew;
    private HomeNewAdapter mHomeNewAdapter;
    private int type = 0;
    private int num = 10;
    private long last_timestamp;

    public static HomeNewFragment newInstance() {
        HomeNewFragment fragment = new HomeNewFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_new;
    }

    @Override
    protected void initView(View root) {
        mHomeNewAdapter = new HomeNewAdapter(getContext());
        getHomeData();
    }

    private void getHomeData() {
        showCoverNetworkLoading();
        HomeRequest homeRequest = new HomeRequest(type, num);
        homeRequest.setRequestCallBack(new ApiObjectCallBack<HomeResponse>() {
            @Override
            public void onSuccess(HomeResponse responses) {
                hideCoverNetworkLoading();
                if (responses.data != null && responses.data.size() > 0) {
                    handleHomeData(responses);
                } else
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

    private void handleHomeData(HomeResponse response) {
        mHomeNewAdapter.setHomeDataList(response.data);
        rcHomeNew.setAdapter(mHomeNewAdapter);
        rcHomeNew.setLayoutManager(new LinearLayoutManager(getContext()));
        mHomeNewAdapter.notifyDataSetChanged();
    }


    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    protected void initData() {

    }
}
