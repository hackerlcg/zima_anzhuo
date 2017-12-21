package com.beihui.market.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beihui.market.App;
import com.beihui.market.R;
import com.beihui.market.getui.GeTuiClient;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.dialog.CommNoneAndroidLoading;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.viewutils.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;


public abstract class BaseComponentActivity extends BaseActivity {


    protected CommNoneAndroidLoading loading;


    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        configureComponent(App.getInstance().getAppComponent());

        super.onCreate(savedInstanceState);
        //初始化个推
        GeTuiClient.install(this);

        setContentView(getLayoutId());

        ImmersionBar.with(this).init();
        if (savedInstanceState != null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStackImmediate(null, 1);
        }

        ButterKnife.bind(this);
        configViews();
        initDatas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Statistic.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Statistic.onPause(this);
    }

    public abstract int getLayoutId();

    /**
     * 对各种控件进行设置、适配、填充数据
     */
    public abstract void configViews();

    public abstract void initDatas();


    protected abstract void configureComponent(AppComponent appComponent);


    protected void showProgress(String msg) {
        if (loading == null) {
            loading = new CommNoneAndroidLoading(this, msg);
        }
        loading.show();
    }

    protected void dismissProgress() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    /**
     * set tool bar render and behavior with default action
     *
     * @param toolbar target to set up
     */
    protected void setupToolbar(Toolbar toolbar) {
        setupToolbar(toolbar, true);
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_white);
    }

    /**
     * helper method for render standard tool with status bar
     *
     * @param toolBar       target to be set up
     * @param withStatusBar true if status bar should render with tool bar
     */
    protected void setupToolbar(Toolbar toolBar, boolean withStatusBar) {
        setSupportActionBar(toolBar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (withStatusBar) {
            ImmersionBar.with(this).titleBar(toolBar).init();
        }
    }

    /**
     * helper method for setting up navigation
     */
    protected void setupToolbarBackNavigation(Toolbar toolbar, int navigationIcon) {
        toolbar.setNavigationIcon(navigationIcon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * hook BaseView.showLoading
     */
    public void showLoading() {
        showProgress(null);
    }

    /**
     * hook BaseView.showErrorMsg(String).
     */
    public void showErrorMsg(String msg) {
        dismissProgress();
        ToastUtils.showShort(this, msg, null);
    }

    //解决崩溃后重新打开程序，fragment 重叠问题
    //当前APP崩溃再次启动或者从后台再次回到这个app的时候，通过onCreate中的参数savedInstanceState恢复了之前的fragment。
    // 此时的FragmentTransaction中的相当于又再次add了fragment进去的，之前保存的fragment也还在。hide()和show()方法对
    // 之前保存的fragment已经失效了。所以出现了重叠的现象
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
        //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ImmersionBar.with(this).destroy();
    }
}
