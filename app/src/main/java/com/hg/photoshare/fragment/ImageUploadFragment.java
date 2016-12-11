package com.hg.photoshare.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hg.photoshare.R;
import com.hg.photoshare.api.request.ImageUploadRequest;
import com.hg.photoshare.api.respones.ImageUploadRespones;
import com.hg.photoshare.contants.ActiveHashTag;
import com.hg.photoshare.contants.Constant;
import com.hg.photoshare.contants.GPSTracker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
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

import com.google.android.gms.location.LocationServices;
import com.theartofdev.edmodo.cropper.CropImage;

import static android.R.attr.country;

/**
 * Created by Nart on 26/10/2016.
 */
public class ImageUploadFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    @BindView(R.id.sc_location)
    Switch scLocation;
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
    String hashtag;
    Bitmap bitmap;
    File fileImage;
    double latitude;
    double longtitude;
    String strlat;
    String strlong;
    String location;
    String caption;
    private Uri fileUri;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mCurrentLocation;

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
        Map<String, String> header = new HashMap<>();
        header.put(ApiParam.TOKEN, SharedPrefUtils.getAccessToken());
        Map<String, String> params = new HashMap<>();
        if (!caption.isEmpty()) {
            params.put(APIConstant.CAPTION, caption);
        }

        if (!hashtag.isEmpty()) {
            params.put(APIConstant.HASHTAG, hashtag);
        }

        if (scLocation.isChecked()) {
            params.put(APIConstant.LOCATION, location);
            params.put(APIConstant.LATITUDE, strlat);
            params.put(APIConstant.LONGTITUDE, strlong);
        }

        Map<String, File> filePart = new HashMap<>();
        filePart.put(APIConstant.IMAGE_PUT, fileImage);

        ImageUploadRequest uploadImageRequest = new ImageUploadRequest(Request.Method.POST, APIConstant.REQUEST_URL_IMAGE_UPLOAD, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideCoverNetworkLoading();
                DialogUtil.showOkBtnDialog(getContext(), "Upload Fail", "Upload Image Fail !");
            }
        }, ImageUploadRespones.class, header, new Response.Listener<ImageUploadRespones>() {
            @Override
            public void onResponse(ImageUploadRespones response) {
                hideCoverNetworkLoading();
                if (response != null) {
                    DialogUtil.showOkBtnDialog(getContext(), "Success", "Upload Image Success !");
                    resetLayout();
                }
            }
        }, params, filePart);

        NetworkUtils.getInstance(getActivity().getApplicationContext()).addToRequestQueue(uploadImageRequest);
    }

    private void resetLayout(){
        fileImage =null;
        ivPhotoUpload.setImageResource(R.drawable.placeholer_image_800);
        etCaption.setText("");
        scLocation.setChecked(false);
        etHashTag.setText("");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.PICK_PHOTO_FORM_AVATAR && resultCode == Activity.RESULT_OK) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().
                        getApplicationContext().getContentResolver(), data.getData());
                if (bitmap != null) {
                    ivPhotoUpload.setImageBitmap(bitmap);
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
                ivPhotoUpload.setImageBitmap(bitmap);
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

    @OnClick(R.id.bt_cancle)
    public void cancle() {
        FragmentUtil.popBackStack(getActivity());
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void getAddress() {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longtitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            location = address + ", " + state + ", " + city + ", " + country;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mCurrentLocation != null) {

            latitude = mCurrentLocation.getLatitude();
            longtitude = mCurrentLocation.getLongitude();

            strlat = Double.toString(latitude);
            strlong = Double.toString(longtitude);

            getAddress();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (scLocation.isChecked()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (scLocation.isChecked()) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @OnCheckedChanged(R.id.sc_location)
    public void checkChangedSend() {
        if (scLocation.isChecked()) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
