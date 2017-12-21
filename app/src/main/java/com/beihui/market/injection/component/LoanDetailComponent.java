package com.beihui.market.injection.component;


import com.beihui.market.injection.module.LoanDetailModule;
import com.beihui.market.ui.activity.LoanDetailActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = LoanDetailModule.class)
public interface LoanDetailComponent {

    void inject(LoanDetailActivity activity);
}
