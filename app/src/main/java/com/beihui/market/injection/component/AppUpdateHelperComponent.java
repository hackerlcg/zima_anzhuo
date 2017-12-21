package com.beihui.market.injection.component;


import com.beihui.market.helper.updatehelper.AppUpdateHelper;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface AppUpdateHelperComponent {

    void inject(AppUpdateHelper helper);
}
