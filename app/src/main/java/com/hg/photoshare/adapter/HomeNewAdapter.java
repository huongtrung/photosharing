package com.hg.photoshare.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hg.photoshare.data.ImageListData;
import com.hg.photoshare.fragment.HomeMenuFragment;
import com.hg.photoshare.fragment.HomeNewFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vn.app.base.api.response.BaseResponse;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.imageloader.ImageLoader;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.SharedPrefUtils;
import vn.app.base.util.StringUtil;

import static android.R.attr.data;
import static android.R.id.message;
import static android.media.CamcorderProfile.get;

/**
 * Created by Nart on 27/10/2016.
 */
public class HomeNewAdapter extends RecyclerView.Adapter<HomeNewAdapter.ViewHolder> {
    Context mContext;
    LayoutInflater inflater;
    List<HomeData> homeDataList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private String userId;
    private int isFollow;
    private int isFavorite;
    private String imageId;
    private String mUserId;

    public HomeNewAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
        notifyDataSetChanged();

    }

    public void setHomeDataList(List<HomeData> homeDataList) {
        this.homeDataList = homeDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final HomeData homeData = homeDataList.get(position);
        mUserId = SharedPrefUtils.getString(Constant.KEY_USER_ID, "");
        userId = homeDataList.get(position).user.id;
        imageId = homeDataList.get(position).image.id;

        if (homeData.user.avatar != null && !homeData.user.avatar.isEmpty())
            Glide.with(mContext).load(homeData.user.avatar).into(holder.ivAccount);
        else
            holder.ivAccount.setImageResource(R.drawable.placeholer_avatar);
        if (homeData.image.url != null && !homeData.image.url.isEmpty())
            Glide.with(mContext).load(homeData.image.url).into(holder.ivPhoto);
        else
            holder.ivPhoto.setImageResource(R.drawable.placeholer_image_1600);

        StringUtil.displayText(homeData.user.username, holder.tvNameAccount);
        if (!userId.equalsIgnoreCase(mUserId)) {
            if (homeData.image.isFavourite) {
                isFavorite = 0;
                holder.ivFavorite.setImageResource(R.drawable.icon_favourite);
            } else {
                isFavorite = 1;
                holder.ivFavorite.setImageResource(R.drawable.icon_no_favourite);
            }
        } else
            holder.ivFavorite.setVisibility(View.GONE);
        if (!userId.equalsIgnoreCase(mUserId)) {
            if (homeData.user.isFollowing) {
                isFollow = 0;
                holder.btFollow.setBackgroundResource(R.color.color_btn_follow_bg);
                holder.btFollow.setText("Following");
            } else {
                isFollow = 1;
                holder.btFollow.setBackgroundResource(R.color.color_button);
                holder.btFollow.setText("Follow");
            }
        } else
            holder.btFollow.setVisibility(View.GONE);
        if (homeData.image.location != null && !homeData.image.location.isEmpty())
            StringUtil.displayText(homeData.image.location, holder.tvLocation);
        else
            holder.tvLocation.setVisibility(View.GONE);
        StringUtil.displayText(homeData.image.caption, holder.tvCaption);
        if (homeData.image.hashtag.size() > 0) {
            String result = "";
            for (String hashTag : homeData.image.hashtag) {
                result += "#" + hashTag + " ";
            }
            StringUtil.displayText(result, holder.tvHashTag);
        }
        holder.ivAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemAvatarClick(v, homeDataList.get(position).user.id);
            }
        });
        holder.tvNameAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemNameClick(v, homeDataList.get(position).user.id);
            }
        });
        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemPhotoClick(v, homeData.image, homeData.user);
            }
        });
        holder.btFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowRequest followRequest = new FollowRequest(userId, isFollow);
                followRequest.setRequestCallBack(new ApiObjectCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        if (data != null && data.status == 1) {
                            if (isFollow == 1) {
                                isFollow = 0;
                                holder.btFollow.setText("Following");
                                holder.btFollow.setBackgroundResource(R.color.color_btn_follow_bg);
                                DialogUtil.showOkBtnDialog(mContext, "Success", "Follow User Successfully !");
                            } else {
                                isFollow = 1;
                                holder.btFollow.setText("Follow");
                                holder.btFollow.setBackgroundResource(R.color.color_button);
                                DialogUtil.showOkBtnDialog(mContext, "Success", " Unfollow User Successfully !");
                            }
                        } else
                            DialogUtil.showOkBtnDialog(mContext, "Error", "Follow User Fail !");
                    }

                    @Override
                    public void onFail(int failCode, String message) {
                        DialogUtil.showOkBtnDialog(mContext, "Error " + failCode, message);
                    }
                });
                followRequest.execute();
            }
        });
        holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteRequest favoriteRequest = new FavoriteRequest(imageId, isFavorite);
                favoriteRequest.setRequestCallBack(new ApiObjectCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        if (data != null && data.status == 1) {
                            if (isFavorite == 1) {
                                isFavorite = 0;
                                holder.ivFavorite.setImageResource(R.drawable.icon_favourite);
                                DialogUtil.showOkBtnDialog(mContext, "Success", "Favorite Image Successfully !");
                            } else {
                                isFavorite = 1;
                                holder.ivFavorite.setImageResource(R.drawable.icon_no_favourite);
                                DialogUtil.showOkBtnDialog(mContext, "Success", " UnFavorite Image Successfully !");
                            }
                        } else
                            DialogUtil.showOkBtnDialog(mContext, "Error", "Favorite Image Fail !");
                    }

                    @Override
                    public void onFail(int failCode, String message) {
                        DialogUtil.showOkBtnDialog(mContext, "Error " + failCode, message);
                    }
                });
                favoriteRequest.execute();
            }
        });
        holder.tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.OnItemLocationClick(v, homeData.image.lat, homeData.image._long);
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAccount;
        TextView tvNameAccount;
        Button btFollow;
        ImageView ivPhoto;
        TextView tvLocation;
        TextView tvCaption;
        TextView tvHashTag;
        ImageView ivFavorite;

        public ViewHolder(View itemView) {
            super(itemView);
            ivAccount = (ImageView) itemView.findViewById(R.id.iv_account);
            tvNameAccount = (TextView) itemView.findViewById(R.id.tv_name_account);
            btFollow = (Button) itemView.findViewById(R.id.bt_follow);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_address);
            tvCaption = (TextView) itemView.findViewById(R.id.tv_description);
            tvHashTag = (TextView) itemView.findViewById(R.id.tv_hash_tag);
            ivFavorite = (ImageView) itemView.findViewById(R.id.iv_favorite);
        }
    }


    public interface OnItemClickListener {
        void onItemAvatarClick(View view, String userId);

        void onItemNameClick(View view, String userId);

        void onItemPhotoClick(View view, ImageBean imageBean, UserBean userBean);

        void OnItemLocationClick(View view, String lat, String longtitude);

    }

}
