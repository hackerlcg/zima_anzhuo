package com.beihui.market.injection.component;


import com.beihui.market.injection.module.RegisterSetPwdModule;
import com.beihui.market.ui.fragment.UserRegisterSetPsdFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = RegisterSetPwdModule.class)
public interface RegisterSetPwdComponent {

    void inject(UserRegisterSetPsdFragment fragment);
}
