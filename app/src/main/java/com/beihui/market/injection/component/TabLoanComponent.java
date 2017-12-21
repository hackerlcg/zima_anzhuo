package com.beihui.market.injection.component;


import com.beihui.market.injection.module.TabLoanModule;
import com.beihui.market.ui.fragment.TabLoanFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = TabLoanModule.class)
public interface TabLoanComponent {

    void inject(TabLoanFragment fragment);
}
