package com.beihui.market.injection.component;


import com.beihui.market.injection.module.UserProfileModule;
import com.beihui.market.ui.activity.UserProfileActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = UserProfileModule.class)
public interface UserProfileComponent {

    void inject(UserProfileActivity activity);
}
