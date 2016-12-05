package com.hg.photoshare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hg.photoshare.R;
import com.hg.photoshare.bean.ImageBean;
import com.hg.photoshare.bean.UserBean;
import com.hg.photoshare.data.HomeData;
import com.hg.photoshare.data.ImageListData;
import com.hg.photoshare.fragment.ImageDetailFragment;
import com.hg.photoshare.fragment.ProfileUserFragment;

import java.security.AccessController;
import java.util.List;

import vn.app.base.util.FragmentUtil;
import vn.app.base.util.StringUtil;

import static org.apache.http.HttpHeaders.IF;

/**
 * Created by GMORUNSYSTEM on 12/1/2016.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private List<ImageListData> imageListData;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageListData imageList = imageListData.get(position);
        if (imageList.user.avatar != null && !imageList.user.avatar.isEmpty())
            Glide.with(mContext).load(imageList.user.avatar).into(holder.ivAccount);
        else
            holder.ivAccount.setImageResource(R.drawable.placeholer_avatar);
        if (imageList.image.url != null && !imageList.image.url.isEmpty())
            Glide.with(mContext).load(imageList.image.url).into(holder.ivPhoto);
        else
            holder.ivPhoto.setImageResource(R.drawable.placeholer_image_1600);

        StringUtil.displayText(imageList.user.username, holder.tvName);
        if (imageList.image.isFavourite = true)
            holder.ivFavorite.setImageResource(R.drawable.icon_favourite);
        else
            holder.ivFavorite.setImageResource(R.drawable.icon_no_favourite);
        if (imageList.user.isFollowing = true)
            holder.btFollow.setBackgroundResource(R.color.color_btn_follow_bg);
        else
            holder.btFollow.setBackgroundResource(R.color.color_button);
        if (imageList.image.location != null && !imageList.image.location.isEmpty())
            StringUtil.displayText(imageList.image.location, holder.tvAddress);
        else
            holder.tvAddress.setVisibility(View.GONE);
        StringUtil.displayText(imageList.image.caption, holder.tvDescription);
        if (imageList.image.hashtag.size() > 0) {
            String result = "";
            for (String hashTag : imageList.image.hashtag) {
                result += "#" + hashTag + " ";
            }
            StringUtil.displayText(result, holder.tvHashTag);
        }
    }

    @Override
    public int getItemCount() {
        return imageListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAccount;
        private ImageView ivPhoto;
        private TextView tvAddress;
        private TextView tvDescription;
        private TextView tvHashTag;
        private ImageView ivFavorite;
        private TextView tvName;
        private Button btFollow;
        private LinearLayout llImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ivAccount = (ImageView) itemView.findViewById(R.id.iv_account);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            tvHashTag = (TextView) itemView.findViewById(R.id.tv_hash_tag);
            ivFavorite = (ImageView) itemView.findViewById(R.id.iv_favorite);
            tvName = (TextView) itemView.findViewById(R.id.tv_name_account);
            btFollow = (Button) itemView.findViewById(R.id.bt_follow);
            llImage=(LinearLayout)itemView.findViewById(R.id.ll_image);
        }
    }
}
