package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.LoanProductDetail;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.LoanProductDetailContract;
import com.beihui.market.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoanDetailPresenter extends BaseRxPresenter implements LoanProductDetailContract.Presenter {

    private Api mApi;
    private LoanProductDetailContract.View mView;
    private UserHelper mUserHelper;

    private LoanProductDetail productDetail;

    @Inject
    LoanDetailPresenter(Api api, LoanProductDetailContract.View view, Context context) {
        mApi = api;
        mView = view;
        mUserHelper = UserHelper.getInstance(context);
    }


    @Override
    public void queryDetail(String id) {
        String userId = null;
        if (mUserHelper.getProfile() != null) {
            userId = mUserHelper.getProfile().getId();
        }
        Disposable dis = mApi.queryLoanProductDetail(id, userId)
                .compose(RxUtil.<ResultEntity<LoanProductDetail>>io2main())
                .subscribe(new Consumer<ResultEntity<LoanProductDetail>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<LoanProductDetail> result) throws Exception {
                                   if (result.isSuccess()) {
                                       productDetail = result.getData();
                                       mView.showLoanDetail(result.getData());
                                   } else if (result.getCode() == 2000039) {
                                       //产品已经下架
                                       mView.showLoanOffSell();
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(LoanDetailPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void checkLoan() {
        if (mUserHelper.getProfile() != null) {
            if (productDetail != null) {
                mView.navigateLoan(productDetail);
            }
        } else {
            mView.navigateLogin();
        }
    }
}
