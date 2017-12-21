package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.SysMsgDetailContract;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

@Module
public class SysMsgDetailModule {

    private SysMsgDetailContract.View mView;

    public SysMsgDetailModule(SysMsgDetailContract.View view) {
        this.mView = view;
    }

    @Provides
    public SysMsgDetailContract.View provideSysMsgContractView() {
        return mView;
    }
}
