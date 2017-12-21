package com.beihui.market.ui.activity;


import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.Message;
import com.beihui.market.entity.NoticeAbstract;
import com.beihui.market.entity.SysMsgAbstract;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerMessageCenterComponent;
import com.beihui.market.injection.module.MessageCenterModule;
import com.beihui.market.ui.adapter.MessageCenterAdapter;
import com.beihui.market.ui.contract.MessageCenterContract;
import com.beihui.market.ui.presenter.MessageCenterPresenter;
import com.beihui.market.ui.rvdecoration.MessageCenterItemDeco;
import com.beihui.market.util.DateFormatUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageCenterActivity extends BaseComponentActivity implements View.OnClickListener, MessageCenterContract.View {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private MessageCenterAdapter adapter;
    private HeaderViewHolder headerViewHolder;

    @Inject
    MessageCenterPresenter presenter;

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        presenter = null;
        if (headerViewHolder.animatable.isRunning()) {
            headerViewHolder.animatable.stop();
        }
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_center;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        adapter = new MessageCenterAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Message message = (Message) adapter.getData().get(position);
                //统计次数
                DataStatisticsHelper.getInstance().onInternalMessageClicked(message.getId());

                Intent intent = new Intent(MessageCenterActivity.this, ComWebViewActivity.class);
                intent.putExtra("title", message.getTitle());
                if (message.getHttpType() == 2) {//网页
                    intent.putExtra("url", message.getUrl());
                } else {//推送内容
                    intent.putExtra("url", NetConstants.generateInternalMessageUrl(message.getId()));
                }
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MessageCenterItemDeco(this));
        View view = LayoutInflater.from(this).inflate(R.layout.layout_message_center_header, recyclerView, false);
        headerViewHolder = new HeaderViewHolder(view);
        headerViewHolder.annItemView.setOnClickListener(this);
        headerViewHolder.msgItemView.setOnClickListener(this);
        headerViewHolder.refreshNewsTv.setOnClickListener(this);
        adapter.setHeaderView(view);

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        presenter.onStart();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerMessageCenterComponent.builder()
                .appComponent(appComponent)
                .messageCenterModule(new MessageCenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ann_item) {
            Intent intent = new Intent(this, NoticeActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.msg_item) {
            Intent intent = new Intent(this, SysMsgActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.refresh_news) {
            presenter.refreshMessage();
            headerViewHolder.animatable.start();
        }
    }

    @Override
    public void setPresenter(MessageCenterContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showAnnounce(NoticeAbstract announce) {
        if (announce.getTitle() != null) {
            headerViewHolder.annContentTv.setText(announce.getTitle());
        }
        headerViewHolder.annDataTv.setText(DateFormatUtils.getCN_MDFormat().format(new Date(announce.getGmtCreate())));
    }

    @Override
    public void showSysMsg(SysMsgAbstract sysMsg) {
        if (sysMsg.getExplain() != null) {
            headerViewHolder.msgContentTv.setText(sysMsg.getExplain());
        }
        headerViewHolder.msgDateTv.setText(DateFormatUtils.getCN_MDFormat().format(new Date(sysMsg.getGmtCreate())));
    }

    @Override
    public void showMessages(List<Message> messages) {
        if (headerViewHolder.reNewsHeader.getVisibility() == View.GONE) {
            headerViewHolder.reNewsHeader.setVisibility(View.VISIBLE);
        }
        if (headerViewHolder.animatable.isRunning()) {
            headerViewHolder.animatable.stop();
        }
        adapter.notifyMessageChanged(messages);
    }

    @Override
    public void showNoMessage() {
        headerViewHolder.reNewsHeader.setVisibility(View.GONE);
        adapter.notifyMessageChanged(null);
        View footer = LayoutInflater.from(this).inflate(R.layout.layout_message_center_no_message, null);
        adapter.setFooterView(footer);
    }

    class HeaderViewHolder {
        @BindView(R.id.ann_item)
        View annItemView;
        @BindView(R.id.ann_content)
        TextView annContentTv;
        @BindView(R.id.ann_date)
        TextView annDataTv;
        @BindView(R.id.msg_item)
        View msgItemView;
        @BindView(R.id.msg_content)
        TextView msgContentTv;
        @BindView(R.id.msg_date)
        TextView msgDateTv;
        @BindView(R.id.re_news_header)
        FrameLayout reNewsHeader;
        @BindView(R.id.refresh_news)
        TextView refreshNewsTv;

        Animatable animatable;


        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
            animatable = (Animatable) refreshNewsTv.getCompoundDrawables()[0];
        }
    }
}