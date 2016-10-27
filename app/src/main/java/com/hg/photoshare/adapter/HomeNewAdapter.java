package com.hg.photoshare.adapter;

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
    LayoutInflater layoutInflater;
    List<HomeData> homeDataList = new ArrayList<>();

    public HomeNewAdapter(LayoutInflater layoutInflater, List<HomeData> homeDataList) {
        this.layoutInflater = layoutInflater;
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
