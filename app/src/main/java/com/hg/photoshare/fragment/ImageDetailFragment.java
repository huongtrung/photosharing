package com.hg.photoshare.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.Marker;
import com.hg.photoshare.R;
import com.hg.photoshare.adapter.CommentListAdapter;
import com.hg.photoshare.api.request.CommentListRequest;
import com.hg.photoshare.api.request.CommentRequest;
import com.hg.photoshare.api.request.DeleteImageRequest;
import com.hg.photoshare.api.request.FavoriteRequest;
import com.hg.photoshare.api.request.FollowRequest;
import com.hg.photoshare.api.respones.CommentListResponse;
import com.hg.photoshare.bean.ImageBean;
import com.hg.photoshare.bean.UserBean;
import com.hg.photoshare.contants.Constant;
import com.hg.photoshare.contants.ErrorCodeUlti;
import com.hg.photoshare.data.HomeData;
import com.hg.photoshare.manage.UserManage;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.app.base.api.response.BaseResponse;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.KeyboardUtil;
import vn.app.base.util.SharedPrefUtils;
import vn.app.base.util.StringUtil;

import static android.R.attr.data;
import static android.R.id.message;

/**
 * Created by Nart on 26/10/2016.
 */
public class ImageDetailFragment extends BaseFragment {
    @BindView(R.id.iv_account)
    CircleImageView ivUser;
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
    @BindView(R.id.ed_comment)
    EditText edComment;
    @BindView(R.id.bt_comment)
    ImageButton btComment;
    @BindView(R.id.rc_comment_list)
    RecyclerView rcCommentList;
    @BindView(R.id.swipe_comment_list)
    SwipeRefreshLayout swipeCommentList;

    private ImageBean mImageBean;
    private UserBean mUserBean;
    private int isFollow;
    private int isFavorite;
    private String userId;
    private String imageId;
    private String latitude;
    private String longtitude;
    private String mUserId;
    private String commentContent;
    private CommentListAdapter mCommentListAdapter;

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
        mCommentListAdapter = new CommentListAdapter(getContext());
        requestCommentList();

        swipeCommentList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestCommentList();
                hideCoverNetworkLoading();
            }
        });
        swipeCommentList.setColorSchemeResources(android.R.color.holo_blue_bright);
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
        if (!userId.equalsIgnoreCase(mUserId)) {
            if (mUserBean.isFollowing) {
                isFollow = 0;
                btFollow.setBackgroundResource(R.color.color_btn_follow_bg);
                btFollow.setText("Following");
            } else {
                isFollow = 1;
                btFollow.setBackgroundResource(R.color.color_button);
                btFollow.setText("Follow");
            }
        } else
            btFollow.setVisibility(View.GONE);
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

    @OnClick({R.id.title_nav_item_bar, R.id.bt_follow, R.id.iv_favorite, R.id.tv_location, R.id.ll_user, R.id.bt_comment})
    public void onClickItem(View v) {
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
                        DialogUtil.showOkBtnDialog(getContext(), "Error : " + failCode, ErrorCodeUlti.getErrorCode(failCode));
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
                        DialogUtil.showOkBtnDialog(getContext(), "Error : " + failCode, ErrorCodeUlti.getErrorCode(failCode));
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
                    replaceFragment(R.id.container, ProfileFragment.newInstance(userId));
                else
                    replaceFragment(R.id.container,UserFragment.newInstance(userId));
                break;
            case R.id.title_nav_item_bar:
                DialogUtil.showTwoBtnWithHandleDialog(getContext(), "Delete Image ?", "Are you sure you want to delete image ?", "Ok", "Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteImageRequest deleteImageRequest = new DeleteImageRequest();
                        deleteImageRequest.setImageId(imageId);
                        deleteImageRequest.setRequestCallBack(new ApiObjectCallBack<BaseResponse>() {
                            @Override
                            public void onSuccess(BaseResponse data) {
                                if (data != null && data.status == 1) {
                                    replaceFragment(R.id.container, HomeFragment.newInstance());
                                } else
                                    DialogUtil.showOkBtnDialog(getContext(), "Error", "Delete Image Fail");
                            }

                            @Override
                            public void onFail(int failCode, String message) {
                                DialogUtil.showOkBtnDialog(getContext(), "Error : " + failCode, ErrorCodeUlti.getErrorCode(failCode));
                            }
                        });
                        deleteImageRequest.execute();

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                break;
            case R.id.bt_comment:
                    showCoverNetworkLoading();
                    KeyboardUtil.hideKeyboard(getActivity());
                    commentContent = edComment.getText().toString().trim();
                    CommentRequest commentRequest = new CommentRequest(imageId, commentContent);
                    commentRequest.setRequestCallBack(new ApiObjectCallBack<BaseResponse>() {
                        @Override
                        public void onSuccess(BaseResponse data) {
                            hideCoverNetworkLoading();
                            if (data != null && data.status == 1) {
                                resetText();
                                requestCommentList();
                            } else
                                DialogUtil.showOkBtnDialog(getContext(), "Comment Fail", "Comment Again !");
                        }

                        @Override
                        public void onFail(int failCode, String message) {
                            hideCoverNetworkLoading();
                            DialogUtil.showOkBtnDialog(getContext(), "Error : " + failCode, ErrorCodeUlti.getErrorCode(failCode));

                        }
                    });
                    commentRequest.execute();
        }
    }

    private void requestCommentList() {
        showCoverNetworkLoading();
        CommentListRequest commentListRequest = new CommentListRequest(imageId);
        commentListRequest.setRequestCallBack(new ApiObjectCallBack<CommentListResponse>() {
            @Override
            public void onSuccess(CommentListResponse response) {
                hideCoverNetworkLoading();
                if (swipeCommentList.isRefreshing())
                    swipeCommentList.setRefreshing(false);
                if (response != null) {
                    mCommentListAdapter.setCommentListDatas(response.data);
                    rcCommentList.setAdapter(mCommentListAdapter);
                    rcCommentList.setLayoutManager(new LinearLayoutManager(getContext()));
                    mCommentListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(int failCode, String message) {
                hideCoverNetworkLoading();
                if (swipeCommentList.isRefreshing())
                    swipeCommentList.setRefreshing(false);
                DialogUtil.showOkBtnDialog(getContext(), "Error :" + failCode, message);
            }
        });
        commentListRequest.execute();
    }

    private void resetText() {
        commentContent = "";
        edComment.setText("");
    }
}
