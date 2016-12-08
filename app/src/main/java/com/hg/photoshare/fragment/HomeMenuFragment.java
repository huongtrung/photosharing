package com.hg.photoshare.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hg.photoshare.R;
import com.hg.photoshare.contants.Constant;
import com.hg.photoshare.manage.UserManage;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.SharedPrefUtils;
import vn.app.base.util.StringUtil;

import static vn.app.base.util.FragmentUtil.replaceFragment;

/**
 * Created by Nart on 26/10/2016.
 */
public class HomeMenuFragment extends BaseFragment {
    @BindView(R.id.iv_menu_account)
    CircleImageView ivAccount;
    @BindView(R.id.tv_menu_account)
    TextView tvName;
    @BindView(R.id.rl_user)
    RelativeLayout rlUser;
    @BindView(R.id.rl_home)
    RelativeLayout rlHome;
    @BindView(R.id.rl_post)
    RelativeLayout rlPost;
    @BindView(R.id.rl_favorite)
    RelativeLayout rlFavorite;
    @BindView(R.id.rl_nearby)
    RelativeLayout rlNearby;
    @BindView(R.id.rl_follow)
    RelativeLayout rlFollow;
    @BindView(R.id.rl_logout)
    RelativeLayout rlLogout;
    private String nameAccount;
    private String userId;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout mDrawerLayout;
    public View contentView;


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        contentView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });

        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        RelativeLayout iconDrawer = (RelativeLayout) toolbar.findViewById(R.id.item_bar);
        iconDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_home_menu;
    }


    @Override
    protected void initView(View root) {
    }

    @OnClick({R.id.rl_user, R.id.rl_home, R.id.rl_post, R.id.rl_favorite, R.id.rl_nearby, R.id.rl_follow, R.id.rl_logout})
    public void OnClick(View v) {
        mDrawerLayout.closeDrawers();
        switch (v.getId()) {
            case R.id.rl_user:
                FragmentUtil.replaceFragment(getFragmentManager(), ProfileFragment.newInstance(userId), null);
                break;
            case R.id.rl_home:
                FragmentUtil.replaceFragment(getFragmentManager(), HomeFragment.newInstance(), null);
                break;
            case R.id.rl_post:
                FragmentUtil.replaceFragment(getFragmentManager(), ImageUploadFragment.newInstance(), null);
                break;
            case R.id.rl_favorite:
                FragmentUtil.replaceFragment(getFragmentManager(), FavouriteFragment.newInstance(), null);
                break;
            case R.id.rl_nearby:
                FragmentUtil.replaceFragment(getFragmentManager(), NearbyFragment.newInstance(), null);
                break;
            case R.id.rl_follow:
                FragmentUtil.replaceFragment(getFragmentManager(), FollowFragment.newInstance(), null);
                break;
            case R.id.rl_logout:
                DialogUtil.showTwoBtnWithHandleDialog(getContext(), "Logout ?", "Are you sure you want to Logout ?", "Ok", "Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserManage.clearUserData();
                        UserManage.clearUser();
                        FragmentUtil.replaceFragment(getFragmentManager(), LoginFragment.newInstance(), null);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    protected void initData() {
        String decodeAvatar = SharedPrefUtils.getString(Constant.KEY_IMAGE_USER, "");
        nameAccount = SharedPrefUtils.getString(Constant.KEY_USER_NAME, "");
        userId = SharedPrefUtils.getString(Constant.KEY_USER_ID, "");
        if (!decodeAvatar.isEmpty())
            Glide.with(getContext()).load(decodeAvatar).into(ivAccount);
        else
            ivAccount.setImageResource(R.drawable.placeholer_avatar);
        StringUtil.displayText(nameAccount, tvName);
    }
}
