package com.xxx.ency.di.component;

import com.xxx.ency.di.module.SecurityCheckFragmentListModule;

import com.xxx.ency.di.scope.FragmentScope;
import com.xxx.ency.view.SecurityCheck.SecurityCheckFragment;
import com.xxx.ency.view.SecurityCheck.SecurityCheckListFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class,
        modules = SecurityCheckFragmentListModule.class)
public interface SecurityCheckFragmentListComponent {
    void inject(SecurityCheckListFragment securityCheckFragment);
}
