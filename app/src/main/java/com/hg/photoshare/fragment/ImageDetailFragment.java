package com.hg.photoshare.fragment;

import android.os.Bundle;
import android.view.View;

import com.hg.photoshare.R;

import butterknife.OnClick;
import vn.app.base.fragment.BaseFragment;

/**
 * Created by Nart on 26/10/2016.
 */
public class ImageDetailFragment extends BaseFragment {

    public static ImageDetailFragment newInstance() {
        ImageDetailFragment fragment = new ImageDetailFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_detail;
    }

    @Override
    protected void initView(View root) {
        setUpToolBarView(true, "Image Detail", true, "Delete", true);
    }

    @Override
    protected void getArgument(Bundle bundle) {
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.title_nav_item_bar)
    public void delete() {

    }
}
