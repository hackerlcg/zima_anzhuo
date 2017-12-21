package com.beihui.market.injection.component;


import com.beihui.market.injection.module.NewsModule;
import com.beihui.market.ui.fragment.TabNewsFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = NewsModule.class)
public interface NewsComponent {

    void inject(TabNewsFragment fragment);
}
