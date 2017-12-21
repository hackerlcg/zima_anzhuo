package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.NoticeDetailContract;

import dagger.Module;
import dagger.Provides;

@Module
public class NoticeDetailModule {

    private NoticeDetailContract.View mView;

    public NoticeDetailModule(NoticeDetailContract.View view) {
        mView = view;
    }

    @Provides
    public NoticeDetailContract.View provideNoticeDetailContractView() {
        return mView;
    }
}
