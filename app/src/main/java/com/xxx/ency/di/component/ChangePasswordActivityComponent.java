package com.xxx.ency.di.component;

import com.xxx.ency.di.module.ChangePaaswordActivityModule;
import com.xxx.ency.di.module.LoginActivityModule;
import com.xxx.ency.di.scope.ActivityScope;
import com.xxx.ency.view.login.ChangePasswrodActivity;
import com.xxx.ency.view.login.LoginActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class,
        modules = ChangePaaswordActivityModule.class)
public interface ChangePasswordActivityComponent {
    void inject(ChangePasswrodActivity changePasswrodActivity);
}
