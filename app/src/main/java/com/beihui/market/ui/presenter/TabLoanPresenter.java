package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.base.Constant;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.ui.contract.TabLoanContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TabLoanPresenter extends BaseRxPresenter implements TabLoanContract.Presenter {
    private static final int PAGE_SIZE = 10;
    private String dueTimes[] = {"1个月及以下", "3个月", "6个月", "12个月", "24个月", "36个月及以上", "不限"};
    private String pros[] = {"上班族", "自由职业", "个体户", "不限"};

    private Api mApi;
    private TabLoanContract.View mView;

    private int amount = Constant.DEFAULT_FILTER_MONEY;
    private int dueTimeSelected = 6;
    private int proSelected = 3;
    private List<LoanProduct.Row> loanProductList = new ArrayList<>();
    private int curPage = 1;

    private Disposable curTask;

    @Inject
    TabLoanPresenter(Api api, TabLoanContract.View view) {
        mApi = api;
        mView = view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mView.showFilters(amount + "", dueTimes[dueTimeSelected], pros[proSelected]);
        if (loanProductList.size() > 0) {
            mView.showLoanProduct(Collections.unmodifiableList(loanProductList), loanProductList.size() >= PAGE_SIZE);
        } else {
            refresh();
        }
    }

    @Override
    public void filterAmount(int amount) {
        if (amount != this.amount) {
            this.amount = amount;
            mView.showFilters(amount + "", dueTimes[dueTimeSelected], pros[proSelected]);

            loanProductList.clear();
            mView.showLoanProduct(Collections.unmodifiableList(loanProductList), false);
            refresh();
        }
    }

    @Override
    public void filterDueTime(int selected) {
        if (selected != dueTimeSelected) {
            dueTimeSelected = selected;
            mView.showFilters(amount + "", dueTimes[dueTimeSelected], pros[proSelected]);

            loanProductList.clear();
            mView.showLoanProduct(Collections.unmodifiableList(loanProductList), false);
            refresh();
        }
    }

    @Override
    public void filterPro(int selected) {
        if (selected != proSelected) {
            proSelected = selected;
            mView.showFilters(amount + "", dueTimes[dueTimeSelected], pros[proSelected]);

            loanProductList.clear();
            mView.showLoanProduct(Collections.unmodifiableList(loanProductList), false);
            refresh();
        }
    }

    @Override
    public void refresh() {
        cancelCurTask();
        curPage = 1;
        Disposable dis = mApi.queryLoanProduct(amount, dueTimeSelected + 1, getProParamBySelected(proSelected), curPage, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<LoanProduct>>io2main())
                .subscribe(new Consumer<ResultEntity<LoanProduct>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<LoanProduct> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().getTotal() > 0) {
                                           loanProductList.clear();
                                           loanProductList.addAll(result.getData().getRows());
                                           mView.showLoanProduct(Collections.unmodifiableList(loanProductList),
                                                   loanProductList.size() >= PAGE_SIZE);

                                       } else {
                                           mView.showNoLoanProduct();
                                       }
                                   } else {
                                       //当前没有任何原始数据，切换至网络错误状态，否则只需提示信息
                                       if (loanProductList.size() == 0) {
                                           mView.showNetError();
                                       } else {
                                           mView.showErrorMsg(result.getMsg());
                                       }
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(TabLoanPresenter.this, throwable);
                                //当前没有任何原始数据，切换至网络错误状态，否则只需提示信息
                                if (loanProductList.size() == 0) {
                                    mView.showNetError();
                                } else {
                                    mView.showErrorMsg(generateErrorMsg(throwable));
                                }
                            }
                        });
        curTask = dis;
        addDisposable(dis);
    }

    @Override
    public void loadMore() {
        curPage++;
        Disposable dis = mApi.queryLoanProduct(amount, dueTimeSelected + 1, getProParamBySelected(proSelected),
                curPage, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<LoanProduct>>io2main())
                .subscribe(new Consumer<ResultEntity<LoanProduct>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<LoanProduct> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().getTotal() > 0) {
                                           loanProductList.addAll(result.getData().getRows());
                                           mView.showLoanProduct(Collections.unmodifiableList(loanProductList),
                                                   result.getData().getTotal() >= PAGE_SIZE);

                                       } else {
                                           mView.showNoMoreLoanProduct();
                                       }
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                       //请求失败，回溯页数
                                       curPage--;
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(TabLoanPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                                //请求失败，回溯页数
                                curPage--;
                            }
                        });
        addDisposable(dis);
    }


    @Override
    public String getFilterAmount() {
        return amount + "";
    }

    @Override
    public String[] getFilterDueTime() {
        return dueTimes;
    }

    @Override
    public int getFilterDueTimeSelected() {
        return dueTimeSelected;
    }

    @Override
    public String[] getFilterPro() {
        return pros;
    }

    private int getProParamBySelected(int selected) {
        switch (selected) {
            case 0:
                return 2;
            case 1:
                return 4;
            case 2:
                return 3;
            case 3:
                return 1;
            default:
                return 0;
        }
    }

    private void cancelCurTask() {
        if (curTask != null && !curTask.isDisposed()) {
            curTask.dispose();
        }
    }
}
