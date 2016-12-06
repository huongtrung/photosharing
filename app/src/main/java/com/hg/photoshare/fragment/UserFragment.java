package com.hg.photoshare.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.hg.photoshare.R;
import com.hg.photoshare.adapter.ImageListAdapter;
import com.hg.photoshare.api.request.ImageListRequest;
import com.hg.photoshare.api.request.UserRequest;
import com.hg.photoshare.api.respones.ImageListResponse;
import com.hg.photoshare.api.respones.ProfileUserResponse;
import com.hg.photoshare.contants.Constant;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.app.base.adapter.DividerItemDecoration;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.StringUtil;

/**
 * Created by GMORUNSYSTEM on 12/6/2016.
 */

public class UserFragment extends BaseFragment {
    @BindView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.bt_change_avatar)
    ImageButton btChangeAvatar;
    @BindView(R.id.tv_name_account)
    TextView tvNameAccount;
    @BindView(R.id.tv_count_follow)
    TextView tvCountFollow;
    @BindView(R.id.tv_count_follower)
    TextView tvCountFollower;
    @BindView(R.id.tv_count_post)
    TextView tvCountPost;
    @BindView(R.id.rc_profile)
    RecyclerView rcProfile;
    @BindView(R.id.fab_profile)
    FloatingActionButton fabProfile;
    @BindView(R.id.swipe_profile)
    SwipeRefreshLayout swipeRefreshProfile;

    private String userId;
    private ImageListAdapter mImageListAdapter;

    public static UserFragment newInstance(String userId) {
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initView(View root) {
        mImageListAdapter = new ImageListAdapter(getContext());

        setUpToolBarView(true, "User", true, "", false);

        fabProfile.setVisibility(View.GONE);
        btChangeAvatar.setVisibility(View.GONE);

        swipeRefreshProfile.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                hideCoverNetworkLoading();
                getRequestImageList();
            }
        });
        swipeRefreshProfile.setColorSchemeResources(android.R.color.holo_blue_bright);
        requestGetProfile();
        getRequestImageList();
    }

    private void requestGetProfile() {
        showCoverNetworkLoading();
            UserRequest userRequest = new UserRequest(userId);
            userRequest.setRequestCallBack(new ApiObjectCallBack<ProfileUserResponse>() {
                @Override
                public void onSuccess(ProfileUserResponse data) {
                    hideCoverNetworkLoading();
                    if (data != null)
                        showData(data);
                    else
                        DialogUtil.showOkBtnDialog(getContext(), "Error", "No data");
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideCoverNetworkLoading();
                    DialogUtil.showOkBtnDialog(getContext(), "Error " + failCode, message);
                }
            });
            userRequest.execute();
        }

    private void showData(ProfileUserResponse response) {
        userId = response.data.id;
        if (response.data.avatar != null && !response.data.avatar.isEmpty())
            Glide.with(getContext()).load(response.data.avatar).into(civAvatar);
        else
            civAvatar.setImageResource(R.drawable.placeholer_avatar);
        StringUtil.displayText(response.data.username, tvNameAccount);
        StringUtil.displayText(response.data.follow.toString(), tvCountFollow);
        StringUtil.displayText(response.data.follower.toString(), tvCountFollower);
        StringUtil.displayText(response.data.post.toString(), tvCountPost);
    }

    private void getRequestImageList() {
        ImageListRequest imageListRequest = new ImageListRequest(userId);
        imageListRequest.setRequestCallBack(new ApiObjectCallBack<ImageListResponse>() {
            @Override
            public void onSuccess(ImageListResponse response) {
                if (response != null) {
                    setUpList(response);
                    if (swipeRefreshProfile.isRefreshing())
                        swipeRefreshProfile.setRefreshing(false);
                } else {
                    DialogUtil.showOkBtnDialog(getContext(), "Error", "No data");
                    if (swipeRefreshProfile.isRefreshing())
                        swipeRefreshProfile.setRefreshing(false);
                }
            }

            @Override
            public void onFail(int failCode, String message) {
                if (swipeRefreshProfile.isRefreshing())
                    swipeRefreshProfile.setRefreshing(false);
                DialogUtil.showOkBtnDialog(getContext(), "Error", message);
            }
        });
        imageListRequest.execute();
    }

    private void setUpList(ImageListResponse response) {
        mImageListAdapter.setImageListData(response.data);
        rcProfile.setAdapter(mImageListAdapter);
        rcProfile.setLayoutManager(new LinearLayoutManager(getContext()));
        rcProfile.addItemDecoration(new DividerItemDecoration(getContext(), null));
        mImageListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void getArgument(Bundle bundle) {
        userId = bundle.getString(Constant.USER_ID);
    }

    @Override
    protected void initData() {

    }
}
