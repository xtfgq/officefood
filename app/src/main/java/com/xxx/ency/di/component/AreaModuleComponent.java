package com.xxx.ency.di.component;

import com.xxx.ency.di.module.AreaWorkModule;

import com.xxx.ency.di.scope.ActivityScope;

import com.xxx.ency.view.work.AreaActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class,
        modules = AreaWorkModule.class)
public interface AreaModuleComponent {
    void inject(AreaActivity areaActivity);
}
