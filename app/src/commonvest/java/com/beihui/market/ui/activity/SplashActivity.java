package com.beihui.market.ui.activity;


import android.content.Intent;
import android.view.ViewGroup;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;

import butterknife.BindView;

public class SplashActivity extends BaseComponentActivity {

    @BindView(R.id.base_container)
    ViewGroup baseContainer;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void configViews() {
    }

    @Override
    public void initDatas() {
        baseContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivityWithoutOverride(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 1000);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }


    @Override
    public void finish() {
        override = false;
        super.finish();
    }
}
