package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.SysMsg;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.SysMsgContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SysMsgPresenter extends BaseRxPresenter implements SysMsgContract.Presenter {

    private static final int PAGE_SIZE = 10;

    private Api mApi;
    private SysMsgContract.View mView;
    private int curPage = 1;
    private List<SysMsg.Row> sysMsgList;
    private UserHelper mUserHelper;

    @Inject
    SysMsgPresenter(Api api, SysMsgContract.View view, Context context) {
        mApi = api;
        mView = view;
        mUserHelper = UserHelper.getInstance(context);
    }


    @Override
    public void onStart() {
        super.onStart();
        curPage = 1;
        Disposable dis = mApi.querySysMsgList(mUserHelper.getProfile().getId(), curPage, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<SysMsg>>io2main())
                .subscribe(new Consumer<ResultEntity<SysMsg>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<SysMsg> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().getTotal() > 0) {
                                           if (sysMsgList == null) {
                                               sysMsgList = new ArrayList<>();
                                           }
                                           sysMsgList.clear();
                                           sysMsgList.addAll(result.getData().getRows());
                                           mView.showSysMsg(Collections.unmodifiableList(sysMsgList));
                                           //返回数据少于请求个数
                                           if (result.getData().getTotal() < PAGE_SIZE) {
                                               mView.showNoMoreSysMsg();
                                           }
                                       } else {
                                           mView.showNoSysMsg();
                                       }
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(SysMsgPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void loadMore() {
        curPage++;
        Disposable dis = mApi.querySysMsgList(mUserHelper.getProfile().getId(), curPage, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<SysMsg>>io2main())
                .subscribe(new Consumer<ResultEntity<SysMsg>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<SysMsg> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().getTotal() > 0) {
                                           sysMsgList.addAll(result.getData().getRows());
                                           mView.showSysMsg(Collections.unmodifiableList(sysMsgList));
                                           //返回的数据少于请求的个数
                                           if (result.getData().getTotal() < PAGE_SIZE) {
                                               mView.showNoMoreSysMsg();
                                           }
                                       } else {
                                           mView.showNoMoreSysMsg();
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
                                logError(SysMsgPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                                //请求失败，回溯页数
                                curPage--;
                            }
                        });
        addDisposable(dis);
    }
}
