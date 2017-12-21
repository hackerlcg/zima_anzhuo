package com.beihui.market.injection.component;


import com.beihui.market.injection.module.LoginModule;
import com.beihui.market.ui.fragment.UserLoginFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = LoginModule.class)
public interface LoginComponent {

    void inject(UserLoginFragment loginFragment);
}
