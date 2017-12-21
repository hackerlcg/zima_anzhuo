package com.beihui.market.ui.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.Invitation;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerInvitationComponent;
import com.beihui.market.injection.module.InvitationModule;
import com.beihui.market.ui.adapter.InvitationAdapter;
import com.beihui.market.ui.contract.InvitationContract;
import com.beihui.market.ui.dialog.ShareDialog;
import com.beihui.market.ui.presenter.InvitationPresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class InvitationActivity extends BaseComponentActivity implements InvitationContract.View {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.invitation_code)
    TextView invitationCodeTv;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.invite)
    TextView inviteBtn;

    private InvitationAdapter adapter;

    @Inject
    InvitationPresenter presenter;

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_invitation;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        adapter = new InvitationAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //umeng统计
                Statistic.onEvent(Events.INVITATION_INVITE);

                UMWeb umWeb = new UMWeb(NetConstants.generateInvitationUrl(UserHelper.getInstance(InvitationActivity.this).getProfile().getId()));
                umWeb.setTitle("告诉你一个手机借款神器");
                umWeb.setDescription("急用钱？秒到账！超给力新口子，下款快，额度高，注册极简.");
                UMImage image = new UMImage(InvitationActivity.this, R.mipmap.ic_launcher);
                umWeb.setThumb(image);
                new ShareDialog()
                        .setUmWeb(umWeb)
                        .show(getSupportFragmentManager(), ShareDialog.class.getSimpleName());
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
        DaggerInvitationComponent.builder()
                .appComponent(appComponent)
                .invitationModule(new InvitationModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(InvitationContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showInvitationCode(String code) {
        invitationCodeTv.setText(code);
    }

    @Override
    public void showInvitations(List<Invitation.Row> list) {
        adapter.notifyInvitationChanged(list);
    }
}
