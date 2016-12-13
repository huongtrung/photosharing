package vn.app.base.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import vn.app.base.BaseApplication;
import vn.app.base.R;
import vn.app.base.constant.AppConstant;
import vn.app.base.util.DebugLog;
import vn.app.base.util.FragmentUtil;
import vn.app.base.util.ImagePickerUtil;
import vn.app.base.util.NetworkUtils;
import vn.app.base.util.StringUtil;
import vn.app.base.util.UiUtil;


public abstract class BaseFragment extends Fragment {
    public Uri outputFileUri;
    protected View rootView;

    protected ViewGroup fragmentViewParent;

    View initialProgressBar;

    View initialNetworkError;

    View initialEmptyList;

    View coverNetworkLoading;

    LinearLayout linearLayoutEmpty;

    TextView tvEmpty;

    protected boolean isLoading = false;

    private Unbinder unbinder;

    public View getInitialEmptyList() {
        return initialEmptyList;
    }

    public View getInitialNetworkError() {
        return initialNetworkError;
    }

    public View getInitialProgressBar() {
        return initialProgressBar;
    }

    public View getCoverNetworkLoading() {
        return coverNetworkLoading;
    }

    public TextView getTvEmpty() {
        return tvEmpty;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DebugLog.i("Lifecycle " + this.getClass().getSimpleName());
        return createRootView(inflater, container);
    }

    private View createRootView(LayoutInflater inflater, ViewGroup container) {
        if (isSkipGenerateBaseLayout()) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            unbinder = ButterKnife.bind(this, rootView);
            initCommonViews(rootView);
        } else {
            rootView = inflater.inflate(R.layout.layout_base_fragment, container, false);
            fragmentViewParent = (ViewGroup) rootView.findViewById(R.id.fragmentViewParent);
            fragmentViewParent.addView(inflater.inflate(getLayoutId(), container, false));
            unbinder = ButterKnife.bind(this, rootView);
            initCommonViews(rootView);
            bypassCommonNetworkLoadingIfNecessary();
        }
        return rootView;
    }

    protected void initCommonViews(View rootView) {
        initialProgressBar = rootView.findViewById(R.id.initialProgressBar);

        initialNetworkError = rootView.findViewById(R.id.initialNetworkError);

        initialEmptyList = rootView.findViewById(R.id.initialEmptyList);

        coverNetworkLoading = rootView.findViewById(R.id.coverNetworkLoading);

        linearLayoutEmpty = (LinearLayout) rootView.findViewById(R.id.common_layout);

        tvEmpty = (TextView) rootView.findViewById(R.id.common_txt_empty);
    }

    protected boolean isSkipGenerateBaseLayout() {
        return false;
    }

    private void bypassCommonNetworkLoadingIfNecessary() {
        if (!isStartWithLoading()) {
            initialResponseHandled();
        } else {
            initialLoadingProgress();
            isLoading = true;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            getArgument(getArguments());
        }
        initView(rootView);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (isCancelRequestOnDestroyView()) {
            NetworkUtils.getInstance(BaseApplication.getInstance()).cancelNormalRequest();
            isLoading = isLoadingMore();
        }
    }

    abstract protected int getLayoutId();

    abstract protected void initView(View root);

    abstract protected void getArgument(Bundle bundle);

    abstract protected void initData();

    protected boolean isStartWithLoading() {
        return false;
    }

    protected String setEmptyDataMessage() {
        return "";
    }

    protected boolean isCancelRequestOnDestroyView() {
        return true;
    }

    protected boolean isLoadingMore() {
        return false;
    }

    private void showAndHideOthers(View target) {
        showOrHide(initialProgressBar, target);
        showOrHide(initialNetworkError, target);
        showOrHide(initialEmptyList, target);
        showOrHide(fragmentViewParent, target);
    }

    protected void showOrHide(View subject, View target) {
        subject.setVisibility(subject == target ? View.VISIBLE : View.GONE);
    }

    protected void showCoverNetworkLoading() {
        UiUtil.showView(coverNetworkLoading);
        isLoading = true;
    }

    protected void hideCoverNetworkLoading() {
        UiUtil.hideView(coverNetworkLoading, true);
        isLoading = false;
    }

    protected void initialLoadingProgress() {
        showAndHideOthers(initialProgressBar);
    }

    protected void initialNetworkError() {
        hideCoverNetworkLoading();
        showAndHideOthers(initialNetworkError);
    }

    public void setUpToolBarView(boolean isShowBack, String title, boolean isShowTitle, String titleNav, boolean isShowTitleNav) {
        RelativeLayout btBack = (RelativeLayout) rootView.findViewById(R.id.rl_item_back);
        TextView tvTitle = (TextView) rootView.findViewById(R.id.title_item_bar);
        TextView tvTitleNav = (TextView) rootView.findViewById(R.id.title_nav_item_bar);
        if (tvTitle != null && isShowTitle) {
            StringUtil.displayText(title, tvTitle);
        }
        if (btBack != null && isShowBack) {
            UiUtil.showViewClickable(btBack);
            btBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleBack();
                }
            });
        }
        if (tvTitleNav != null && isShowTitleNav) {
            StringUtil.displayText(titleNav, tvTitleNav);
            UiUtil.showViewClickable(tvTitleNav);

        }
    }

    protected void handleBack() {
        int backStackCnt = getFragmentManager().getBackStackEntryCount();
        if (backStackCnt > 1) {
            getFragmentManager().popBackStack();
        } else {
            getActivity().onBackPressed();
        }
    }

    public void replaceFragment(int container, Fragment fragment) {
        replaceFragment(container, fragment, null, true);
    }

    public void replaceFragment(int container, Fragment fragment, String tag, boolean isAddToBackStack) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        if (tag != null) {
            Fragment f = manager.findFragmentByTag(tag);
            if (f != null) {
                manager.popBackStack();
            }
        }
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(container, fragment, tag);
        if (isAddToBackStack) {
            transaction.addToBackStack(tag);
        }
        int id = transaction.commit();
    }


    public Uri getPhotoFileUri(String fileName) {
        if (isExternalStorageAvailable()) {
            File mediaStoreDir = new File
                    (getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Picture");
            if (!mediaStoreDir.exists() && !mediaStoreDir.mkdir()) {
            }
            return Uri.fromFile(new File(mediaStoreDir.getPath() + File.separator + fileName + File.separator));
        }
        return null;
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    protected void initialEmptyList() {
        hideCoverNetworkLoading();
        showAndHideOthers(initialEmptyList);
        linearLayoutEmpty.setGravity(Gravity.CENTER);
        tvEmpty.setText(setEmptyDataMessage());
    }

    protected void initialResponseHandled() {
        hideCoverNetworkLoading();
        showAndHideOthers(fragmentViewParent);
    }

    protected boolean checkFragmentVisible() {
        if (isVisible() && getActivity() != null) {
            return true;
        } else {
            return false;
        }
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    public File savebitmap(Bitmap bmp) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        // String temp = null;
        File file = new File(extStorageDirectory, bmp + ".png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, bmp + ".png");
        }

        try {
            outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    public static File creatFilefromBitmap(Bitmap bitmap) throws IOException {
        File imageAvatar;
        File imageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/InstagramFaker");
        imageDir.mkdir();
        imageAvatar = new File(imageDir, "avatar.jpg");
        OutputStream fOut = new FileOutputStream(imageAvatar);
        Bitmap getBitmap = bitmap;
        getBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        fOut.flush();
        fOut.close();
        return imageAvatar;
    }

//    protected void clearBackStack() {
//        ((CommonActivity) getActivity()).popEntireFragmentBackStack();
//    }
}

