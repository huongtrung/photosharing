package com.hg.photoshare.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hg.photoshare.R;
import com.hg.photoshare.adapter.TutorialAdapter;
import com.hg.photoshare.api.request.TutorialRequest;
import com.hg.photoshare.api.respones.RegisterResponse;
import com.hg.photoshare.api.respones.TutorialResponse;
import com.hg.photoshare.bean.TutorialBean;
import com.hg.photoshare.data.TutorialData;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import vn.app.base.api.volley.callback.ApiObjectCallBack;
import vn.app.base.fragment.BaseFragment;
import vn.app.base.util.DialogUtil;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.KeyboardUtil;
import vn.app.base.util.SharedPrefUtils;
import vn.app.base.util.StringUtil;

/**
 * Created by Nart on 24/10/2016.
 */
public class ToturialFragment extends BaseFragment {
    public static final String KEY_AGREE = "key_agree";
    @BindView(R.id.bt_skip)
    Button btSkip;
    @BindView(R.id.vp_tutorial)
    ViewPager vpTutorial;
    @BindView(R.id.tut_indicator)
    CircleIndicator indicator;

    TutorialData tutorialData;
    TutorialAdapter tutorialAdapter;

    @OnClick(R.id.bt_skip)
    public void goHome(){
        FragmentUtil.pushFragment(getActivity(),HomeFragment.newInstance(),null);
    }

    public static ToturialFragment newInstance() {
        ToturialFragment fragment = new ToturialFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_toturial;
    }

    @Override
    protected boolean isStartWithLoading() {
        return tutorialData==null;
    }

    @Override
    protected void initView(View root) {
        SharedPrefUtils.putBoolean(KEY_AGREE, true);
    }

    private void getDatatutorial() {
        TutorialRequest tutorialRequest = new TutorialRequest();
        tutorialRequest.setRequestCallBack(new ApiObjectCallBack<TutorialResponse>() {
            @Override
            public void onSuccess(TutorialResponse data) {
                hideCoverNetworkLoading();
                initialResponseHandled();
                handleTutData(data.data);

            }
            @Override
            public void onFail(int failCode, String message) {
                hideCoverNetworkLoading();
                DialogUtil.showOkBtnDialog(getContext(), "Error : " +failCode, message);
            }

        });

        tutorialRequest.execute();

    }



    private void handleTutData(TutorialData getData) {

        TutorialAdapter tutorialAdapter = new TutorialAdapter(getChildFragmentManager(), getData);

        vpTutorial.setAdapter(tutorialAdapter);

        indicator.setViewPager(vpTutorial);

    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    protected void initData() {
        if (tutorialData==null){
            getDatatutorial();
        }
        else {
            handleTutData(tutorialData);
        }
    }

}
