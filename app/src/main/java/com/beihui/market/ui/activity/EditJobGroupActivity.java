package com.beihui.market.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.Profession;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerEditProfessionComponent;
import com.beihui.market.injection.module.EditProfessionModule;
import com.beihui.market.ui.contract.EditProfessionContract;
import com.beihui.market.ui.presenter.EditProfessionPresenter;
import com.beihui.market.util.viewutils.ToastUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class EditJobGroupActivity extends BaseComponentActivity implements EditProfessionContract.View, View.OnClickListener {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.profession_container)
    LinearLayout professionContainer;
    private View curSelectedView;

    @Inject
    EditProfessionPresenter presenter;

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_job_group;
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
        DaggerEditProfessionComponent.builder()
                .appComponent(appComponent)
                .editProfessionModule(new EditProfessionModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onClick(View view) {
        if (view != curSelectedView) {
            if (curSelectedView != null) {
                curSelectedView.setSelected(false);
            }
            curSelectedView = view;
            curSelectedView.setSelected(true);

            presenter.updateProfession((Profession) view.getTag());
        }
    }

    @Override
    public void setPresenter(EditProfessionContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showProfession(List<Profession> professions) {
        if (professions != null && professions.size() > 0) {
            LayoutInflater inflater = LayoutInflater.from(this);
            for (int i = 0, count = professions.size(); i < count; ++i) {
                Profession pro = professions.get(i);
                TextView view = (TextView) inflater.inflate(R.layout.rv_item_profession, professionContainer, false);
                professionContainer.addView(view);
                view.setText(pro.getText());
                view.setOnClickListener(this);

                view.setTag(pro);

                if (pro.getSelected() != 0) {
                    view.setSelected(true);
                    curSelectedView = view;
                }
            }
        }
    }

    @Override
    public void showErrorMsg(String msg) {
        dismissProgress();
        ToastUtils.showShort(this, msg, null);
    }

    @Override
    public void showUpdateSuccess(String msg) {
        dismissProgress();
        ToastUtils.showShort(this, msg, null);
        finish();
    }
}
