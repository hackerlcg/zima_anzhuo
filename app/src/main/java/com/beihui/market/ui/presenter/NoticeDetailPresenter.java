package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.NoticeDetail;
import com.beihui.market.ui.contract.NoticeDetailContract;
import com.beihui.market.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class NoticeDetailPresenter extends BaseRxPresenter implements NoticeDetailContract.Presenter {

    private Api mApi;
    private NoticeDetailContract.View mView;

    @Inject
    NoticeDetailPresenter(Api api, NoticeDetailContract.View view) {
        mApi = api;
        mView = view;
    }

    @Override
    public void queryDetail(String id) {
        Disposable dis = mApi.queryNoticeDetail(id)
                .compose(RxUtil.<ResultEntity<NoticeDetail>>io2main())
                .subscribe(new Consumer<ResultEntity<NoticeDetail>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<NoticeDetail> result) throws Exception {
                                   if (result.isSuccess()) {
                                       mView.showDetail(result.getData());
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(NoticeDetailPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
