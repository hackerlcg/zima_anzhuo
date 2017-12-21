package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.LoanProductDetailContract;

import dagger.Module;
import dagger.Provides;

@Module
public class LoanDetailModule {

    private LoanProductDetailContract.View mView;

    public LoanDetailModule(LoanProductDetailContract.View view) {
        mView = view;
    }

    @Provides
    public LoanProductDetailContract.View provideLoanProductDetailContractView() {
        return mView;
    }
}
