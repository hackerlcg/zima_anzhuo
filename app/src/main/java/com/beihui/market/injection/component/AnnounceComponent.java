package com.beihui.market.injection.component;


import com.beihui.market.injection.module.AnnounceModule;
import com.beihui.market.ui.activity.NoticeActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = AnnounceModule.class)
public interface AnnounceComponent {

    void inject(NoticeActivity activity);
}
