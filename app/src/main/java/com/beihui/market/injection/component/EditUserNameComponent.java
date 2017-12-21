package com.beihui.market.injection.component;


import com.beihui.market.injection.module.EditUserNameModule;
import com.beihui.market.ui.activity.EditNickNameActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = EditUserNameModule.class)
public interface EditUserNameComponent {

    void inject(EditNickNameActivity activity);
}
