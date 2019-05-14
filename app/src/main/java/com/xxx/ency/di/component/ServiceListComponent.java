package com.xxx.ency.di.component;
import com.xxx.ency.di.module.ServiceListModule;
import com.xxx.ency.di.scope.FragmentScope;
import com.xxx.ency.view.servicelist.ServiceListFragment;
import dagger.Component;

/**
 * Created by xiarh on 2017/11/2.
 */

@FragmentScope
@Component(dependencies = AppComponent.class,
        modules =ServiceListModule.class)
public interface ServiceListComponent {

    void inject(ServiceListFragment serviceListFragment);

}
