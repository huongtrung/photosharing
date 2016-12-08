package com.hg.photoshare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hg.photoshare.R;
import com.hg.photoshare.data.CommentListData;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.app.base.util.StringUtil;

/**
 * Created by GMORUNSYSTEM on 12/8/2016.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context mContext;
    private List<CommentListData> commentListDatas = new ArrayList<>();

    public CommentListAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    public void setCommentListDatas(List<CommentListData> commentListDatas) {
        this.commentListDatas = commentListDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_coment_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentListData commentListData = commentListDatas.get(position);
        if (commentListData.user.avatar != null && !commentListData.user.avatar.isEmpty())
            Glide.with(mContext).load(commentListData.user.avatar).into(holder.ivCommentAccount);
        else
            holder.ivCommentAccount.setImageResource(R.drawable.placeholer_avatar);
            StringUtil.displayText(commentListData.comment,holder.tvCommentList);

    }

    @Override
    public int getItemCount() {
        return commentListDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivCommentAccount;
        TextView tvCommentList;

        public ViewHolder(View itemView) {
            super(itemView);
            ivCommentAccount = (CircleImageView) itemView.findViewById(R.id.iv_account_comment);
            tvCommentList = (TextView) itemView.findViewById(R.id.tv_comment_list);
        }
    }
}
