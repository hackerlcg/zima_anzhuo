package com.beihui.market.injection.component;


import com.beihui.market.injection.module.RegisterVerifyModule;
import com.beihui.market.ui.fragment.UserRegisterVerifyCodeFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = RegisterVerifyModule.class)
public interface RegisterVerifyComponent {

    void inject(UserRegisterVerifyCodeFragment fragment);
}
