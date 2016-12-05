package com.hg.photoshare.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hg.photoshare.R;
import com.hg.photoshare.adapter.FollowListAdapter;
import com.hg.photoshare.adapter.ImageListAdapter;
import com.hg.photoshare.api.request.FavouriteListRequest;
import com.hg.photoshare.api.respones.ImageListResponse;

import butterknife.BindView;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;

import static android.R.id.message;

/**
 * Created by Nart on 26/10/2016.
 */
public class FavouriteFragment extends BaseFragment {
    private ImageListAdapter mFavoriteAdapter;
    @BindView(R.id.rc_favourite)
    RecyclerView rcFavourite;

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
        setUpToolBarView(true, "Favorite", true, "", false);
        mFavoriteAdapter = new ImageListAdapter(getContext());
        getRequestFavorite();
    }

    private void getRequestFavorite() {
        showCoverNetworkLoading();
        FavouriteListRequest favouriteListRequest = new FavouriteListRequest();
        favouriteListRequest.setRequestCallBack(new ApiObjectCallBack<ImageListResponse>() {
            @Override
            public void onSuccess(ImageListResponse response) {
                if (response.data != null && response.data.size() > 0)
                    setUpList(response);
                else
                    DialogUtil.showOkBtnDialog(getContext(), "Error ", "No data");
            }

            @Override
            public void onFail(int failCode, String message) {
                hideCoverNetworkLoading();
                DialogUtil.showOkBtnDialog(getContext(), "Error " + failCode, message);
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
