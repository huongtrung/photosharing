package com.hg.photoshare.fragment;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hg.photoshare.R;

import vn.app.base.fragment.BaseFragment;

/**
 * Created by Nart on 26/10/2016.
 */
public class NearbyFragment extends BaseFragment implements GoogleMap.OnInfoWindowClickListener {

    private double latitude;
    private double longtitude;

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

        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frag_map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng latLng = new LatLng(latitude, longtitude);
                CameraUpdate allTheThings = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                googleMap.moveCamera(allTheThings);
                googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longtitude)).title("Your position"));
            }
        });

    }

    @Override
    protected void getArgument(Bundle bundle) {
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}
