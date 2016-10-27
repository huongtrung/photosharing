package com.hg.photoshare.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.hg.photoshare.R;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.FragmentUtil;

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

    private static final String PREF_USER_LEARNED_DRAWER = "Navigation drawer learn";
    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;
    private int mCurrentSelectedPosition = 0;
    private boolean mUserLearnedDrawer;
    public View contentView;
    int currentMenuPos = 0;

    public View getMenuByPosition(int position) {
        switch (position) {
            case 1:
                return rlUser;
            case 2:
                return rlHome;
            case 3:
                return rlPost;
            case 4:
                return rlFavorite;
            case 5:
                return rlNearby;
            case 6:
                return rlFollow;
            case 7:
                return rlLogout;
            default:
                return null;
        }
    }

    public void setCurrentMenu(int position) {
        View currentMenu = getMenuByPosition(currentMenuPos);
        if (currentMenu != null) {
            currentMenu.setSelected(false);
        }
        this.currentMenuPos = position;
        currentMenu = getMenuByPosition(currentMenuPos);
        if (currentMenu != null) {
            currentMenu.setSelected(true);
        }
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        contentView = getActivity().findViewById(R.id.container);
        ImageView ivOpenDrawer = (ImageView) rootView.findViewById(R.id.iv_open_drawer);
        ivOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                getActivity().invalidateOptionsMenu();
            }
        });
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                Float moveFactor = (drawerView.getWidth() * slideOffset);
                contentView.setTranslationX(moveFactor);
                mDrawerLayout.bringChildToFront(drawerView);
                mDrawerLayout.requestLayout();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (!isAdded()) {
                    return;
                }
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (!isAdded()) {
                    return;
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        setCurrentMenu(3);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_home_menu;
    }

    @Override
    protected void initView(View root) {
        ivAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.pushFragment(getActivity(), ProfileFragment.newInstance(), null);
            }
        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.pushFragment(getActivity(), ProfileFragment.newInstance(), null);
            }
        });
        rlUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.pushFragment(getActivity(), ProfileFragment.newInstance(), null);
            }
        });
        rlHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.pushFragment(getActivity(), HomeFragment.newInstance(), null);
            }
        });
        rlPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.pushFragment(getActivity(), ImageUploadFragment.newInstance(), null);
            }
        });
        rlFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.pushFragment(getActivity(), FavouriteFragment.newInstance(), null);
            }
        });
        rlNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.pushFragment(getActivity(), NearbyFragment.newInstance(), null);
            }
        });
        rlFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.pushFragment(getActivity(), FollowFragment.newInstance(), null);
            }
        });
        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.pushFragment(getActivity(), LoginFragment.newInstance(), null);
            }
        });
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    protected void initData() {

    }
}
