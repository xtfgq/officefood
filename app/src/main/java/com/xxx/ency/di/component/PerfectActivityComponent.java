package com.xxx.ency.di.component;

import com.xxx.ency.di.module.PerfectActivityModule;
import com.xxx.ency.di.module.ScanCodeActivityModule;
import com.xxx.ency.di.scope.ActivityScope;
import com.xxx.ency.view.perfect.PerfectActivity;
import com.xxx.ency.view.scan.ScanCodeActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class,
        modules = PerfectActivityModule.class)
public interface PerfectActivityComponent {
    void inject(PerfectActivity perfectActivity);
}
