package com.beihui.market.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.base.Constant;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.HotNews;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.entity.NoticeAbstract;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerTabHomeComponent;
import com.beihui.market.injection.module.TabHomeModule;
import com.beihui.market.ui.activity.ComWebViewActivity;
import com.beihui.market.ui.activity.LoanDetailActivity;
import com.beihui.market.ui.activity.MessageCenterActivity;
import com.beihui.market.ui.activity.NewsDetailActivity;
import com.beihui.market.ui.activity.NoticeDetailActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.activity.WorthTestActivity;
import com.beihui.market.ui.adapter.HotNewsAdapter;
import com.beihui.market.ui.adapter.LoanRVAdapter;
import com.beihui.market.ui.busevents.NavigateLoan;
import com.beihui.market.ui.busevents.NavigateNews;
import com.beihui.market.ui.contract.TabHomeContract;
import com.beihui.market.ui.dialog.AdDialog;
import com.beihui.market.ui.presenter.TabHomePresenter;
import com.beihui.market.ui.rvdecoration.HomeItemDeco;
import com.beihui.market.ui.rvdecoration.HotNewsItemDeco;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.FastClickUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.SPUtils;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.AutoTextView;
import com.beihui.market.view.StateLayout;
import com.beihui.market.view.stateprovider.HomeStateViewProvider;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class TabHomeFragment extends BaseTabFragment implements View.OnClickListener, TabHomeContract.View {
    @BindView(R.id.root_container)
    FrameLayout rootContainer;
    @BindView(R.id.faked_bar)
    View fakedBar;
    @BindView(R.id.tool_bar_container)
    View toolBarContainer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.state_layout)
    StateLayout stateLayout;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.center_text)
    TextView center_text;

    @BindView(R.id.notice_container)
    FrameLayout noticeContainer;
    @BindView(R.id.notice_close)
    ImageView noticeCloseIv;
    @BindView(R.id.notice_text)
    AutoTextView noticeTv;

    @Inject
    TabHomePresenter presenter;

    private LoanRVAdapter loanRVAdapter;

    //记录顶部banner的高度
    private int bannerHeight;
    //status and tool bar render
    public float toolBarBgAlpha;

    private HeaderViewHolder headerViewHolder;

    //记录输入的钱数
    private int inputMoney = Constant.DEFAULT_FILTER_MONEY;

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        int scrollY;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            scrollY += dy;
            int maxMove = bannerHeight / 2;
            renderStatusAndToolBar(scrollY / (float) maxMove);
        }

    };

    public static TabHomeFragment newInstance() {
        return new TabHomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //umeng统计
        Statistic.onEvent(Events.ENTER_HOME_PAGE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (headerViewHolder != null) {
            headerViewHolder.banner.startAutoPlay();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        headerViewHolder.banner.stopAutoPlay();
    }

    @Override
    public void onDestroyView() {
        headerViewHolder.destroy();
        headerViewHolder = null;
        presenter.onDestroy();
        super.onDestroyView();
    }

    void renderStatusAndToolBar(float alpha) {
        toolBarBgAlpha = alpha;
        int alphaInt = (int) (alpha * 255);
        alphaInt = alphaInt < 10 ? 0 : alphaInt;
        alphaInt = alphaInt > 255 ? 255 : alphaInt;
        toolBarContainer.setBackgroundColor(Color.argb(alphaInt, 85, 145, 255));
        center_text.setTextColor(Color.argb(alphaInt, 255, 255, 255));
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_home;
    }

    @Override
    public void configViews() {
        stateLayout.setStateViewProvider(new HomeStateViewProvider(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onStart();
            }
        }));
        noticeCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeContainer.setVisibility(View.GONE);
                SPUtils.setNoticeClosed(getContext(), true);
            }
        });
        ((AppCompatActivity) getActivity()).setSupportActionBar(    toolbar);
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        loanRVAdapter = new LoanRVAdapter();
        loanRVAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), LoanDetailActivity.class);
                intent.putExtra("loan", (LoanProduct.Row) adapter.getItem(position));
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnScrollListener(onScrollListener);
        recyclerView.setAdapter(loanRVAdapter);
        recyclerView.addItemDecoration(new HomeItemDeco());
        LayoutInflater inflater =
                (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headerView =
                inflater.inflate(R.layout.layout_tab_home_rv_header, recyclerView, false);
        headerViewHolder = new HeaderViewHolder(headerView);

        headerViewHolder.worthTest.setOnClickListener(this);
        headerViewHolder.lyEtbg.setOnClickListener(this);
        headerViewHolder.tvTuiJian.setOnClickListener(this);
        headerViewHolder.tvMore.setOnClickListener(this);
        headerViewHolder.etMoney.setText(inputMoney + "");
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (dstart == 0 && !TextUtils.isEmpty(source)) {
                    if (source.equals("0")) {
                        return "";
                    }
                }
                return null;
            }
        };
        InputFilter[] filters = headerViewHolder.etMoney.getFilters();
        if (filters == null) {
            filters = new InputFilter[]{filter};
        } else {
            InputFilter[] temp = filters;
            filters = new InputFilter[temp.length + 1];
            for (int i = 0; i < temp.length; ++i) {
                filters[i] = temp[i];
            }
            filters[temp.length] = filter;
        }
        headerViewHolder.etMoney.setFilters(filters);

        loanRVAdapter.setHeaderView(headerView);

        int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        ViewGroup.LayoutParams lp = fakedBar.getLayoutParams();
        lp.height = statusHeight;
        fakedBar.setLayoutParams(lp);
        renderStatusAndToolBar(toolBarBgAlpha);

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });
    }

    @Override
    public void initDatas() {
        refreshLayout.setRefreshing(true);
        presenter.onStart();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerTabHomeComponent.builder()
                .appComponent(appComponent)
                .tabHomeModule(new TabHomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.worth_test:
                //umeng统计
                Statistic.onEvent(Events.HOME_CLICK_TEST);

                if (!FastClickUtils.isFastClick()) {
                    presenter.checkMyWorth();
                }
                break;
            case R.id.ly_etbg:
                InputMethodUtil.openSoftKeyboard(getActivity(), headerViewHolder.etMoney);
                break;
            case R.id.tv_tuijian:
                //umeng统计
                Statistic.onEvent(Events.HOME_CLICK_RECOMMEND);

                String moneyStr = headerViewHolder.etMoney.getText().toString();
                if (TextUtils.isEmpty(moneyStr)) {
                    ToastUtils.showShort(getContext(), "请输入金额", null);
                    return;
                }
                try {
                    inputMoney = Integer.parseInt(moneyStr);
                    if (inputMoney < Constant.MIN_FILTER_MONEY) {
                        ToastUtils.showShort(getContext(), "最低借款" + Constant.MIN_FILTER_MONEY + "元", null);
                        return;
                    }
                    EventBus.getDefault().post(new NavigateLoan(inputMoney));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ToastUtils.showShort(getContext(), "请输入正确金额", null);
                }
                break;
            case R.id.tv_more:
                //umeng统计
                Statistic.onEvent(Events.HOME_CLICK_VIEW_MORE);

                EventBus.getDefault().post(new NavigateNews());
                break;
        }
    }

    @Override
    protected boolean needFakeStatusBar() {
        //fake status bar is unexpected.
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tab_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //umeng统计
        Statistic.onEvent(Events.HOME_CLICK_MESSAGE);

        if (!FastClickUtils.isFastClick()) {
            presenter.checkMsg();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(TabHomeContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showBanner(List<AdBanner> list) {
        handleShowContent();
        if (headerViewHolder != null) {
            headerViewHolder.loadBanner(list);
        }
    }

    @Override
    public void showBorrowingScroll(List<String> list) {
        handleShowContent();
        if (headerViewHolder != null) {
            headerViewHolder.marqueeView.startWithList(list);
        }
    }

    @Override
    public void showHotNews(List<HotNews> news) {
        handleShowContent();
        if (headerViewHolder != null) {
            headerViewHolder.newsAdapter.notifyHotNewsChanged(news);
        }
    }

    @Override
    public void showHotLoanProducts(List<LoanProduct.Row> products) {
        handleShowContent();
        if (loanRVAdapter != null) {
            loanRVAdapter.notifyLoanProductChanged(products);
        }
    }

    @Override
    public void showAdDialog(AdBanner ad) {
        new AdDialog().setAd(ad).show(getChildFragmentManager(), AdDialog.class.getSimpleName());
    }

    @Override
    public void showNotice(NoticeAbstract notice) {
        final NoticeAbstract noticeAbstract = notice;
        noticeContainer.setVisibility(View.VISIBLE);
        noticeTv.setScrollMode(AutoTextView.SCROLL_FAST);
        noticeTv.setText(notice.getTitle());
        noticeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NoticeDetailActivity.class);
                intent.putExtra("noticeId", noticeAbstract.getId());
                startActivity(intent);

                noticeContainer.setVisibility(View.GONE);

                SPUtils.setNoticeClosed(getContext(), true);
            }
        });
    }

    @Override
    public void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showError() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        stateLayout.switchState(StateLayout.STATE_NET_ERROR);
    }

    @Override
    public void navigateLogin() {
        UserAuthorizationActivity.launch(getActivity(), null);
    }

    @Override
    public void navigateWorthTest() {
        Intent intent = new Intent(getContext(), WorthTestActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateMessageCenter() {
        Intent intent = new Intent(getContext(), MessageCenterActivity.class);
        startActivity(intent);
    }

    private void handleShowContent() {
        if (stateLayout != null) {
            stateLayout.switchState(StateLayout.STATE_CONTENT);
        }
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    class HeaderViewHolder {
        View itemView;

        @BindView(R.id.banner)
        Banner banner;
        @BindView(R.id.ly_banner)
        LinearLayout lyBanner;
        @BindView(R.id.marqueeView)
        MarqueeView marqueeView;
        @BindView(R.id.ly_etbg)
        LinearLayout lyEtbg;
        @BindView(R.id.et_money)
        EditText etMoney;
        @BindView(R.id.tv_tuijian)
        TextView tvTuiJian;
        @BindView(R.id.tv_more)
        TextView tvMore;
        @BindView(R.id.worth_test)
        View worthTest;
        @BindView(R.id.recycle_hor)
        RecyclerView recyclerView;

        HotNewsAdapter newsAdapter;

        Unbinder unbinder;

        private List<AdBanner> bannerAds = new ArrayList<>();

        HeaderViewHolder(View itemView) {
            this.itemView = itemView;
            unbinder = ButterKnife.bind(this, itemView);

            banner.setDelayTime(5000);
            banner.setIndicatorGravity(BannerConfig.RIGHT);
            banner.isAutoPlay(true);

            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
            newsAdapter = new HotNewsAdapter();
            newsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = new Intent(getContext(), NewsDetailActivity.class);
                    intent.putExtra("hotNews", (HotNews) adapter.getData().get(position));
                    startActivity(intent);
                }
            });

            recyclerView.setLayoutManager(llm);
            recyclerView.addItemDecoration(new HotNewsItemDeco());
            recyclerView.setAdapter(newsAdapter);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lyBanner.getLayoutParams();
            params.width = CommonUtils.getScreenMaxWidth(getActivity(), 0);
            bannerHeight = (params.width * 530) / 975;
            params.height = bannerHeight;

            lyBanner.setLayoutParams(params);

            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    AdBanner ad = bannerAds.get(position);
                    //统计点击
                    DataStatisticsHelper.getInstance().onAdClicked(ad.getId(), ad.getType());
                    //跳原生还是跳Web
                    if (ad.isNative()) {
                        Intent intent = new Intent(getContext(), LoanDetailActivity.class);
                        intent.putExtra("loanId", ad.getLocalId());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getContext(), ComWebViewActivity.class);
                        intent.putExtra("url", ad.getUrl());
                        intent.putExtra("title", ad.getTitle());
                        startActivity(intent);
                    }
                }
            });
        }

        void destroy() {
            marqueeView.stopFlipping();
            unbinder.unbind();
        }

        void loadBanner(List<AdBanner> list) {
            if (list != null) {
                bannerAds.addAll(list);
                List<String> images = new ArrayList<>();
                for (int i = 0; i < list.size(); ++i) {
                    images.add(list.get(i).getImgUrl());
                }
                banner.setImages(images).setImageLoader(new BannerImageLoader()).start();
            }
        }
    }

    private class BannerImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context)
                    .load((String) path)
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.mipmap.banner_place_holder)
                    .into(imageView);
        }
    }
}
