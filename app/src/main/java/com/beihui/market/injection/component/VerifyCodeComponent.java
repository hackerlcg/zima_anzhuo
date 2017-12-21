package com.beihui.market.injection.component;

import com.beihui.market.injection.module.VerifyCodeModule;
import com.beihui.market.ui.fragment.RequireVerifyCodeFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = VerifyCodeModule.class)
public interface VerifyCodeComponent {

    void inject(RequireVerifyCodeFragment fragment);
}
