package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.SysMsgContract;

import dagger.Module;
import dagger.Provides;

@Module
public class SysMsgModule {

    private SysMsgContract.View mView;

    public SysMsgModule(SysMsgContract.View view) {
        mView = view;
    }

    @Provides
    public SysMsgContract.View provideSysMsgContractView() {
        return mView;
    }
}
