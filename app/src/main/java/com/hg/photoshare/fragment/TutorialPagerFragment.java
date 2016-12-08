package com.hg.photoshare.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hg.photoshare.R;
import com.hg.photoshare.bean.ProfileBean;
import com.hg.photoshare.bean.TutorialBean;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.imageloader.ImageLoader;
import vn.app.base.util.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutorialPagerFragment extends BaseFragment {
    @BindView(R.id.iv_account_toturial)
    CircleImageView ivAccount;
    @BindView(R.id.iv_toturial)
    ImageView ivBackground;
    @BindView(R.id.tv_toturial)
    TextView tvToturial;

    String avatarUser;
    String title;
    String background;
    boolean showAvatar;

    public static final String KEY_TUTORIAL_TITLE = "KEY_TUTORIAL_TITLE";
    public static final String KEY_TUTORIAL_AVATAR = "KEY_TUTORIAL_AVATAR";
    public static final String KEY_TUTORIAL_SHOW_AVATAR = "KEY_TUTORIAL_SHOW_AVATAR";
    public static final String KEY_TUTORIAL_IMAGE = "KEY_TUTORIAL_IMAGE";

    public static TutorialPagerFragment newInstance(TutorialBean tutorialBean, ProfileBean profile) {
        TutorialPagerFragment fragment = new TutorialPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TUTORIAL_TITLE, tutorialBean.title);
        bundle.putString(KEY_TUTORIAL_AVATAR, profile.avatar);
        bundle.putString(KEY_TUTORIAL_IMAGE, tutorialBean.image);
        bundle.putBoolean(KEY_TUTORIAL_SHOW_AVATAR, tutorialBean.showAvatar);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tutorial_pager;
    }

    @Override
    protected void initView(View root) {

    }

    @Override
    protected void getArgument(Bundle bundle) {
        avatarUser = bundle.getString(KEY_TUTORIAL_AVATAR);
        title = bundle.getString(KEY_TUTORIAL_TITLE);
        background = bundle.getString(KEY_TUTORIAL_IMAGE);
        showAvatar = bundle.getBoolean(KEY_TUTORIAL_SHOW_AVATAR);
    }

    @Override
    protected void initData() {
        ImageLoader.loadImage(getContext(), background, ivBackground);
        StringUtil.displayText(title,tvToturial);
        if (showAvatar == true) {
            if (avatarUser.isEmpty()) {
                ivAccount.setImageResource(R.drawable.placeholer_avatar);
            } else {
                ImageLoader.loadImage(getContext(), avatarUser, ivAccount);
            }
        } else {
            ivAccount.setVisibility(View.GONE);
        }
    }
}
