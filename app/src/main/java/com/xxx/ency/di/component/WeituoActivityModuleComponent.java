package com.xxx.ency.di.component;
import com.xxx.ency.di.module.WeituoActivityModule;
import com.xxx.ency.di.scope.ActivityScope;
import com.xxx.ency.view.work.WeituoActivity;
import dagger.Component;
@ActivityScope
@Component(dependencies = AppComponent.class,
        modules = WeituoActivityModule.class)
public interface WeituoActivityModuleComponent {
    void inject(WeituoActivity weituoActivity);
}
