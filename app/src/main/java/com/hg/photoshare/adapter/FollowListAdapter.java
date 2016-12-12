package com.hg.photoshare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hg.photoshare.R;
import com.hg.photoshare.api.request.FollowRequest;
import com.hg.photoshare.bean.UserBean;
import com.hg.photoshare.contants.Constant;
import com.hg.photoshare.data.UserData;
import com.hg.photoshare.inter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.app.base.api.response.BaseResponse;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.SharedPrefUtils;
import vn.app.base.util.StringUtil;

import static android.R.attr.data;
import static com.hg.photoshare.R.id.item_container;

/**
 * Created by GMORUNSYSTEM on 11/21/2016.
 */

public class FollowListAdapter extends RecyclerView.Adapter<FollowListAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private List<UserData> userList = new ArrayList<>();
    private int isFollow;
    private String mUserId;

    public FollowListAdapter(Context context, OnItemClickListener itemClickListener) {
        this.mContext = context;
        this.mOnItemClickListener = itemClickListener;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final UserData userData = userList.get(position);

        if (userData.user.avatar != null && !userData.user.avatar.isEmpty())
            Glide.with(mContext).load(userData.user.avatar).into(holder.ivAvatarFollow);
        else
            holder.ivAvatarFollow.setImageResource(R.drawable.placeholer_avatar);
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
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position, userData.user.id);
            }
        });
        holder.btFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowRequest followRequest = new FollowRequest(userData.user.id, isFollow);
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
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivAvatarFollow;
        TextView tvName;
        Button btFollow;
        View container;

        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView;
            ivAvatarFollow = (CircleImageView) itemView.findViewById(R.id.iv_avatar_follow);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            btFollow = (Button) itemView.findViewById(R.id.bt_follow);
        }
    }
}

