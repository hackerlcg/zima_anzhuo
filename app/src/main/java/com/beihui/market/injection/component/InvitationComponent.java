package com.beihui.market.injection.component;


import com.beihui.market.injection.module.InvitationModule;
import com.beihui.market.ui.activity.InvitationActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = InvitationModule.class)
public interface InvitationComponent {

    void inject(InvitationActivity activity);
}
