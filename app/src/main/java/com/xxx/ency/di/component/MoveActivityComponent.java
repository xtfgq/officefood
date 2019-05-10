package com.xxx.ency.di.component;

import com.xxx.ency.di.module.MoveActivityModule;
import com.xxx.ency.di.module.ScanCodeActivityModule;
import com.xxx.ency.di.scope.ActivityScope;
import com.xxx.ency.view.move.MoveActivity;
import com.xxx.ency.view.scan.ScanCodeActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class,
        modules = MoveActivityModule.class)
public interface MoveActivityComponent {
    void inject(MoveActivity moveActivity);
}
