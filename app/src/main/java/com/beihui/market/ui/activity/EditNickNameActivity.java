package com.beihui.market.ui.activity;

import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerEditUserNameComponent;
import com.beihui.market.injection.module.EditUserNameModule;
import com.beihui.market.ui.contract.EditUserNameContract;
import com.beihui.market.ui.presenter.EditUserNamePresenter;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.viewutils.ToastUtils;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class EditNickNameActivity extends BaseComponentActivity implements EditUserNameContract.View {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.confirm)
    TextView confirmBtn;

    @Inject
    EditUserNamePresenter presenter;

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_nick_name;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source != null) {
                    try {
                        String destStr = dest.toString();
                        String srcStr = source.toString();
                        String result = srcStr + destStr;
                        byte[] bytes = result.getBytes("GBK");
                        //10个中文或者20个字符
                        if (bytes.length > 20) {
                            int srcEnd = srcStr.length() - 1;
                            bytes = (destStr + srcStr.substring(0, srcEnd)).getBytes("GBk");
                            while (bytes.length > 20) {
                                srcEnd--;
                                bytes = (destStr + srcStr.substring(0, srcEnd)).getBytes("GBk");
                            }
                            return srcStr.substring(0, srcEnd);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };

        InputFilter[] filters = editText.getFilters();
        if (filters == null) {
            filters = new InputFilter[]{filter};
        } else {
            InputFilter[] temp = filters;
            filters = new InputFilter[temp.length + 1];
            System.arraycopy(temp, 0, filters, 0, temp.length);
            filters[temp.length] = filter;
        }
        editText.setFilters(filters);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmBtn.setEnabled(editText.getText().toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        presenter.onStart();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerEditUserNameComponent.builder()
                .appComponent(appComponent)
                .editUserNameModule(new EditUserNameModule(this))
                .build()
                .inject(this);
    }

    @OnClick(R.id.confirm)
    void onViewClicked() {
        presenter.updateUserName(editText.getText().toString());
    }

    @Override
    public void setPresenter(EditUserNameContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showErrorMsg(String msg) {
        dismissProgress();
        ToastUtils.showShort(this, msg, null);
    }

    @Override
    public void showUserName(String name) {
        editText.setText(name);
        editText.setSelection(editText.getText().length());
    }

    @Override
    public void showUpdateNameSuccess(String msg) {
        dismissProgress();
        ToastUtils.showShort(this, msg, null);
        finish();
    }

    @Override
    public void finish() {
        InputMethodUtil.closeSoftKeyboard(this);
        super.finish();
    }
}
