package com.xxx.ency.di.component;

import com.xxx.ency.di.module.SecurityCheckFragmentModule;
import com.xxx.ency.di.scope.FragmentScope;
import com.xxx.ency.view.SecurityCheck.SecurityCheckFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class,
        modules = SecurityCheckFragmentModule.class)
public interface SecurityCheckFragmentComponent {
    void inject(SecurityCheckFragment securityCheckFragment);
}
