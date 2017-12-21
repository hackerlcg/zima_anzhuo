package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.NoticeContract;

import dagger.Module;
import dagger.Provides;

@Module
public class AnnounceModule {

    private NoticeContract.View mView;

    public AnnounceModule(NoticeContract.View view) {
        mView = view;
    }

    @Provides
    public NoticeContract.View provideAnnounceContractView() {
        return mView;
    }
}
