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
import com.hg.photoshare.adapter.FollowListAdapter;
import com.hg.photoshare.adapter.HomeNewAdapter;
import com.hg.photoshare.adapter.ImageListAdapter;
import com.hg.photoshare.api.request.FavouriteListRequest;
import com.hg.photoshare.api.respones.ImageListResponse;
import com.hg.photoshare.bean.ImageBean;
import com.hg.photoshare.bean.UserBean;
import com.hg.photoshare.contants.Constant;
import com.hg.photoshare.contants.ErrorCodeUlti;

import java.util.Locale;

import butterknife.BindView;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.customview.endlessrecycler.EndlessRecyclerOnScrollListener;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.SharedPrefUtils;

import static android.R.id.message;

/**
 * Created by Nart on 26/10/2016.
 */
public class FavouriteFragment extends BaseFragment {
    private ImageListAdapter mFavoriteAdapter;
    @BindView(R.id.rc_favourite)
    RecyclerView rcFavourite;
    @BindView(R.id.swipe_favourite)
    SwipeRefreshLayout swipeFavorite;
    private String mUserId;
    private int mCurrentPage = 10;
    private static final int DEFAULT_PAGER = 10;

    public static FavouriteFragment newInstance() {
        FavouriteFragment fragment = new FavouriteFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_favourite;
    }

    @Override
    protected void initView(View root) {
        setUpToolBarView(true, getString(R.string.title_favorite), true, "", false);
        mUserId = SharedPrefUtils.getString(Constant.KEY_USER_ID, "");
        mFavoriteAdapter = new ImageListAdapter(getContext());

        getRequestFavorite();
        swipeFavorite.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRequestFavorite();
            }
        });
        swipeFavorite.setColorSchemeResources(android.R.color.holo_blue_bright);
        mFavoriteAdapter.setmOnItemClickListener(new HomeNewAdapter.OnItemClickListener() {

            @Override
            public void onItemAvatarClick(View view, String userId) {
                if (userId.equalsIgnoreCase(mUserId))
                    replaceFragment(R.id.container, ProfileFragment.newInstance(userId));
                else
                    replaceFragment(R.id.container, UserFragment.newInstance(userId));
            }

            @Override
            public void onItemNameClick(View view, String userId) {
                if (userId.equalsIgnoreCase(mUserId))
                    replaceFragment(R.id.container, ProfileFragment.newInstance(userId));
                else
                    replaceFragment(R.id.container, UserFragment.newInstance(userId));
            }

            @Override
            public void onItemPhotoClick(View view, ImageBean imageBean, UserBean userBean) {
                replaceFragment(R.id.container, ImageDetailFragment.newInstance(imageBean, userBean));
            }

            @Override
            public void OnItemLocationClick(View view, String lat, String longtitude) {
                Uri uri = Uri.parse(String.format(Locale.ENGLISH, "geo:%f,%f", Float.valueOf(lat), Float.valueOf(longtitude)));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void getRequestFavorite() {
        showCoverNetworkLoading();
        FavouriteListRequest favouriteListRequest = new FavouriteListRequest();
        favouriteListRequest.setRequestCallBack(new ApiObjectCallBack<ImageListResponse>() {
            @Override
            public void onSuccess(ImageListResponse response) {
                if (swipeFavorite.isRefreshing())
                    swipeFavorite.setRefreshing(false);
                hideCoverNetworkLoading();
                if (response.data != null && response.data.size() > 0)
                    setUpList(response);
                else
                    DialogUtil.showOkBtnDialog(getContext(), "Error ", "No data");
            }

            @Override
            public void onFail(int failCode, String message) {
                hideCoverNetworkLoading();
                if (swipeFavorite.isRefreshing())
                    swipeFavorite.setRefreshing(false);
                DialogUtil.showOkBtnDialog(getContext(), "Error : " + failCode, ErrorCodeUlti.getErrorCode(failCode));
            }
        });
        favouriteListRequest.execute();
    }

    private void setUpList(ImageListResponse response) {
        mFavoriteAdapter.setImageListData(response.data);
        rcFavourite.setAdapter(mFavoriteAdapter);
        rcFavourite.setLayoutManager(new LinearLayoutManager(getContext()));
        mFavoriteAdapter.notifyDataSetChanged();
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    protected void initData() {

    }
}
