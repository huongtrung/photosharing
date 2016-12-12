package com.hg.photoshare.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.hg.photoshare.R;

/**
 * Created by GMORUNSYSTEM on 12/12/2016.
 */

public class InfowindowAdapter implements GoogleMap.InfoWindowAdapter {
    Activity activity;
    TextView title;
    TextView snippet;

    public InfowindowAdapter(Activity activity) {
        this.activity = activity;

    }

    @Override
    public View getInfoWindow(Marker marker) {
        View content = activity.getLayoutInflater().inflate(R.layout.detail_marker,null);
        title = (TextView)content.findViewById(R.id.tvtitle);
        snippet = (TextView)content.findViewById(R.id.tvsnippet);
        title.setText(marker.getTitle());
        snippet.setText(marker.getSnippet());
        return content;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
