package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.TabMineContract;

import dagger.Module;
import dagger.Provides;

@Module
public class TabMineModule {

    private TabMineContract.View mView;

    public TabMineModule(TabMineContract.View view) {
        mView = view;
    }

    @Provides
    public TabMineContract.View provideTabMineContractView() {
        return mView;
    }
}
