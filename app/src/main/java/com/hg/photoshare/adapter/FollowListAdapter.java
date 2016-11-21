package com.hg.photoshare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hg.photoshare.R;
import com.hg.photoshare.bean.UserBean;

import java.util.List;

import vn.app.base.util.StringUtil;

/**
 * Created by GMORUNSYSTEM on 11/21/2016.
 */

public class FollowListAdapter extends RecyclerView.Adapter<FollowListAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context mContext;
    private List<UserBean> userList;

    public FollowListAdapter(Context context) {
        this.mContext = context;
        inflater=LayoutInflater.from(context);
    }

    public void setUserList(List<UserBean> userList) {
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_follow, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserBean userBean = userList.get(position);
        Glide.with(mContext).load(userBean.avatar).into(holder.ivAvatarFollow);
        StringUtil.displayText(userBean.username,holder.tvName);
        if (userBean.isFollowing)
            holder.btFollow.setVisibility(View.VISIBLE);
        else
            holder.btFollow.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatarFollow;
        TextView tvName;
        Button btFollow;

        public ViewHolder(View itemView) {
            super(itemView);
             ivAvatarFollow = (ImageView) itemView.findViewById(R.id.iv_avatar_follow);
             tvName = (TextView) itemView.findViewById(R.id.tv_name);
             btFollow = (Button) itemView.findViewById(R.id.bt_follow);
        }
    }
}

