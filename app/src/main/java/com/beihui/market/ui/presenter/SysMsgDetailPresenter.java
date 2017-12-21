package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.SysMsgDetail;
import com.beihui.market.ui.contract.SysMsgDetailContract;
import com.beihui.market.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SysMsgDetailPresenter extends BaseRxPresenter implements SysMsgDetailContract.Presenter {
    private Api mApi;
    private SysMsgDetailContract.View mView;

    @Inject
    SysMsgDetailPresenter(Api api, SysMsgDetailContract.View view) {
        mApi = api;
        mView = view;
    }


    @Override
    public void queryMsgDetail(String id) {
        Disposable dis = mApi.querySysMsgDetail(id)
                .compose(RxUtil.<ResultEntity<SysMsgDetail>>io2main())
                .subscribe(new Consumer<ResultEntity<SysMsgDetail>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<SysMsgDetail> result) throws Exception {
                                   if (result.isSuccess()) {
                                       mView.showSysMsgDetail(result.getData());
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(SysMsgDetailPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
