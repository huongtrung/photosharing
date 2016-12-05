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
import com.hg.photoshare.api.request.FollowRequest;
import com.hg.photoshare.bean.UserBean;
import com.hg.photoshare.data.UserData;

import java.util.ArrayList;
import java.util.List;

import vn.app.base.api.response.BaseResponse;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.StringUtil;

import static android.R.attr.data;

/**
 * Created by GMORUNSYSTEM on 11/21/2016.
 */

public class FollowListAdapter extends RecyclerView.Adapter<FollowListAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context mContext;
    private List<UserData> userList = new ArrayList<>();

    public FollowListAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setUserList(List<UserData> userList) {
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
        final UserData userData = userList.get(position);
        if (userData.user.avatar != null && !userData.user.avatar.isEmpty())
            Glide.with(mContext).load(userData.user.avatar).into(holder.ivAvatarFollow);
        else
            holder.ivAvatarFollow.setImageResource(R.drawable.dummy_avatar);
        StringUtil.displayText(userData.user.username, holder.tvName);
        if (userData.user.isFollowing != null) {
            if (userData.user.isFollowing) {
                holder.btFollow.setText("Following");
                holder.btFollow.setBackgroundResource(R.color.color_btn_follow_bg);
            } else {
                holder.btFollow.setText("Follow");
                holder.btFollow.setBackgroundResource(R.color.color_button);
            }
        }
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

