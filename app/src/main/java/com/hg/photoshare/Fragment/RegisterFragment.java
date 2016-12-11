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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hg.photoshare.contants.Constant;
import com.hg.photoshare.R;
import com.hg.photoshare.api.request.RegisterRequest;
import com.hg.photoshare.api.respones.RegisterResponse;
import com.hg.photoshare.manage.UserManage;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.api.volley.callback.SimpleRequestCallBack;
import vn.app.base.constant.APIConstant;
import vn.app.base.constant.ApiParam;
import vn.app.base.constant.AppConstant;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.BitmapUtil;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.ImagePickerUtil;
import vn.app.base.util.KeyboardUtil;
import vn.app.base.util.NetworkUtils;
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

    private String userName = "";
    private String emailAdd = "";
    private String password = "";
    private String compassword = "";
    Uri fileUri;
    Bitmap bitmap;
    private File fileImage;

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
        KeyboardUtil.hideKeyboard(getActivity());
        showCoverNetworkLoading();
        userName = etUser.getText().toString().trim();
        emailAdd = etEmail.getText().toString().trim();
        password = etPass.getText().toString().trim();
        compassword = etConfirm.getText().toString().trim();
        if (!StringUtil.checkStringValid(userName) || !StringUtil.checkStringValid(emailAdd)
                || !StringUtil.checkStringValid(password) || !StringUtil.checkStringValid(compassword)) {
            DialogUtil.showOkBtnDialog(getActivity(), "Required fields", "Please re-enter again");
        }
        if (!password.equals(compassword)) {
            DialogUtil.showOkBtnDialog(getActivity(), "Password not match ", "Please re-enter again");
        } else {
            try {
                showCoverNetworkLoading();
                Map<String, String> header = new HashMap<>();
                Map<String, String> params = new HashMap<>();
                params.put(APIConstant.FULLNAME, userName);
                params.put(APIConstant.EMAIL, emailAdd);
                params.put(APIConstant.PASSWORD, SHA1(password));
                Map<String, File> filePart = new HashMap<>();
                filePart.put(APIConstant.UPLOAD_IMAGE, fileImage);
                RegisterRequest registerRequest = new RegisterRequest(Request.Method.POST, APIConstant.REQUEST_URL_REGISTER, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DialogUtil.showOkBtnDialog(getContext(), "Register Fail", "Please register again !");
                    }
                }, RegisterResponse.class, header, new Response.Listener<RegisterResponse>() {
                    @Override
                    public void onResponse(RegisterResponse response) {
                        hideCoverNetworkLoading();
                        if (response != null && response.data != null)
                            handleRegisterSuccess(response);
                        else
                            DialogUtil.showOkBtnDialog(getContext(), "Register Fail", "Please register again !");
                    }
                }, params, filePart);
                NetworkUtils.getInstance(getActivity().getApplicationContext()).addToRequestQueue(registerRequest);
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
            replaceFragment(R.id.container, ToturialFragment.newInstance());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.PICK_PHOTO_FORM_AVATAR && resultCode == Activity.RESULT_OK) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().
                        getApplicationContext().getContentResolver(), data.getData());
                if (bitmap != null) {
                    ivAvatar.setImageBitmap(bitmap);
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
                ivAvatar.setImageBitmap(bitmap);
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
