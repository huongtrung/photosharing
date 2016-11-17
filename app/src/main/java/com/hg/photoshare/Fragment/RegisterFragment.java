package com.hg.photoshare.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.hg.photoshare.contants.Constant;
import com.hg.photoshare.R;
import com.hg.photoshare.api.request.RegisterRequest;
import com.hg.photoshare.api.respones.RegisterResponse;
import com.hg.photoshare.manage.UserManage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.api.volley.callback.SimpleRequestCallBack;
import vn.app.base.constant.AppConstant;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.BitmapUtil;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.KeyboardUtil;
import vn.app.base.util.SharedPrefUtils;
import vn.app.base.util.StringUtil;

/**
 * Created by on 10/16/2016.
 */

public class RegisterFragment extends BaseFragment {
    @BindView(R.id.ivAvatar)
    CircleImageView ivAvatar;

    @BindView(R.id.etUser)
    EditText etUser;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPass)
    EditText etPass;

    @BindView(R.id.etConfirm)
    EditText etConfirm;

    RegisterResponse registerResponse;

    private String userName = "";
    private String emailAdd = "";
    private String password = "";
    private String compassword = "";
    File fileImageAvatar;
    Bitmap bitmap;

    public static RegisterFragment newInstance() {
        RegisterFragment registerFragment = new RegisterFragment();
        return registerFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initView(View root) {

    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.ivAvatar)
    public void chooseImage() {
        registerForContextMenu(ivAvatar);
        getActivity().openContextMenu(ivAvatar);
    }

    @OnClick(R.id.btnSignUp)
    public void goToUserProfile() {
        userName = etUser.getText().toString().trim();
        emailAdd = etEmail.getText().toString().trim();
        password = etPass.getText().toString().trim();
        compassword = etConfirm.getText().toString().trim();
        if (!StringUtil.checkStringValid(userName) || !StringUtil.checkStringValid(emailAdd)
                || !StringUtil.checkStringValid(password) || !StringUtil.checkStringValid(compassword)) {
            DialogUtil.showOkBtnDialog(getActivity(), "Required fields", "Please re-enter again");
            return;
        }
        if (!password.equals(compassword)) {
            DialogUtil.showOkBtnDialog(getActivity(), "Password not match ", "Please re-enter again");
        } else {
            try {
                RegisterRequest registerRequest = new RegisterRequest(userName, emailAdd, SHA1(password));
                registerRequest.setRequestCallBack(new ApiObjectCallBack<RegisterResponse>() {
                    @Override
                    public void onSuccess(RegisterResponse data) {
                        registerResponse = data;
                        handleRegisterSuccess(registerResponse);
                        hideCoverNetworkLoading();
                    }

                    @Override
                    public void onFail(int failCode, String message) {
                        hideCoverNetworkLoading();
                        DialogUtil.showOkBtnDialog(getContext(), "Error : " + failCode, message);
                    }
                });
                registerRequest.execute();
                KeyboardUtil.hideKeyboard(getActivity());
                showCoverNetworkLoading();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRegisterSuccess(RegisterResponse registerResponse) {
        if (registerResponse != null) {
            UserManage.saveCurrentUser(registerResponse.data);
            SharedPrefUtils.saveAccessToken(registerResponse.data.token);
            Log.e("user:", registerResponse.data.token);
            FragmentUtil.pushFragment(getActivity(), ToturialFragment.newInstance(), null);
        }
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
                ivAvatar.setImageBitmap(bitmap);
                fileImageAvatar = savebitmap(bitmap);
                Log.e("file", fileImageAvatar + "");
            }
        } else if (requestCode == Constant.CAM_PHOTO_FORM_AVATAR && resultCode == Activity.RESULT_OK) {
            Uri fileUri = getPhotoFileUri("temp.png");
            Bitmap bitmap = BitmapUtil.decodeFromFile(fileUri.getPath(), 1900, 600);
            ivAvatar.setImageBitmap(bitmap);
            fileImageAvatar = savebitmap(bitmap);
            Log.e("file", fileImageAvatar + "");
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
