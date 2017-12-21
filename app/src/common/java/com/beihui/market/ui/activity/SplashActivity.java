package com.beihui.market.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.request.RequestConstants;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerSplashComponent;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;
import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseComponentActivity {
    @BindView(R.id.ad_image)
    ImageView adImageView;
    @BindView(R.id.ignore)
    TextView ignoreTv;
    @BindView(R.id.bottom_logo)
    ImageView bottomLogoIv;

    private AdTimer adTimer;

    @Inject
    Api api;

    private Disposable disposable;

    private boolean adClicked;

    private SplashHandler handler;

    @Override
    protected void onDestroy() {
        if (adTimer != null) {
            adTimer.cancel();
        }
        adTimer = null;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        handler.removeMessages(1);
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void configViews() {
        int padding = (int) getResources().getDisplayMetrics().density * 35;
        bottomLogoIv.setPadding(0, padding, 0, padding);
        ignoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(null);
            }
        });
    }

    @Override
    public void initDatas() {
        handler = new SplashHandler(this);
        Message msg = Message.obtain();
        msg.what = 1;
        handler.sendMessageDelayed(msg, 1000 * 5);

        try {
            String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            if (version.equals(SPUtils.getLastInstalledVersion(this))) {
                checkAd();
            } else {
                handler.removeMessages(1);
                SPUtils.setLastInstalledVersion(this, version);
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            launch(null);
        }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerSplashComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    private void launch(String url) {
        handler.removeMessages(1);

        if (!adClicked) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
            finish();
        }
    }

    private void checkAd() {
        disposable = api.querySupernatant(RequestConstants.SUP_TYPE_AD)
                .compose(RxUtil.<ResultEntity<List<AdBanner>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<AdBanner>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<AdBanner>> result) throws Exception {
                                   handler.removeMessages(1);

                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           startAd(result.getData().get(0));
                                       } else {
                                           launch(null);
                                       }
                                   } else {
                                       launch(null);
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                launch(null);
                            }
                        });
    }

    private void startAd(AdBanner ad) {
        final AdBanner adBanner = ad;
        ignoreTv.setVisibility(View.VISIBLE);
        adTimer = new AdTimer(5 * 1000, 1000);
        adTimer.start();

        if (ad != null && ad.getImgUrl() != null) {
            adImageView.setVisibility(View.VISIBLE);
            adImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adClicked = true;
                    Context context = SplashActivity.this;
                    //统计点击
                    DataStatisticsHelper.getInstance().onAdClicked(adBanner.getId(), adBanner.getType());
                    //先跳转至首页，在跳转至广告页
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);

                    //跳Native还是跳Web
                    if (adBanner.isNative()) {
                        intent = new Intent(context, LoanDetailActivity.class);
                        intent.putExtra("loanId", adBanner.getLocalId());
                        intent.putExtra("loanName", adBanner.getTitle());
                        startActivity(intent);
                    } else {
                        intent = new Intent(context, ComWebViewActivity.class);
                        intent.putExtra("url", adBanner.getUrl());
                        intent.putExtra("title", adBanner.getTitle());
                        startActivity(intent);
                    }
                    finish();
                }
            });

            Glide.with(this)
                    .load(ad.getImgUrl())
                    .asBitmap()
                    .into(adImageView);
        }
    }

    @Override
    public void finish() {
        override = false;
        super.finish();
    }

    private class AdTimer extends CountDownTimer {

        AdTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            ignoreTv.setText((millisUntilFinished / 1000) + "  跳过");
        }

        @Override
        public void onFinish() {
            launch(null);
        }
    }

    private static class SplashHandler extends Handler {
        private WeakReference<SplashActivity> weakReference;

        SplashHandler(SplashActivity splashActivity) {
            super(Looper.getMainLooper());
            weakReference = new WeakReference<>(splashActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && weakReference.get() != null) {
                weakReference.get().launch(null);
            }
        }
    }
}
