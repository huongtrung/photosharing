package com.hg.photoshare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hg.photoshare.R;
import com.hg.photoshare.api.request.FavoriteRequest;
import com.hg.photoshare.api.request.FollowRequest;
import com.hg.photoshare.contants.Constant;
import com.hg.photoshare.contants.ErrorCodeUlti;
import com.hg.photoshare.data.ImageListData;
import com.hg.photoshare.fragment.HomeFragment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.app.base.api.response.BaseResponse;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.SharedPrefUtils;
import vn.app.base.util.StringUtil;

/**
 * Created by GMORUNSYSTEM on 12/1/2016.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private List<ImageListData> imageListData;
    private HomeNewAdapter.OnItemClickListener mOnItemClickListener;
    private String userId;
    private String mUserId;
    private int isFollow;
    private String latitude;
    private String longtitude;
    private int isFavorite;
    private String imageId;

    public void setmOnItemClickListener(HomeNewAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public ImageListAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    public void setImageListData(List<ImageListData> imageListData) {
        if (imageListData == null)
            this.imageListData.clear();
        else
            this.imageListData = imageListData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_favorite, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mUserId = SharedPrefUtils.getString(Constant.KEY_USER_ID, "");

        final ImageListData imageList = imageListData.get(position);
        userId = imageListData.get(position).user.id;
        imageId = imageListData.get(position).image.id;
        latitude = imageListData.get(position).image.lat;
        longtitude = imageListData.get(position).image._long;
        if (imageList.user.avatar != null && !imageList.user.avatar.isEmpty())
            Glide.with(mContext).load(imageList.user.avatar).into(holder.ivAccount);
        else
            holder.ivAccount.setImageResource(R.drawable.placeholer_avatar);
        if (imageList.image.url != null && !imageList.image.url.isEmpty())
            Glide.with(mContext).load(imageList.image.url).into(holder.ivPhoto);
        else
            holder.ivPhoto.setImageResource(R.drawable.placeholer_image_1600);

        StringUtil.displayText(imageList.user.username, holder.tvName);

        if (imageList.image.isFavourite) {
            isFavorite = 0;
            holder.ivFavorite.setImageResource(R.drawable.icon_favourite);
        } else {
            isFavorite = 1;
            holder.ivFavorite.setImageResource(R.drawable.icon_no_favourite);
        }
        if (!userId.equalsIgnoreCase(mUserId))
            holder.btFollow.setVisibility(View.VISIBLE);
        else
            holder.btFollow.setVisibility(View.GONE);
        if (imageList.user.isFollowing) {
            isFollow = 0;
            holder.btFollow.setBackgroundResource(R.color.color_btn_follow_bg);
            holder.btFollow.setText("Following");
        } else {
            isFollow = 1;
            holder.btFollow.setBackgroundResource(R.color.color_button);
            holder.btFollow.setText("Follow");
        }
        if (imageList.image.location != null && !imageList.image.location.isEmpty())
            StringUtil.displayText(imageList.image.location, holder.tvLocation);
        else
            holder.tvLocation.setVisibility(View.GONE);
        StringUtil.displayText(imageList.image.caption, holder.tvDescription);
        if (imageList.image.hashtag.size() > 0) {
            String result = "";
            for (String hashTag : imageList.image.hashtag) {
                result += "#" + hashTag + " ";
            }
            StringUtil.displayText(result, holder.tvHashTag);
        }
        holder.ivAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemAvatarClick(v, imageListData.get(position).user.id);
            }
        });
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemNameClick(v, imageListData.get(position).user.id);
            }
        });
        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemPhotoClick(v, imageList.image, imageList.user);
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
                                holder.btFollow.setText(mContext.getString(R.string.title_bt_following));
                                holder.btFollow.setBackgroundResource(R.color.color_btn_follow_bg);
                                DialogUtil.showOkBtnDialog(mContext, mContext.getString(R.string.success), mContext.getString(R.string.follow_success));
                            } else {
                                isFollow = 1;
                                holder.btFollow.setText(mContext.getString(R.string.title_bt_follow));
                                holder.btFollow.setBackgroundResource(R.color.color_button);
                                DialogUtil.showOkBtnDialog(mContext, mContext.getString(R.string.success),mContext.getString(R.string.Unfollow_success));
                            }
                        } else
                            DialogUtil.showOkBtnDialog(mContext, mContext.getString(R.string.error), mContext.getString(R.string.follow_fail));
                    }

                    @Override
                    public void onFail(int failCode, String message) {
                        DialogUtil.showOkBtnDialog(mContext, "Error : " + failCode, ErrorCodeUlti.getErrorCode(failCode));
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
                        DialogUtil.showOkBtnDialog(mContext, "Error : " + failCode, ErrorCodeUlti.getErrorCode(failCode));
                    }
                });
                favoriteRequest.execute();
            }
        });
        holder.tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.OnItemLocationClick(v, imageList.image.lat, imageList.image._long);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAccount;
        private ImageView ivPhoto;
        private TextView tvLocation;
        private TextView tvDescription;
        private TextView tvHashTag;
        private ImageView ivFavorite;
        private TextView tvName;
        private Button btFollow;
        private LinearLayout llImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ivAccount = (CircleImageView) itemView.findViewById(R.id.iv_account);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_address);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            tvHashTag = (TextView) itemView.findViewById(R.id.tv_hash_tag);
            ivFavorite = (ImageView) itemView.findViewById(R.id.iv_favorite);
            tvName = (TextView) itemView.findViewById(R.id.tv_name_account);
            btFollow = (Button) itemView.findViewById(R.id.bt_follow);
            llImage = (LinearLayout) itemView.findViewById(R.id.ll_image);
        }
    }
}
