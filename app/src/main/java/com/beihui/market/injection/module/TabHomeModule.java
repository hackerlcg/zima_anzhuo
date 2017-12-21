package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.TabHomeContract;

import dagger.Module;
import dagger.Provides;

@Module
public class TabHomeModule {

    private TabHomeContract.View mView;

    public TabHomeModule(TabHomeContract.View view) {
        mView = view;
    }

    @Provides
    public TabHomeContract.View provideTabHomeContractView() {
        return mView;
    }
}
