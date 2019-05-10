package com.xxx.ency.di.component;


import com.xxx.ency.di.module.MessageActivityModule;
import com.xxx.ency.di.scope.ActivityScope;
import com.xxx.ency.view.message.MessageActivity;


import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class,
        modules = MessageActivityModule.class)
public interface MessageActivityComponent {
    void inject(MessageActivity messageActivity);
}
