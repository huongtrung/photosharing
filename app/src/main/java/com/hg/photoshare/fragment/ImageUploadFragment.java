package com.hg.photoshare.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.hg.photoshare.R;
import com.hg.photoshare.api.request.ImageUploadRequest;
import com.hg.photoshare.contants.ActiveHashTag;
import com.hg.photoshare.contants.Constant;
import com.hg.photoshare.contants.GPSTracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import vn.app.base.api.volley.callback.SimpleRequestCallBack;
import vn.app.base.constant.AppConstant;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.BitmapUtil;
import vn.app.base.util.ColorUtil;
import vn.app.base.util.DialogUtil;

/**
 * Created by Nart on 26/10/2016.
 */
public class ImageUploadFragment extends BaseFragment {
    @BindView(R.id.sc_location)
    SwitchCompat scLocation;
    @BindView(R.id.iv_photo_upload)
    ImageView ivPhotoUpload;
    @BindView(R.id.bt_camera_upload)
    ImageView btCameraUpload;
    @BindView(R.id.et_hashtag)
    EditText etHashTag;
    @BindView(R.id.bt_cancle)
    Button btCancle;
    @BindView(R.id.et_caption)
    EditText etCaption;
    String hashtag = "";

    private boolean switchStatus = false;
    Bitmap bitmap;
    File fileImage;
    GPSTracker mGPS;
    double latitude;
    double longtitude;
    String location;
    String caption;

    public static ImageUploadFragment newInstance() {
        ImageUploadFragment fragment = new ImageUploadFragment();
        return fragment;
    }

    @OnClick(R.id.bt_camera_upload)
    void openCam() {
        registerForContextMenu(btCameraUpload);
        getActivity().openContextMenu(btCameraUpload);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_upload;
    }

    @Override
    protected void initView(View root) {
        setUpToolBarView(true, "Post Image", true, "", false);
        scLocation.setChecked(false);
        ColorUtil.switchColor(switchStatus, scLocation);
        scLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mGPS = new GPSTracker(getContext());
                if (mGPS.canGetLocation()) {
                    latitude = mGPS.getLatitude();
                    longtitude = mGPS.getLongitude();
                    location = mGPS.getLocation().toString();
                    Log.e("lat", latitude + "");
                    Log.e("long", longtitude + "");
                    Log.e("location", location + "");
                } else {
                    mGPS.showSettingsAlert();
                }
            }
        });

        char[] additionalSymbols = new char[]{'_'};
        final ActiveHashTag editHashTag = ActiveHashTag.Factory.create(ResourcesCompat.getColor(getResources(), R.color.color_white, null), null, additionalSymbols);
        editHashTag.operate(etHashTag);

        etHashTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> charString = editHashTag.getAllHashTags(true);
                ArrayList<String> listString = new ArrayList<String>();
                for (String s : charString) {
                    String[] cut = new String[charString.size()];
                    cut = s.split("#");
                    listString.add(cut[1]);
                }
                String result = "";
                for (String s : listString) {
                    result += s + ",";
                }
                if (result != null & result.length() > 0) {
                    result = result.substring(0, result.length() - 1);
                }
                Log.e("result", result);
                hashtag = result;
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                Spannable wordtoSpan = new SpannableString(hashtag);
//                wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 1, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                etHashTag.setText(wordtoSpan);
            }
        });
    }


    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.bt_post)
    public void post() {
        caption = etCaption.getText().toString();
        showCoverNetworkLoading();
        new ImageUploadRequest(fileImage, caption, location, latitude,
                longtitude, hashtag, new SimpleRequestCallBack() {
            @Override
            public void onResponse(boolean success, String message) {
                hideCoverNetworkLoading();
                if (success) {
                    Log.e("success", success + message + "");
                } else {
                    DialogUtil.showOkBtnDialog(getContext(), "Error", message + "");
                }
            }

        }).execute();
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
                ivPhotoUpload.setImageBitmap(bitmap);
                fileImage = savebitmap(bitmap);
                Log.e("file", fileImage + "");
            }
        } else if (requestCode == Constant.CAM_PHOTO_FORM_AVATAR && resultCode == Activity.RESULT_OK) {
            Uri fileUri = getPhotoFileUri("temp.png");
            Bitmap bitmap = BitmapUtil.decodeFromFile(fileUri.getPath(), 1900, 600);
            fileImage = savebitmap(bitmap);
            Log.e("file", fileImage + "");
            ivPhotoUpload.setImageBitmap(bitmap);

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

    @OnClick(R.id.bt_cancle)
    public void cancle() {
        handleBack();
    }
}
