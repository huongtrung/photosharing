package com.hg.photoshare.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hg.photoshare.R;
import com.hg.photoshare.contants.Constant;
import com.hg.photoshare.manage.UserManage;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.SharedPrefUtils;
import vn.app.base.util.StringUtil;

/**
 * Created by Nart on 26/10/2016.
 */
public class HomeMenuFragment extends BaseFragment {
    private static final String KEY_USER_NAME = "KEY_USER_NAME";
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

    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        contentView = getActivity().findViewById(R.id.container);
        ImageView openDrawer = (ImageView) toolbar.findViewById(R.id.iv_open_drawer);
        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    mDrawerLayout.closeDrawers();
//                } else {
                mDrawerLayout.openDrawer(GravityCompat.START);
//                }
//                getActivity().invalidateOptionsMenu();

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
                FragmentUtil.pushFragment(getActivity(), ProfileUserFragment.newInstance(nameAccount), null);
            }
        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.pushFragment(getActivity(), ProfileUserFragment.newInstance(nameAccount), null);
            }
        });
        rlUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.pushFragment(getActivity(), ProfileUserFragment.newInstance(nameAccount), null);
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
                DialogUtil.showTwoBtnWithHandleDialog(getContext(), "Logout ?", "Are you sure you want to Logout ?", "Ok", "Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserManage.clearUserData();
                        FragmentUtil.pushFragment(getActivity(), LoginFragment.newInstance(), null);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

            }
        });
    }

    private void onDrawerClosed() {
        if (!isAdded()) {
            return;
        }
        getActivity().invalidateOptionsMenu();
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    protected void initData() {
        String decodeAvatar = SharedPrefUtils.getString(Constant.KEY_IMAGE_USER, "");
        nameAccount = SharedPrefUtils.getString(Constant.KEY_USER_NAME, "");
        if (!decodeAvatar.isEmpty())
            Glide.with(getContext()).load(decodeAvatar).into(ivAccount);
        else
            ivAccount.setImageResource(R.drawable.placeholer_avatar);
        StringUtil.displayText(nameAccount, tvName);
    }
}
