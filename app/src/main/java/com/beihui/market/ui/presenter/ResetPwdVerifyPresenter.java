package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.Phone;
import com.beihui.market.ui.contract.ResetPwdVerifyContract;
import com.beihui.market.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ResetPwdVerifyPresenter extends BaseRxPresenter implements ResetPwdVerifyContract.Presenter {
    private Api mApi;
    private ResetPwdVerifyContract.View mView;

    private boolean isRequestingVerification;

    @Inject
    public ResetPwdVerifyPresenter(Api api, ResetPwdVerifyContract.View view) {
        mApi = api;
        mView = view;
    }

    @Override
    public void requestVerification(String phone) {
        if (!isRequestingVerification) {
            Disposable dis = mApi.requestRestPwdSms(phone).compose(RxUtil.<ResultEntity<Phone>>io2main())
                    .subscribe(new Consumer<ResultEntity<Phone>>() {
                                   @Override
                                   public void accept(@NonNull ResultEntity<Phone> result) throws Exception {
                                       isRequestingVerification = false;
                                       if (result.isSuccess()) {
                                           mView.showVerificationSend(result.getMsg());
                                       } else {
                                           mView.showErrorMsg(result.getMsg());
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    isRequestingVerification = false;
                                    logError(ResetPwdVerifyPresenter.this, throwable);
                                    mView.showErrorMsg(generateErrorMsg(throwable));
                                }
                            });
            addDisposable(dis);
        }
    }

    @Override
    public void nextMove(String phone, String verificationCode) {
        final String requestPhone = phone;
        Disposable dis = mApi.verifyResetPwdCode(phone, verificationCode).compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       mView.moveToNextStep(requestPhone);
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(ResetPwdVerifyPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
