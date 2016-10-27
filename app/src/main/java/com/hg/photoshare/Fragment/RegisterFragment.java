package com.hg.photoshare.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.hg.photoshare.api.request.UploadImageRequest;
import com.hg.photoshare.api.respones.UploadImageResponse;
import com.hg.photoshare.contants.Contants;
import com.hg.photoshare.R;
import com.hg.photoshare.api.request.RegisterRequest;
import com.hg.photoshare.api.respones.RegisterResponse;
import com.hg.photoshare.manage.UserManage;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.KeyboardUtil;
import vn.app.base.util.SharedPrefUtils;
import vn.app.base.util.StringUtil;

/**
 * Created by on 10/16/2016.
 */

public class RegisterFragment extends BaseFragment {
    public static final String USER_PHOTO = "USER_PHOTO";
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
                    DialogUtil.showOkBtnDialog(getContext(), "Error : " +failCode, message);
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

    private void handleRegisterSuccess(RegisterResponse registerResponse) {
        if (registerResponse != null) {
            UserManage.saveCurrentUser(registerResponse.data);
            SharedPrefUtils.saveAccessToken(registerResponse.data.token);
            Log.e("user:", registerResponse.data.token);
            FragmentUtil.replaceFragment(getActivity(), ToturialFragment.newInstance(), null);
        }
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select File"), Contants.PICK_PHOTO_FOR_AVATAR);
    }

    private void pickForCam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Contants.CAM_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Contants.PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = null;
            if (data != null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().
                            getApplicationContext().getContentResolver(), data.getData());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ivAvatar.setImageBitmap(bitmap);
            }
        }
        if (requestCode == Contants.CAM_PHOTO_FOR_AVATAR && requestCode == Activity.RESULT_OK) ;
        {
            bitmap = (Bitmap) data.getExtras().get("data");
            ivAvatar.setImageBitmap(bitmap);
        }
    }

    public File savebitmap(Bitmap filename) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;

        File file = new File(filename + ".png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, filename + ".png");
            Log.e("file exist", "" + file + ",Bitmap= " + filename);
        }
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getName());

            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
        return file;
    }



}
