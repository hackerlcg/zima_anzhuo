package com.beihui.market.injection.component;


import com.beihui.market.injection.module.EditProfessionModule;
import com.beihui.market.ui.activity.EditJobGroupActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = EditProfessionModule.class)
public interface EditProfessionComponent {

    void inject(EditJobGroupActivity activity);
}
