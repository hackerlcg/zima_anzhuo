package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.Notice;
import com.beihui.market.entity.NoticeDetail;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerNoticeDetailComponent;
import com.beihui.market.injection.module.NoticeDetailModule;
import com.beihui.market.ui.contract.NoticeDetailContract;
import com.beihui.market.ui.presenter.NoticeDetailPresenter;
import com.beihui.market.util.DateFormatUtils;

import javax.inject.Inject;

import butterknife.BindView;

public class NoticeDetailActivity extends BaseComponentActivity implements NoticeDetailContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView titleTv;
    @BindView(R.id.date)
    TextView dateTv;
    @BindView(R.id.read_sum)
    TextView readSumTv;
    @BindView(R.id.web_view)
    WebView webView;

    @Inject
    NoticeDetailPresenter presenter;

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        presenter = null;
        webView.destroy();
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_notice_detail;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void configViews() {
        setupToolbar(toolbar);

        webView.setWebViewClient(new WebViewClient());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        Notice.Row notice = getIntent().getParcelableExtra("notice");
        String noticeId = getIntent().getStringExtra("noticeId");
        if (notice != null) {
            if (notice.getId() != null) {
                presenter.queryDetail(notice.getId());
            }
        } else if (noticeId != null) {
            presenter.queryDetail(noticeId);
        }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerNoticeDetailComponent.builder()
                .appComponent(appComponent)
                .noticeDetailModule(new NoticeDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(NoticeDetailContract.Presenter presenter) {
        //injected.nothing to do;
    }

    @Override
    public void showDetail(NoticeDetail detail) {
        if (detail != null) {
            if (detail.getTitle() != null) {
                titleTv.setText(detail.getTitle());
            }
            dateTv.setText(DateFormatUtils.formatMMddHHmm(detail.getGmtCreate()));
            readSumTv.setText("阅读 " + detail.getReadSum());
            if (detail.getContent() != null) {
                webView.loadData(detail.getContent(), "text/html;charset=UTF-8", null);
            }
        }
    }
}
