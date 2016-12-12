package com.hg.photoshare.fragment;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hg.photoshare.R;
import com.hg.photoshare.adapter.InfoWindowAdapter;
import com.hg.photoshare.api.request.NearByRequest;
import com.hg.photoshare.api.respones.NearByRespones;
import com.hg.photoshare.bean.ImageBean;
import com.hg.photoshare.bean.UserBean;
import com.hg.photoshare.contants.ErrorCodeUlti;
import com.hg.photoshare.data.NearByData;

import java.util.List;

import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;

/**
 * Created by Nart on 26/10/2016.
 */
public class NearbyFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private double latitude;
    private double longtitude;
    private String latCurrentUser;
    private String longCurrentUser;
    private String captionPost;
    private String userName;
    private List<NearByData> nearByData;
    private ImageBean imageBean;
    private UserBean userBean;
    private InfoWindowAdapter newInfo;

    private final static int CONNECTION_FAILED_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    SupportMapFragment mapFragment;

    public static NearbyFragment newInstance() {
        NearbyFragment fragment = new NearbyFragment();
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_near_by;
    }

    @Override
    protected void initView(View root) {
        setUpToolBarView(true, "Nearby", true, "", false);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext()).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frag_map);
    }

    @Override
    protected void getArgument(Bundle bundle) {
    }

    @Override
    protected void initData() {

    }

    private void requestNearBy() {
        showCoverNetworkLoading();
        NearByRequest nearByRequest = new NearByRequest(String.valueOf(latitude), String.valueOf(longtitude));
        nearByRequest.setRequestCallBack(new ApiObjectCallBack<NearByRespones>() {
            @Override
            public void onSuccess(NearByRespones respones) {
                hideCoverNetworkLoading();
                if (respones != null && respones.data.size() >= 0)
                    handleRequestSuccess(respones);
                else
                    DialogUtil.showOkBtnDialog(getContext(), "Failed !", "Please try again !");
            }

            @Override
            public void onFail(int failCode, String message) {
                hideCoverNetworkLoading();
                DialogUtil.showOkBtnDialog(getContext(), "Error : " + failCode, ErrorCodeUlti.getErrorCode(failCode));
            }
        });
        nearByRequest.execute();
    }

    private void handleRequestSuccess(final NearByRespones respones) {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                nearByData = respones.data;
                for (int i = 0; i < nearByData.size(); i++) {
                    latCurrentUser = nearByData.get(i).image.lat;
                    longCurrentUser = nearByData.get(i).image._long;
                    userName = nearByData.get(i).user.username;
                    captionPost = nearByData.get(i).image.caption;
                    imageBean = nearByData.get(i).image;
                    userBean = nearByData.get(i).user;
                    if (latCurrentUser != null && !latCurrentUser.isEmpty() || longCurrentUser != null && !longCurrentUser.isEmpty()) {
                        newInfo = new InfoWindowAdapter(getActivity());
                        googleMap.setInfoWindowAdapter(newInfo);

                        MarkerOptions mMarker = new MarkerOptions().position(new LatLng(Double.parseDouble(latCurrentUser), Double.parseDouble(longCurrentUser)));
                        mMarker.icon(BitmapDescriptorFactory.fromBitmap(resizeMarker(R.drawable.map_pin)));

                        googleMap.addMarker(mMarker.title(userName)
                                .snippet(captionPost)).setTag(nearByData);
                    }
                }
            }
        });
    }

    private Bitmap resizeMarker(int ResID) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ResID);
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 40, 70, true);
        return resized;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            latitude = location.getLatitude();
            longtitude = location.getLongitude();

            requestNearBy();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    LatLng latLng = new LatLng(latitude, longtitude);
                    CameraUpdate allTheThings = CameraUpdateFactory.newLatLngZoom(latLng, 20);
                    googleMap.moveCamera(allTheThings);
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    googleMap.setMyLocationEnabled(true);
                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                                replaceFragment(R.id.container, ImageDetailFragment.newInstance(imageBean, userBean));
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILED_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            DialogUtil.showOkBtnDialog(getContext(), "Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longtitude = location.getLongitude();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}
