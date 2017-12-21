package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.HotNews;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.entity.NoticeAbstract;
import com.beihui.market.entity.request.RequestConstants;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.TabHomeContract;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TabHomePresenter extends BaseRxPresenter implements TabHomeContract.Presenter {

    private Api mApi;
    private TabHomeContract.View mView;
    private Context mContext;

    private boolean hasAdInit = false;
    private NoticeAbstract notice;
    private List<AdBanner> banners = new ArrayList<>();
    private List<String> notices = new ArrayList<>();
    private List<HotNews> hotNews = new ArrayList<>();
    private List<LoanProduct.Row> hotLoanProducts = new ArrayList<>();

    @Inject
    TabHomePresenter(Api api, TabHomeContract.View view, Context context) {
        mApi = api;
        mView = view;
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        //如果已经过初始化，则直接返回数据
        //ad dialog
        if (!hasAdInit) {
            queryAd();
        }
        //notice
        if (notice == null) {
            queryNotice();
        } else if (!SPUtils.getNoticeClosed(mContext)) {
            mView.showNotice(notice);
        }

        //banner
        if (banners.size() == 0) {
            queryBanner();
        } else {
            mView.showBanner(Collections.unmodifiableList(banners));
        }
        //loan success notice
        if (notices.size() == 0) {
            queryScrolling();
        } else {
            mView.showBorrowingScroll(Collections.unmodifiableList(notices));
        }
        //news
        if (hotNews.size() == 0) {
            queryHotNews();
        } else {
            mView.showHotNews(hotNews);
        }
        //hot loan products
        if (hotLoanProducts.size() == 0) {
            queryHotLoanProducts();
        } else {
            mView.showHotLoanProducts(hotLoanProducts);
        }
    }

    @Override
    public void refresh() {
        queryBanner();
        queryScrolling();
        queryHotNews();
        queryHotLoanProducts();
    }

    @Override
    public void checkMsg() {
        if (UserHelper.getInstance(mContext).getProfile() != null) {
            mView.navigateMessageCenter();
        } else {
            mView.navigateLogin();
        }
    }

    @Override
    public void checkMyWorth() {
        if (UserHelper.getInstance(mContext).getProfile() != null) {
            mView.navigateWorthTest();
        } else {
            mView.navigateLogin();
        }
    }

    private void queryBanner() {
        Disposable dis = mApi.querySupernatant(RequestConstants.SUP_TYPE_BANNER)
                .compose(RxUtil.<ResultEntity<List<AdBanner>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<AdBanner>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<AdBanner>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           banners.clear();
                                           banners.addAll(result.getData());
                                           mView.showBanner(Collections.unmodifiableList(banners));
                                       }
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                handleThrowable(throwable);
                            }
                        });
        addDisposable(dis);
    }

    private void queryAd() {
        Disposable dis = mApi.querySupernatant(RequestConstants.SUP_TYPE_DIALOG)
                .compose(RxUtil.<ResultEntity<List<AdBanner>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<AdBanner>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<AdBanner>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       hasAdInit = true;
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           AdBanner adBanner = result.getData().get(0);
                                           String lastId = SPUtils.getLastDialogAdId(mContext);
                                           //同一个广告只显示一次
                                           if (lastId == null || !lastId.equals(adBanner.getLocalId())) {
                                               mView.showAdDialog(result.getData().get(0));
                                           }
                                           //记录已展示的id
                                           SPUtils.setLastDialogAdId(mContext, adBanner.getLocalId());
                                       }
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                handleThrowable(throwable);
                            }
                        });
        addDisposable(dis);
    }

    private void queryScrolling() {
        Disposable dis = mApi.queryBorrowingScroll()
                .compose(RxUtil.<ResultEntity<List<String>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<String>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<String>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           notices.clear();
                                           notices.addAll(result.getData());
                                           mView.showBorrowingScroll(Collections.unmodifiableList(notices));
                                       }
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                handleThrowable(throwable);
                            }
                        });
        addDisposable(dis);
    }

    private void queryHotNews() {
        Disposable dis = mApi.queryHotNews()
                .compose(RxUtil.<ResultEntity<List<HotNews>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<HotNews>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<HotNews>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           hotNews.clear();
                                           hotNews.addAll(result.getData());
                                           mView.showHotNews(Collections.unmodifiableList(hotNews));
                                       }
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }

                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(TabHomePresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    private void queryHotLoanProducts() {
        Disposable dis = mApi.queryHotLoanProducts()
                .compose(RxUtil.<ResultEntity<List<LoanProduct.Row>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<LoanProduct.Row>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<LoanProduct.Row>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           hotLoanProducts.clear();
                                           hotLoanProducts.addAll(result.getData());
                                           mView.showHotLoanProducts(Collections.unmodifiableList(hotLoanProducts));
                                       }
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(TabHomePresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    private void queryNotice() {
        Disposable dis = mApi.queryNoticeHome()
                .compose(RxUtil.<ResultEntity<NoticeAbstract>>io2main())
                .subscribe(new Consumer<ResultEntity<NoticeAbstract>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<NoticeAbstract> result) throws Exception {
                                   if (result.isSuccess() && result.getData() != null) {
                                       notice = result.getData();
                                       if (notice.getId() != null) {
                                           if (!notice.getId().equals(SPUtils.getLastNoticeId(mContext))) {
                                               mView.showNotice(notice);
                                               SPUtils.setNoticeClosed(mContext, false);
                                           } else if (!SPUtils.getNoticeClosed(mContext)) {
                                               mView.showNotice(notice);
                                           }
                                           SPUtils.setLastNoticeId(mContext, notice.getId());
                                       }
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(TabHomePresenter.this, throwable);
                            }
                        });
        addDisposable(dis);
    }

    private void handleThrowable(Throwable throwable) {
        logError(TabHomePresenter.this, throwable);
        mView.showErrorMsg(generateErrorMsg(throwable));

        if (isAllDataEmpty()) {
            mView.showError();
        }
    }

    private boolean isAllDataEmpty() {
        return notice == null
                && banners.size() == 0
                && notices.size() == 0
                && hotNews.size() == 0
                && hotLoanProducts.size() == 0;
    }
}
