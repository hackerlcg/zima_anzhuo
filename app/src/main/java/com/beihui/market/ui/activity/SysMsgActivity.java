package com.beihui.market.ui.activity;


import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.SysMsg;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerSysMsgComponent;
import com.beihui.market.injection.module.SysMsgModule;
import com.beihui.market.ui.adapter.SysMsgAdapter;
import com.beihui.market.ui.contract.SysMsgContract;
import com.beihui.market.ui.presenter.SysMsgPresenter;
import com.beihui.market.ui.rvdecoration.NewsItemDeco;
import com.beihui.market.view.StateLayout;
import com.beihui.market.view.stateprovider.MessageStateViewProvider;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class SysMsgActivity extends BaseComponentActivity implements SysMsgContract.View {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.state_layout)
    StateLayout stateLayout;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private SysMsgAdapter adapter;

    @Inject
    SysMsgPresenter presenter;

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sys_msg;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        adapter = new SysMsgAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SysMsgActivity.this, SysMsgDetailActivity.class);
                intent.putExtra("sysMsg", (SysMsg.Row) adapter.getData().get(position));
                startActivity(intent);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.loadMore();
            }
        }, recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        float density = getResources().getDisplayMetrics().density;
        int padding = (int) (density * 7);
        recyclerView.addItemDecoration(new NewsItemDeco((int) (density * 0.5), padding, padding));

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onStart();
            }
        });

        stateLayout.setStateViewProvider(new MessageStateViewProvider());

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        refreshLayout.setRefreshing(true);
        presenter.onStart();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerSysMsgComponent.builder()
                .appComponent(appComponent)
                .sysMsgModule(new SysMsgModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(SysMsgContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showSysMsg(List<SysMsg.Row> sysMsg) {
        stateLayout.switchState(StateLayout.STATE_CONTENT);
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        if (adapter.isLoading()) {
            adapter.loadMoreComplete();
        }
        adapter.notifySysMsgChanged(sysMsg);
    }

    @Override
    public void showNoSysMsg() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        stateLayout.switchState(StateLayout.STATE_EMPTY);
    }

    @Override
    public void showNoMoreSysMsg() {
        adapter.loadMoreEnd(true);
    }

    @Override
    public void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        if (adapter != null && adapter.isLoading()) {
            adapter.loadMoreEnd(true);
        }
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }
}
