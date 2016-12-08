package com.hg.photoshare.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.hg.photoshare.R;
import com.hg.photoshare.adapter.HomeNewAdapter;
import com.hg.photoshare.api.request.FollowRequest;
import com.hg.photoshare.api.request.HomeRequest;
import com.hg.photoshare.api.respones.HomeResponse;
import com.hg.photoshare.bean.ImageBean;
import com.hg.photoshare.bean.UserBean;
import com.hg.photoshare.contants.Constant;
import com.hg.photoshare.data.HomeData;

import java.util.Locale;

import butterknife.BindView;
import vn.app.base.api.response.BaseResponse;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.SharedPrefUtils;

import static android.R.attr.data;
import static android.R.id.message;

/**
 * Created by Nart on 25/10/2016.
 */
public class HomeNewFragment extends BaseFragment {

    @BindView(R.id.rc_home_new)
    RecyclerView rcHomeNew;
    @BindView(R.id.swipe_home)
    SwipeRefreshLayout swipeHome;

    private HomeNewAdapter mHomeNewAdapter;
    private int typeIndex;
    private int num = 10;
    private String mUserId;

    public static HomeNewFragment newInstance(int type) {
        HomeNewFragment fragment = new HomeNewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.TYPE_INDEX_NEW, type);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_new;
    }

    @Override
    protected void initView(View root) {
        getHomeData();
        mUserId = SharedPrefUtils.getString(Constant.KEY_USER_ID, "");
        mHomeNewAdapter = new HomeNewAdapter(getContext());
        swipeHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHomeData();
                hideCoverNetworkLoading();
            }
        });
        swipeHome.setColorSchemeResources(android.R.color.holo_blue_bright);

        mHomeNewAdapter.setmOnItemClickListener(new HomeNewAdapter.OnItemClickListener() {

            @Override
            public void onItemAvatarClick(View view, String userId) {
                if (userId.equalsIgnoreCase(mUserId))
                    FragmentUtil.replaceFragment(getActivity(), ProfileFragment.newInstance(userId), null);
                else
                    FragmentUtil.replaceFragment(getActivity(), UserFragment.newInstance(userId), null);
            }

            @Override
            public void onItemNameClick(View view, String userId) {
                if (userId.equalsIgnoreCase(mUserId))
                    FragmentUtil.replaceFragment(getActivity(), ProfileFragment.newInstance(userId), null);
                else
                    FragmentUtil.replaceFragment(getActivity(), UserFragment.newInstance(userId), null);
            }

            @Override
            public void onItemPhotoClick(View view, ImageBean imageBean, UserBean userBean) {
                FragmentUtil.replaceFragment(getActivity(), ImageDetailFragment.newInstance(imageBean, userBean), null);
            }

            @Override
            public void OnItemLocationClick(View view, String lat, String longtitude) {
                Uri uri = Uri.parse(String.format(Locale.ENGLISH, "geo:%f,%f", Float.valueOf(lat), Float.valueOf(longtitude)));
                Log.e("lat", lat + "long" + longtitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }

        });
    }

    public void getHomeData() {
        showCoverNetworkLoading();
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
        rcHomeNew.setAdapter(mHomeNewAdapter);
        rcHomeNew.setLayoutManager(new LinearLayoutManager(getContext()));
        mHomeNewAdapter.notifyDataSetChanged();
    }


    @Override
    protected void getArgument(Bundle bundle) {
        typeIndex = bundle.getInt(Constant.TYPE_INDEX_NEW);
    }

    @Override
    protected void initData() {

    }

}
