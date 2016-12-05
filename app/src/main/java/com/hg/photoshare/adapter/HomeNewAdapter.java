package com.hg.photoshare.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hg.photoshare.R;
import com.hg.photoshare.bean.ImageBean;
import com.hg.photoshare.data.HomeData;
import com.hg.photoshare.data.ImageListData;

import java.util.ArrayList;
import java.util.List;

import vn.app.base.imageloader.ImageLoader;
import vn.app.base.util.StringUtil;

import static android.media.CamcorderProfile.get;

/**
 * Created by Nart on 27/10/2016.
 */
public class HomeNewAdapter extends RecyclerView.Adapter<HomeNewAdapter.ViewHolder> {
    Context mContext;
    LayoutInflater inflater;
    List<HomeData> homeDataList = new ArrayList<>();

    public HomeNewAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeData homeData = homeDataList.get(position);
        if (homeData.user.avatar != null && !homeData.user.avatar.isEmpty())
            Glide.with(mContext).load(homeData.user.avatar).into(holder.ivAccount);
        else
            holder.ivAccount.setImageResource(R.drawable.placeholer_avatar);
        if (homeData.image.url != null && !homeData.image.url.isEmpty())
            Glide.with(mContext).load(homeData.image.url).into(holder.ivPhoto);
        else
            holder.ivPhoto.setImageResource(R.drawable.placeholer_image_1600);

        StringUtil.displayText(homeData.user.username, holder.tvNameAccount);
        if (homeData.image.isFavourite = true)
            holder.ivFavorite.setImageResource(R.drawable.icon_favourite);
        else
            holder.ivFavorite.setImageResource(R.drawable.icon_no_favourite);
        if (homeData.user.isFollowing = true)
            holder.btFollow.setBackgroundResource(R.color.color_btn_follow_bg);
        else
            holder.btFollow.setBackgroundResource(R.color.color_button);
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
}
