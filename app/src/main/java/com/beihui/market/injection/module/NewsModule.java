package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.TabNewsContract;

import dagger.Module;
import dagger.Provides;

@Module
public class NewsModule {

    private TabNewsContract.View mView;

    public NewsModule(TabNewsContract.View view) {
        mView = view;
    }

    @Provides
    public TabNewsContract.View provideNewsContractView() {
        return mView;
    }
}
