package com.hg.photoshare.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hg.photoshare.R;
import com.hg.photoshare.api.request.FavoriteRequest;
import com.hg.photoshare.api.request.FollowRequest;
import com.hg.photoshare.bean.ImageBean;
import com.hg.photoshare.bean.UserBean;
import com.hg.photoshare.contants.Constant;
import com.hg.photoshare.data.HomeData;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import vn.app.base.api.response.BaseResponse;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.SharedPrefUtils;
import vn.app.base.util.StringUtil;

/**
 * Created by Nart on 26/10/2016.
 */
public class ImageDetailFragment extends BaseFragment {
    @BindView(R.id.iv_account)
    ImageView ivUser;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.bt_follow)
    Button btFollow;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.iv_favorite)
    ImageView ivFavorite;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.tv_hash_tag)
    TextView tvHashTag;

    private ImageBean mImageBean;
    private UserBean mUserBean;
    private int isFollow;
    private int isFavorite;
    private String userId;
    private String imageId;
    private String latitude;
    private String longtitude;
    private String mUserId;

    public static ImageDetailFragment newInstance(ImageBean imageBean, UserBean userBean) {
        ImageDetailFragment fragment = new ImageDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.KEY_IMAGE_BEAN, imageBean);
        bundle.putParcelable(Constant.KEY_USER_BEAN, userBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_detail;
    }

    @Override
    protected void initView(View root) {
        mUserId = SharedPrefUtils.getString(Constant.KEY_USER_ID, "");
        if (userId.equalsIgnoreCase(mUserId))
            setUpToolBarView(true, "Image Detail", true, "Delete", true);
        else
            setUpToolBarView(true, "Image Detail", true, "", false);

    }

    @Override
    protected void getArgument(Bundle bundle) {
        mImageBean = bundle.getParcelable(Constant.KEY_IMAGE_BEAN);
        mUserBean = bundle.getParcelable(Constant.KEY_USER_BEAN);
        userId = mUserBean.id;
        imageId = mImageBean.id;
        latitude = mImageBean.lat;
        longtitude = mImageBean._long;
    }


    @Override
    protected void initData() {
        if (mUserBean.avatar != null && !mUserBean.avatar.isEmpty())
            Glide.with(getContext()).load(mUserBean.avatar).into(ivUser);
        else
            ivUser.setImageResource(R.drawable.placeholer_avatar);
        if (mImageBean.url != null && !mImageBean.url.isEmpty())
            Glide.with(getContext()).load(mImageBean.url).into(ivPhoto);
        else
            ivPhoto.setImageResource(R.drawable.placeholer_image_1600);

        StringUtil.displayText(mUserBean.username, tvUserName);
        if (mImageBean.isFavourite) {
            isFavorite = 0;
            ivFavorite.setImageResource(R.drawable.icon_favourite);
        } else {
            isFavorite = 1;
            ivFavorite.setImageResource(R.drawable.icon_no_favourite);
        }
        if (mUserBean.isFollowing) {
            isFollow = 0;
            btFollow.setText("Following");
            btFollow.setBackgroundResource(R.color.color_btn_follow_bg);
        } else {
            isFollow = 1;
            btFollow.setText("Follow");
            btFollow.setBackgroundResource(R.color.color_button);
        }
        if (mImageBean.location != null && !mImageBean.location.isEmpty())
            StringUtil.displayText(mImageBean.location, tvLocation);
        else
            tvLocation.setVisibility(View.GONE);
        StringUtil.displayText(mImageBean.caption, tvDescription);
        if (mImageBean.hashtag.size() > 0) {
            String result = "";
            for (String hashTag : mImageBean.hashtag) {
                result += "#" + hashTag + " ";
            }
            StringUtil.displayText(result, tvHashTag);
        }
    }

    @OnClick({R.id.title_nav_item_bar, R.id.bt_follow, R.id.iv_favorite, R.id.tv_location, R.id.ll_user})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_follow:
                FollowRequest followRequest = new FollowRequest(userId, isFollow);
                followRequest.setRequestCallBack(new ApiObjectCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        if (data != null && data.status == 1) {
                            if (isFollow == 1) {
                                isFollow = 0;
                                btFollow.setText("Following");
                                btFollow.setBackgroundResource(R.color.color_btn_follow_bg);
                                DialogUtil.showOkBtnDialog(getContext(), "Success", "Follow User Successfully !");
                            } else {
                                isFollow = 1;
                                btFollow.setText("Follow");
                                btFollow.setBackgroundResource(R.color.color_button);
                                DialogUtil.showOkBtnDialog(getContext(), "Success", " Unfollow User Successfully !");
                            }
                        } else
                            DialogUtil.showOkBtnDialog(getContext(), "Error", "Follow User Fail !");
                    }

                    @Override
                    public void onFail(int failCode, String message) {
                        DialogUtil.showOkBtnDialog(getContext(), "Error " + failCode, message);
                    }
                });
                followRequest.execute();
                break;
            case R.id.iv_favorite:
                FavoriteRequest favoriteRequest = new FavoriteRequest(imageId, isFavorite);
                favoriteRequest.setRequestCallBack(new ApiObjectCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        if (data != null && data.status == 1) {
                            if (isFavorite == 1) {
                                isFavorite = 0;
                                ivFavorite.setImageResource(R.drawable.icon_favourite);
                                DialogUtil.showOkBtnDialog(getContext(), "Success", "Favorite Image Successfully !");
                            } else {
                                isFavorite = 1;
                                ivFavorite.setImageResource(R.drawable.icon_no_favourite);
                                DialogUtil.showOkBtnDialog(getContext(), "Success", " UnFavorite Image Successfully !");
                            }
                        } else
                            DialogUtil.showOkBtnDialog(getContext(), "Error", "Favorite Image Fail !");
                    }

                    @Override
                    public void onFail(int failCode, String message) {
                        DialogUtil.showOkBtnDialog(getContext(), "Error " + failCode, message);
                    }
                });
                favoriteRequest.execute();
                break;
            case R.id.tv_location:
                Uri uri = Uri.parse(String.format(Locale.ENGLISH, "geo:%f,%f", Float.valueOf(latitude), Float.valueOf(longtitude)));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.ll_user:
                if (userId.equalsIgnoreCase(mUserId))
                    FragmentUtil.replaceFragment(getActivity(), ProfileFragment.newInstance(userId), null);
                else
                    FragmentUtil.replaceFragment(getActivity(), UserFragment.newInstance(userId), null);
        }
    }
}
