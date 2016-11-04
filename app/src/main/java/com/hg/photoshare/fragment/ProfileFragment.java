package com.hg.photoshare.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.hg.photoshare.R;
import com.hg.photoshare.contants.Constant;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.BitmapUtil;
import vn.app.base.util.FragmentUtil;

/**
 * Created by Nart on 26/10/2016.
 */
public class ProfileFragment extends BaseFragment {

    @BindView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.bt_change_avatar)
    ImageButton btChangeAvatar;

    File fileImage;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initView(View root) {
        setUpToolBarView(true, "Profile", true, "Update", true);
    }

    @OnClick(R.id.title_nav_item_bar)
    public void update() {
        Log.e("aaaaa", "aaaaaaaaa");
    }

    @OnClick(R.id.fab_profile)
    public void goPost() {
        FragmentUtil.replaceFragment(getActivity(), ImageUploadFragment.newInstance(), null);
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    protected void initData() {

    }
    @OnClick(R.id.bt_change_avatar)
    public void changeAvatar(){
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
                Log.e("file", fileImage + "");
            }
        } else if (requestCode == Constant.CAM_PHOTO_FORM_AVATAR && resultCode == Activity.RESULT_OK) {
            Uri fileUri = getPhotoFileUri("temp.png");
            Bitmap bitmap = BitmapUtil.decodeFromFile(fileUri.getPath(), 800, 800);
            civAvatar.setImageBitmap(bitmap);
            fileImage = savebitmap(bitmap);
            Log.e("file", fileImage + "");
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
