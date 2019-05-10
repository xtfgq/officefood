package com.xxx.ency.di.component;

import com.xxx.ency.di.module.MoveActivityModule;
import com.xxx.ency.di.module.SendActivityModule;
import com.xxx.ency.di.scope.ActivityScope;
import com.xxx.ency.view.move.MoveActivity;
import com.xxx.ency.view.send.SendActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class,
        modules = SendActivityModule.class)
public interface SendActivityComponent {
    void inject(SendActivity sendActivity);
}
