package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutUsActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.version_name)
    TextView versionNameTv;


    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void configViews() {
        setupToolbar(toolbar);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionNameTv.setText("v" + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @OnClick({R.id.welcome, R.id.get_know_us, R.id.user_agreement})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.welcome:
                Intent toWelCome = new Intent(this, WelcomeActivity.class);
                toWelCome.putExtra("fromAboutUs", true);
                startActivity(toWelCome);
                break;
            case R.id.get_know_us:
            case R.id.user_agreement:
                Intent intent = new Intent(this, ComWebViewActivity.class);
                String title = view.getId() == R.id.get_know_us ? "了解" + getString(R.string.app_name) : "用户协议";
                String url = view.getId() == R.id.get_know_us ? NetConstants.H5_ABOUT_US : NetConstants.H5_USER_AGREEMENT;
                intent.putExtra("title", title);
                intent.putExtra("url", url);
                startActivity(intent);
                break;
        }
    }
}
