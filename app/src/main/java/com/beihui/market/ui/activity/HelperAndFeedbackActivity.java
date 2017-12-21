package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.NetConstants;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerFeedbackComponent;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.LogUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.viewutils.ToastUtils;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class HelperAndFeedbackActivity extends BaseComponentActivity implements View.OnClickListener {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.help)
    TextView helpTv;
    @BindView(R.id.feedback)
    TextView feedbackTv;

    @BindView(R.id.helper_container)
    View helperContainer;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.feedback_container)
    View feedbackContainer;
    @BindView(R.id.content)
    EditText contentEt;
    @BindView(R.id.length)
    TextView lengthTv;
    @BindView(R.id.submit)
    TextView submitBtn;

    @Inject
    Api api;

    private Disposable disposable;

    private View selected;

    @Override
    protected void onDestroy() {
        webView.getSettings().setJavaScriptEnabled(false);
        webView.destroy();
        webView = null;

        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_helper_and_feedback;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void configViews() {
        setupToolbar(toolbar);
        helpTv.setOnClickListener(this);
        feedbackTv.setOnClickListener(this);
        webView.setWebChromeClient(new ChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);

        contentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = contentEt.getText().length();
                lengthTv.setText(len + "/300");
                submitBtn.setEnabled(len > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        lengthTv.setText("0/300");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disposable = api.submitFeedback(UserHelper.getInstance(getApplicationContext()).getProfile().getId(),
                        contentEt.getText().toString())
                        .compose(RxUtil.<ResultEntity>io2main())
                        .subscribe(new Consumer<ResultEntity>() {
                                       @Override
                                       public void accept(@NonNull ResultEntity result) throws Exception {
                                           if (result.isSuccess()) {
                                               ToastUtils.showShort(HelperAndFeedbackActivity.this,
                                                       result.getMsg(), null);
                                               submitBtn.postDelayed(new Runnable() {
                                                   @Override
                                                   public void run() {
                                                       finish();
                                                   }
                                               }, 200);
                                           } else {
                                               ToastUtils.showShort(HelperAndFeedbackActivity.this,
                                                       result.getMsg(), null);
                                           }
                                       }
                                   },
                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(@NonNull Throwable throwable) throws Exception {
                                        LogUtils.e("Feedback", "error " + throwable);
                                        ToastUtils.showShort(HelperAndFeedbackActivity.this,
                                                "请求出错了", null);
                                    }
                                });
            }
        });

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        webView.loadUrl(NetConstants.H5_HELPER);
        select(helpTv);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerFeedbackComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void onClick(View v) {
        if (v != selected) {
            select(v);
        }
    }

    @Override
    public void finish() {
        InputMethodUtil.closeSoftKeyboard(this);
        super.finish();
    }

    private void select(View view) {
        if (selected != null) {
            selected.setSelected(false);
        }
        selected = view;
        if (selected != null) {
            selected.setSelected(true);
        }
        helperContainer.setVisibility(view == helpTv ? View.VISIBLE : View.GONE);
        feedbackContainer.setVisibility(view == feedbackTv ? View.VISIBLE : View.GONE);

        if (feedbackContainer.getVisibility() == View.VISIBLE) {
            contentEt.requestFocus();
            InputMethodUtil.openSoftKeyboard(this, contentEt);
        } else {
            InputMethodUtil.closeSoftKeyboard(this, contentEt);
            contentEt.clearFocus();
        }
    }


    private class ChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }
}
