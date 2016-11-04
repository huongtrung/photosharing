package com.hg.photoshare.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hg.photoshare.R;
import com.hg.photoshare.adapter.HomeNewAdapter;
import com.hg.photoshare.data.HomeData;

import java.util.List;

import butterknife.BindView;
import vn.app.base.fragment.BaseFragment;

/**
 * Created by Nart on 25/10/2016.
 */
public class HomeNewFragment extends BaseFragment {

    @BindView(R.id.rc_home_follow)
    RecyclerView rcHomeFollow;
    HomeNewAdapter mHomeNewAdapter;
    List<HomeData> homeDataList;
    public static HomeNewFragment newInstance() {
        HomeNewFragment fragment  = new HomeNewFragment();
        Bundle bundle=new Bundle();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_new;
    }

    @Override
    protected void initView(View root) {
        mHomeNewAdapter=new HomeNewAdapter(homeDataList);
        rcHomeFollow.setAdapter(mHomeNewAdapter);
        rcHomeFollow.setLayoutManager(new LinearLayoutManager(getContext()));
        mHomeNewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    protected void initData() {

    }
}
