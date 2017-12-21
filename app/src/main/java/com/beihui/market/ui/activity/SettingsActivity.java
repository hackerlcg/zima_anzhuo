package com.beihui.market.ui.activity;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.AppUpdate;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.updatehelper.AppUpdateHelper;
import com.beihui.market.helper.updatehelper.DownloadService;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerSettingComponent;
import com.beihui.market.injection.module.SettingModule;
import com.beihui.market.ui.busevents.UserLogoutEvent;
import com.beihui.market.ui.contract.SettingContract;
import com.beihui.market.ui.dialog.CommNoneAndroidDialog;
import com.beihui.market.ui.presenter.SettingPresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.viewutils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsActivity extends BaseComponentActivity implements SettingContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.version_name)
    TextView versionNameTv;

    @Inject
    SettingPresenter presenter;

    private AppUpdateHelper updateHelper = AppUpdateHelper.newInstance();

    @Override
    protected void onDestroy() {
        updateHelper.destroy();
        presenter.onDestroy();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        presenter.onStart();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerSettingComponent.builder()
                .appComponent(appComponent)
                .settingModule(new SettingModule(this))
                .build()
                .inject(this);
    }

    @OnClick({R.id.change_psd, R.id.star_me, R.id.about_us, R.id.exit, R.id.version_container})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.change_psd:
                //umeng统计
                Statistic.onEvent(Events.SETTING_CHANGE_PASSWORD);


                Intent toChangePsd = new Intent(this, ChangePsdActivity.class);
                startActivity(toChangePsd);
                break;
            case R.id.star_me:
                try {
                    Intent toMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationInfo().packageName));
                    startActivityWithoutOverride(toMarket);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.about_us:
                Intent toAboutUs = new Intent(this, AboutUsActivity.class);
                startActivity(toAboutUs);
                break;
            case R.id.exit:
                //umeng统计
                Statistic.onEvent(Events.SETTING_EXIT);

                new CommNoneAndroidDialog().withMessage("确认退出" + getString(R.string.app_name))
                        .withPositiveBtn("再看看", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //umeng统计
                                Statistic.onEvent(Events.EXIT_DISMISS);
                            }
                        })
                        .withNegativeBtn("退出", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //umeng统计
                                Statistic.onEvent(Events.EXIT_CONFIRM);

                                presenter.logout();
                            }
                        }).show(getSupportFragmentManager(), CommNoneAndroidDialog.class.getSimpleName());
                break;
            case R.id.version_container:
                presenter.checkVersion();
                break;
        }
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showLatestVersion(String version) {
        if (version != null) {
            versionNameTv.setText(version);
        }
    }

    @Override
    public void showLogoutSuccess() {
        //发送用户退出全局事件
        EventBus.getDefault().post(new UserLogoutEvent());

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void showUpdate(AppUpdate update) {
        final AppUpdate appInfo = update;
        CommNoneAndroidDialog dialog = new CommNoneAndroidDialog()
                .withMessage(update.getContent())
                .withPositiveBtn("立即更新", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateHelper.processAppUpdate(appInfo, SettingsActivity.this);
                    }
                })
                .withNegativeBtn("稍后再说", null);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "Update");
    }

    @Override
    public void showHasBeenLatest(String msg) {
        ToastUtils.showShort(this, msg, null);
    }
}

