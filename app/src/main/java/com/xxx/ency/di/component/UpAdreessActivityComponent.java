package com.xxx.ency.di.component;
import com.xxx.ency.di.module.UpAdressActivityModule;
import com.xxx.ency.di.scope.ActivityScope;
import com.xxx.ency.view.map.UpAdressActivity;
import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class,
        modules = UpAdressActivityModule.class)
public interface UpAdreessActivityComponent {
    void inject(UpAdressActivity upAdressActivity);
}
