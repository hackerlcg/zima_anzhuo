package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.ChangePsdContract;

import dagger.Module;
import dagger.Provides;

@Module
public class ChangePsdModule {

    private ChangePsdContract.View mView;

    public ChangePsdModule(ChangePsdContract.View view) {
        mView = view;
    }

    @Provides
    public ChangePsdContract.View provideChangePsdContractView() {
        return mView;
    }
}
