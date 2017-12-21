package com.beihui.market.ui.dialog;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.beihui.market.BuildConfig;
import com.beihui.market.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ShareDialog extends DialogFragment {

    private static final String TAG = ShareDialog.class.getSimpleName();

    Unbinder unbinder;

    private UMWeb umWeb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CommonBottomDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_share, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(true);
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setAttributes(lp);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @SuppressLint("InlinedApi")
    @OnClick({R.id.share_wechat, R.id.share_wechat_moment, R.id.share_qq, R.id.share_weibo, R.id.cancel})
    void OnViewClicked(View view) {
        switch (view.getId()) {
            case R.id.share_wechat:
                shareWeb(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.share_wechat_moment:
                shareWeb(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.share_qq:
                //qq分享需要存储权限
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    this.requestPermissions(permission, 1);
                } else {
                    shareWeb(SHARE_MEDIA.QQ);
                }
                break;
            case R.id.share_weibo:
                shareWeb(SHARE_MEDIA.SINA);
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }

    public ShareDialog setUmWeb(UMWeb umWeb) {
        this.umWeb = umWeb;
        return this;
    }

    private void shareWeb(SHARE_MEDIA media) {
        dismiss();
        if (umWeb == null) {
            throw new IllegalStateException("未设置分享内容 ");
        }
        new ShareAction(getActivity()).withMedia(this.umWeb).setPlatform(media).setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "share action start");
                }
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "share action result " + share_media);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "share action error " + share_media + " throwable " + throwable);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "share action cancel " + share_media);
                }
            }
        }).share();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                shareWeb(SHARE_MEDIA.QQ);
            }

        }
    }
}
