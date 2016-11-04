package com.hg.photoshare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hg.photoshare.R;
import com.hg.photoshare.bean.ImageBean;
import com.hg.photoshare.data.HomeData;

import java.util.ArrayList;
import java.util.List;

import vn.app.base.imageloader.ImageLoader;
import vn.app.base.util.StringUtil;

/**
 * Created by Nart on 27/10/2016.
 */
public class HomeNewAdapter extends RecyclerView.Adapter<HomeNewAdapter.ViewHolder> {
    Context mContext;
    LayoutInflater layoutInflater;
    List<HomeData> homeDataList = new ArrayList<>();

    public HomeNewAdapter(List<HomeData> homeDataList) {
        this.homeDataList = homeDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StringUtil.displayText(homeDataList.get(position).user.username, holder.tvNameAccount);
        ImageLoader.loadImage(mContext.getApplicationContext(), homeDataList.get(position).user.avatar, holder.ivAccount);
        if (homeDataList.get(position).user.isFollowing = true) {
            holder.btFollow.setText("Following");
//            holder.btFollow.setBackgroundColor();
        } else {
            holder.btFollow.setText("Follow");
        }
        ImageLoader.loadImage(mContext.getApplicationContext(), homeDataList.get(position).image.url, holder.ivPhoto);
        StringUtil.displayText(homeDataList.get(position).image.location, holder.tvLocation);
        StringUtil.displayText(homeDataList.get(position).image.caption, holder.tvCaption);
//        StringUtil.displayText(homeDataList.get(position).image.hashtag, holder.tvHashTag);
        if (homeDataList.get(position).image.isFavourite = true) {
            holder.ivFavorite.setImageResource(R.drawable.icon_favourite);
        }
        else {
            holder.ivFavorite.setImageResource(R.drawable.icon_no_favourite);
        }

    }

    @Override
    public int getItemCount() {
       if (homeDataList==null){
           return 0;
       }
        return homeDataList.size()+1;
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
