package com.hg.photoshare.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hg.photoshare.R;
import com.hg.photoshare.adapter.ImageListAdapter;
import com.hg.photoshare.api.request.ImageListRequest;
import com.hg.photoshare.api.request.ProfileRequest;
import com.hg.photoshare.api.request.UpdateProfileRequest;
import com.hg.photoshare.api.request.UserRequest;
import com.hg.photoshare.api.respones.ImageListResponse;
import com.hg.photoshare.api.respones.ProfileUserResponse;
import com.hg.photoshare.contants.Constant;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.api.volley.callback.SimpleRequestCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.BitmapUtil;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.SharedPrefUtils;
import vn.app.base.util.StringUtil;

import static android.R.id.message;

/**
 * Created by Nart on 26/10/2016.
 */
public class ProfileUserFragment extends BaseFragment {

    @BindView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.bt_change_avatar)
    ImageButton btChangeAvatar;
    @BindView(R.id.tv_name_account)
    TextView tvNameAccount;
    @BindView(R.id.tv_count_follow)
    TextView tvCountFollow;
    @BindView(R.id.tv_count_follower)
    TextView tvCountFollower;
    @BindView(R.id.tv_count_post)
    TextView tvCountPost;
    @BindView(R.id.rc_profile)
    RecyclerView rcProfile;
    @BindView(R.id.fab_profile)
    FloatingActionButton fabProfile;

    private String userName;
    private String mUserName;
    private String userId = "";
    private ImageListAdapter mImageListAdapter;

    File fileImage;

    public static ProfileUserFragment newInstance(String userName) {
        ProfileUserFragment fragment = new ProfileUserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.USERNAME, userName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initView(View root) {
        mUserName = SharedPrefUtils.getString(Constant.KEY_USER_NAME, "");
        mImageListAdapter = new ImageListAdapter(getContext());

        if (userName.equalsIgnoreCase(mUserName))
            setUpToolBarView(true, "Profile", true, "Update", true);

        else {
            setUpToolBarView(true, "User", true, "", false);
            fabProfile.setVisibility(View.GONE);
        }

        requestGetProfile();
        getRequestImageList();
    }

    private void requestGetProfile() {
        showCoverNetworkLoading();
        if (userName.equalsIgnoreCase(mUserName)) {
            ProfileRequest profilelRequest = new ProfileRequest();
            profilelRequest.setRequestCallBack(new ApiObjectCallBack<ProfileUserResponse>() {
                @Override
                public void onSuccess(ProfileUserResponse response) {
                    hideCoverNetworkLoading();
                    if (response != null)
                        showData(response);
                    else
                        DialogUtil.showOkBtnDialog(getContext(), "Error", "No data ");
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideCoverNetworkLoading();
                    DialogUtil.showOkBtnDialog(getContext(), "Error " + failCode, message);
                }
            });
            profilelRequest.execute();
        } else {
            UserRequest userRequest = new UserRequest(userName);
            userRequest.setRequestCallBack(new ApiObjectCallBack<ProfileUserResponse>() {
                @Override
                public void onSuccess(ProfileUserResponse data) {
                    hideCoverNetworkLoading();
                    if (data != null)
                        showData(data);
                    else
                        DialogUtil.showOkBtnDialog(getContext(), "Error", "No data");
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideCoverNetworkLoading();
                    DialogUtil.showOkBtnDialog(getContext(), "Error " + failCode, message);
                }
            });
            userRequest.execute();
        }
    }

    private void showData(ProfileUserResponse response) {
        userId = response.data.id;
        if (response.data.avatar != null && !response.data.avatar.isEmpty())
            Glide.with(getContext()).load(response.data.avatar).into(civAvatar);
        else
            civAvatar.setImageResource(R.drawable.dummy_avatar);
        StringUtil.displayText(response.data.username, tvNameAccount);
        StringUtil.displayText(response.data.follow.toString(), tvCountFollow);
        StringUtil.displayText(response.data.follower.toString(), tvCountFollower);
        StringUtil.displayText(response.data.post.toString(), tvCountPost);
    }

    private void getRequestImageList() {
        ImageListRequest imageListRequest = new ImageListRequest(userName, userId);
        imageListRequest.setRequestCallBack(new ApiObjectCallBack<ImageListResponse>() {
            @Override
            public void onSuccess(ImageListResponse response) {
                if (response != null)
                    setUpList(response);
                else
                    DialogUtil.showOkBtnDialog(getContext(), "Error", "No data");
            }

            @Override
            public void onFail(int failCode, String message) {
                DialogUtil.showOkBtnDialog(getContext(), "Error", message);
            }
        });
        imageListRequest.execute();
    }

    private void setUpList(ImageListResponse response) {
        mImageListAdapter.setImageListData(response.data);
        rcProfile.setAdapter(mImageListAdapter);
        rcProfile.setLayoutManager(new LinearLayoutManager(getContext()));
        mImageListAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.title_nav_item_bar)
    public void update() {
        showCoverNetworkLoading();
        new UpdateProfileRequest(fileImage, new SimpleRequestCallBack() {
            @Override
            public void onResponse(boolean success, String message) {
                hideCoverNetworkLoading();
                if (success)
                    Toast.makeText(getContext(), "aaaaa", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getContext(), "cc", Toast.LENGTH_LONG).show();

            }
        }).execute();
    }

    @OnClick(R.id.fab_profile)
    public void goPost() {
        FragmentUtil.pushFragment(getActivity(), ImageUploadFragment.newInstance(), null);
    }

    @Override
    protected void getArgument(Bundle bundle) {
        userName = bundle.getString(Constant.USERNAME);
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.bt_change_avatar)
    public void changeAvatar() {
        registerForContextMenu(btChangeAvatar);
        getActivity().openContextMenu(btChangeAvatar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.PICK_PHOTO_FORM_AVATAR && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().
                        getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap != null) {
                civAvatar.setImageBitmap(bitmap);
                fileImage = savebitmap(bitmap);
            }
        } else if (requestCode == Constant.CAM_PHOTO_FORM_AVATAR && resultCode == Activity.RESULT_OK) {
            Uri fileUri = getPhotoFileUri("temp.png");
            Bitmap bitmap = BitmapUtil.decodeFromFile(fileUri.getPath(), 800, 800);
            civAvatar.setImageBitmap(bitmap);
            fileImage = savebitmap(bitmap);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Pick Photo");
        menu.add(Menu.NONE, Constant.CONTEXT_MENU_FROM_CAM, Menu.NONE, "Camera");
        menu.add(Menu.NONE, Constant.CONTEXT_MENU_FROM_STORAGE, Menu.NONE, "Storage");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Constant.CONTEXT_MENU_FROM_CAM:
                pickFormCam();
                break;
            case Constant.CONTEXT_MENU_FROM_STORAGE:
                pickFormStorage();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
