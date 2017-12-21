package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.News;
import com.beihui.market.ui.contract.TabNewsContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TabNewsPresenter extends BaseRxPresenter implements TabNewsContract.Presenter {
    private static final int PAGE_SIZE = 10;

    private Api mApi;
    private TabNewsContract.View mView;
    private List<News.Row> news = new ArrayList<>();
    private int curPage = 1;
    //是否已经没有更多历史可加载
    private boolean reachEnd;

    @Inject
    TabNewsPresenter(Api api, TabNewsContract.View view) {
        mApi = api;
        mView = view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //如果已经存在资讯数据，则认为已经完成初始化，根据现有状态更新
        if (news != null && news.size() > 0) {
            mView.showNews(news);
            if (reachEnd) {
                mView.showNoMoreNews();
            }
        } else {
            refresh();
        }
    }

    @Override
    public void refresh() {
        curPage = 1;
        Disposable dis = mApi.queryNews(curPage, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<News>>io2main())
                .subscribe(new Consumer<ResultEntity<News>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<News> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().getTotal() > 0) {
                                           news.clear();
                                           news.addAll(result.getData().getRows());
                                           mView.showNews(Collections.unmodifiableList(news));
                                           //返回的数据少于请求的个数
                                           if (result.getData().getTotal() < PAGE_SIZE) {
                                               reachEnd();
                                           }
                                       } else {
                                           mView.showNoNews();
                                       }
                                   } else {
                                       //只有在没有任何数据的情况下出现网络错误才会切换网络错误状态
                                       if (news.size() == 0) {
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
                                logError(TabNewsPresenter.this, throwable);
                                //只有在没有任何数据的情况下出现网络错误才会切换网络错误状态
                                if (news.size() == 0) {
                                    mView.showNetError();
                                } else {
                                    mView.showErrorMsg(generateErrorMsg(throwable));
                                }
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void loadMore() {
        curPage++;
        Disposable dis = mApi.queryNews(curPage, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<News>>io2main())
                .subscribe(new Consumer<ResultEntity<News>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<News> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().getTotal() > 0) {
                                           news.addAll(result.getData().getRows());
                                           mView.showNews(Collections.unmodifiableList(news));
                                           //返回的数据少于请求的个数
                                           if (result.getData().getTotal() < PAGE_SIZE) {
                                               reachEnd();
                                           }
                                       } else {
                                           reachEnd();
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
                                logError(TabNewsPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                                //请求失败，回溯页数
                                curPage--;
                            }
                        });
        addDisposable(dis);
    }

    private void reachEnd() {
        reachEnd = true;
        mView.showNoMoreNews();
    }
}
