package com.beihui.market.ui.presenter;

import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.ResetPwdSetPwdContract;
import com.beihui.market.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ResetPwdSetPwdPresenter extends BaseRxPresenter implements ResetPwdSetPwdContract.Presenter {

    private Api mApi;
    private ResetPwdSetPwdContract.View mView;
    private Context mContext;
    private UserHelper mUserHelper;

    @Inject
    public ResetPwdSetPwdPresenter(Api api, ResetPwdSetPwdContract.View view, Context context) {
        mApi = api;
        mView = view;
        mContext = context;
        mUserHelper = UserHelper.getInstance(context);
    }

    @Override
    public void resetPwd(String phone, String pwd) {
        mView.showLoading();
        Disposable dis = mApi.resetPwd(phone, pwd)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       mUserHelper.clearUser(mContext);
                                       mView.showRestPwdSuccess(result.getMsg());
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(ResetPwdSetPwdPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
