package com.hg.photoshare.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hg.photoshare.Contants.Contants;
import com.hg.photoshare.R;
import com.hg.photoshare.api.request.RegisterRequest;
import com.hg.photoshare.api.respones.RegisterResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.OnClick;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.StringUtil;

/**
 * Created by on 10/16/2016.
 */

public class RegisterFragment extends BaseFragment {

    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;

    @BindView(R.id.etUser)
    EditText etUser;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPass)
    EditText etPass;

    @BindView(R.id.etConfirm)
    EditText etConfirm;

//    @BindView(R.id.btnSignUp)
//    Button btnSignUp;

    RegisterResponse registerResponse;

    private String userRegist = "";
    private String emailRegist = "";
    private String passRegist = "";
    private String compassRegist = "";
    private Bitmap bitmap;

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
        pickForCam();
    }

    @OnClick(R.id.btnSignUp)
    public void goToUserProfile(){
        checkTextRegist();
        RegisterRequest registerRequest = new RegisterRequest(userRegist, emailRegist,passRegist, bitmap);
        registerRequest.setRequestCallBack(new ApiObjectCallBack<RegisterResponse>() {
            @Override
            public void onSuccess(RegisterResponse data) {
                registerResponse = data;
                handleRegisterSuccess();
            }

            @Override
            public void onFail(int failCode, String message) {
                hideCoverNetworkLoading();
            }
        });
        registerRequest.execute();
        showCoverNetworkLoading();
    }

    private void handleRegisterSuccess() {
        if (registerResponse.registerBean != null) {
            FragmentUtil.replaceFragment(getActivity(), LoginFragment.newInstance(), null);
        }
    }

    private void checkTextRegist() {
        userRegist = etUser.getText().toString().trim();
        emailRegist = etEmail.getText().toString().trim();
        passRegist = etPass.getText().toString().trim();
        compassRegist = etConfirm.getText().toString().trim();
        if (!StringUtil.checkStringValid(userRegist) || !StringUtil.checkStringValid(emailRegist)
                || !StringUtil.checkStringValid(passRegist) || !StringUtil.checkStringValid(compassRegist)) {
            DialogUtil.showOkBtnDialog(getActivity(), "Required fields", "Please re-enter again");
            return;
        }
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        int id = item.getItemId();
//        if (id == R.id.take_photo)
//        {
//            pickImage();
//            return true;
//        }
//        if (id==R.id.camera_photo){
//            pickForCam();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        getActivity().getMenuInflater().inflate(R.menu.register_choose_menu,menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

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


}
