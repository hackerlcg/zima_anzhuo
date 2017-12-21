package com.beihui.market.injection.component;


import com.beihui.market.entity.NoticeDetail;
import com.beihui.market.injection.module.NoticeDetailModule;
import com.beihui.market.ui.activity.NoticeDetailActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = NoticeDetailModule.class)
public interface NoticeDetailComponent {

    void inject(NoticeDetailActivity activity);
}
