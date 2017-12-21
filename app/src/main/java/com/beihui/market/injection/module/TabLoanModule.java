package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.TabLoanContract;

import dagger.Module;
import dagger.Provides;

@Module
public class TabLoanModule {

    private TabLoanContract.View mView;

    public TabLoanModule(TabLoanContract.View view) {
        mView = view;
    }

    @Provides
    public TabLoanContract.View provideTabLoanContractView() {
        return mView;
    }
}
