package com.xxx.ency.di.component;

import com.xxx.ency.di.module.LoginActivityModule;
import com.xxx.ency.di.module.ScanCodeActivityModule;
import com.xxx.ency.di.scope.ActivityScope;
import com.xxx.ency.view.login.LoginActivity;
import com.xxx.ency.view.scan.ScanCodeActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class,
        modules = ScanCodeActivityModule.class)
public interface ScanCodeActivityComponent {
    void inject(ScanCodeActivity scanCodeActivity);
}
