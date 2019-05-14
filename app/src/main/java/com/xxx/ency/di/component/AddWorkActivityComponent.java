package com.xxx.ency.di.component;
import com.xxx.ency.di.module.AddWorkActivityModule;
import com.xxx.ency.di.scope.ActivityScope;
import com.xxx.ency.view.work.AddWorkActivity;
import dagger.Component;
@ActivityScope
@Component(dependencies = AppComponent.class,
        modules = AddWorkActivityModule.class)
public interface AddWorkActivityComponent {
    void inject(AddWorkActivity addWorkActivity);
}
