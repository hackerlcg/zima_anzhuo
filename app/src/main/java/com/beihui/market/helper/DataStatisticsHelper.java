package com.beihui.market.helper;


import android.content.Context;

import com.beihui.market.App;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.injection.component.DaggerDataStatisticHelperComponent;
import com.beihui.market.util.LogUtils;
import com.beihui.market.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class DataStatisticsHelper {
    private static DataStatisticsHelper sInstance;

    private static final String TAG = DataStatisticsHelper.class.getSimpleName();

    @Inject
    Api api;
    @Inject
    Context context;

    private DataStatisticsHelper() {
        DaggerDataStatisticHelperComponent.builder()
                .appComponent(App.getInstance().getAppComponent())
                .build()
                .inject(this);
    }

    public static DataStatisticsHelper getInstance() {
        if (sInstance == null) {
            synchronized (DataStatisticsHelper.class) {
                if (sInstance == null) {
                    sInstance = new DataStatisticsHelper();
                }
            }
        }
        return sInstance;
    }

    public void onProductClicked(String id) {
        String userId = UserHelper.getInstance(context).getProfile() != null ?
                UserHelper.getInstance(context).getProfile().getId() : null;
        api.onProductClicked(userId, id).compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity resultEntity) throws Exception {

                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                LogUtils.e(TAG, "product statistics error " + throwable);
                            }
                        });
    }

    public void onAdClicked(String id, int type) {
        String userId = UserHelper.getInstance(context).getProfile() != null ?
                UserHelper.getInstance(context).getProfile().getId() : null;
        api.onAdClicked(id, userId, type)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity resultEntity) throws Exception {

                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                LogUtils.e(TAG, "ad statistics error " + throwable);
                            }
                        });
    }

    public void onInternalMessageClicked(String id) {
        api.onInternalMessageClicked(id)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity resultEntity) throws Exception {

                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                LogUtils.e(TAG, "internal message statistic error " + throwable);
                            }
                        });
    }
}
