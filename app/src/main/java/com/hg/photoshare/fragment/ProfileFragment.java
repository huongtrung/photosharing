package com.hg.photoshare.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.hg.photoshare.R;
import com.hg.photoshare.adapter.HomeNewAdapter;
import com.hg.photoshare.adapter.ImageListAdapter;
import com.hg.photoshare.api.request.ImageListRequest;
import com.hg.photoshare.api.request.ProfileRequest;
import com.hg.photoshare.api.request.UpdateProfileRequest;
import com.hg.photoshare.api.request.UserRequest;
import com.hg.photoshare.api.respones.ImageListResponse;
import com.hg.photoshare.api.respones.ProfileUserResponse;
import com.hg.photoshare.bean.ImageBean;
import com.hg.photoshare.bean.UserBean;
import com.hg.photoshare.contants.Constant;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.app.base.adapter.DividerItemDecoration;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.constant.APIConstant;
import vn.app.base.constant.ApiParam;
import vn.app.base.constant.AppConstant;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.BitmapUtil;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.ImagePickerUtil;
import vn.app.base.util.NetworkUtils;
import vn.app.base.util.SharedPrefUtils;
import vn.app.base.util.StringUtil;

import static android.R.attr.bitmap;

/**
 * Created by Nart on 26/10/2016.
 */
public class ProfileFragment extends BaseFragment {

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
    @BindView(R.id.swipe_profile)
    SwipeRefreshLayout swipeRefreshProfile;
    private Uri fileUri;

    private String mUserId;
    private String userId;
    private ImageListAdapter mImageListAdapter;
    private File fileImage;
    private Bitmap bitmap;

    public static ProfileFragment newInstance(String userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initView(View root) {
        mImageListAdapter = new ImageListAdapter(getContext());
        setUpToolBarView(true, "Profile", true, "Update", true);

        swipeRefreshProfile.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRequestImageList();
            }
        });
        swipeRefreshProfile.setColorSchemeResources(android.R.color.holo_blue_bright);

        requestGetProfile();
        getRequestImageList();

        mImageListAdapter.setmOnItemClickListener(new HomeNewAdapter.OnItemClickListener() {

            @Override
            public void onItemAvatarClick(View view, String userId) {
            }

            @Override
            public void onItemNameClick(View view, String userId) {
            }

            @Override
            public void onItemPhotoClick(View view, ImageBean imageBean, UserBean userBean) {
                replaceFragment(R.id.container, ImageDetailFragment.newInstance(imageBean, userBean));
            }

            @Override
            public void OnItemLocationClick(View view, String lat, String longtitude) {
                Uri uri = Uri.parse(String.format(Locale.ENGLISH, "geo:%f,%f", Float.valueOf(lat), Float.valueOf(longtitude)));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }

        });
    }

    private void requestGetProfile() {
        showCoverNetworkLoading();
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
    }

    private void showData(ProfileUserResponse response) {
        if (response.data.avatar != null && !response.data.avatar.isEmpty())
            Glide.with(getContext()).load(response.data.avatar).into(civAvatar);
        else
            civAvatar.setImageResource(R.drawable.placeholer_avatar);
        StringUtil.displayText(response.data.username, tvNameAccount);
        StringUtil.displayText(response.data.follow.toString(), tvCountFollow);
        StringUtil.displayText(response.data.follower.toString(), tvCountFollower);
        StringUtil.displayText(response.data.post.toString(), tvCountPost);

    }

    private void getRequestImageList() {
        ImageListRequest imageListRequest = new ImageListRequest(userId);
        imageListRequest.setRequestCallBack(new ApiObjectCallBack<ImageListResponse>() {
            @Override
            public void onSuccess(ImageListResponse response) {
                if (response != null) {
                    setUpList(response);
                    if (swipeRefreshProfile.isRefreshing())
                        swipeRefreshProfile.setRefreshing(false);
                } else {
                    DialogUtil.showOkBtnDialog(getContext(), "Error", "No data");
                    if (swipeRefreshProfile.isRefreshing())
                        swipeRefreshProfile.setRefreshing(false);
                }
            }

            @Override
            public void onFail(int failCode, String message) {
                if (swipeRefreshProfile.isRefreshing())
                    swipeRefreshProfile.setRefreshing(false);
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
        Map<String, String> header = new HashMap<>();
        header.put(ApiParam.TOKEN, SharedPrefUtils.getAccessToken());

        Map<String, String> params = new HashMap<>();


        Map<String, File> filePart = new HashMap<>();
        filePart.put(APIConstant.UPLOAD_IMAGE, fileImage);

        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest(Request.Method.POST, APIConstant.REQUEST_URL_UPDATE_PROFILE, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideCoverNetworkLoading();
                DialogUtil.showOkBtnDialog(getContext(), "Update Fail", "Update Profile Failed !");
            }
        }, ProfileUserResponse.class, header, new Response.Listener<ProfileUserResponse>() {
            @Override
            public void onResponse(ProfileUserResponse response) {
                hideCoverNetworkLoading();
                showData(response);
                getRequestImageList();
                SharedPrefUtils.putString(Constant.KEY_IMAGE_USER,response.data.avatar );
                DialogUtil.showOkBtnDialog(getContext(), "Upload Success", "Upload Image Success !");

            }
        }, params, filePart);
        NetworkUtils.getInstance(getActivity().getApplicationContext()).addToRequestQueue(updateProfileRequest);
    }


    @OnClick(R.id.fab_profile)
    public void goPost() {
        replaceFragment(R.id.container, ImageUploadFragment.newInstance());
    }

    @Override
    protected void getArgument(Bundle bundle) {
        userId = bundle.getString(Constant.USER_ID);
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
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().
                        getApplicationContext().getContentResolver(), data.getData());
                if (bitmap != null) {
                    civAvatar.setImageBitmap(bitmap);
                    fileImage = savebitmap(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == Constant.CAM_PHOTO_FORM_AVATAR && resultCode == Activity.RESULT_OK) {
            CropImage.activity(fileUri).setAspectRatio(16, 9).start(getContext(), this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmap = BitmapUtil.decodeFromFile(resultUri.getPath(), 900, 900);
                try {
                    fileImage = creatFilefromBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                civAvatar.setImageBitmap(bitmap);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
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
        ImagePickerUtil imagePickerUtil = new ImagePickerUtil();
        switch (item.getItemId()) {
            case Constant.CONTEXT_MENU_FROM_CAM:

                Intent getCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = Uri.fromFile(imagePickerUtil.createFileUri(getActivity()));
                getCamera.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(getCamera, AppConstant.CAM_PHOTO_FORM_AVATAR);
                break;
            case Constant.CONTEXT_MENU_FROM_STORAGE:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select File"), AppConstant.PICK_PHOTO_FORM_AVATAR);
                break;
        }
        return super.onContextItemSelected(item);
    }
}
