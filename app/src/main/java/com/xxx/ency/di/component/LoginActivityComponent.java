package com.xxx.ency.di.component;

import com.xxx.ency.di.module.LoginActivityModule;

import com.xxx.ency.di.scope.ActivityScope;
import com.xxx.ency.view.login.LoginActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class,
        modules = LoginActivityModule.class)
public interface LoginActivityComponent {
    void inject(LoginActivity loginActivity);
}
