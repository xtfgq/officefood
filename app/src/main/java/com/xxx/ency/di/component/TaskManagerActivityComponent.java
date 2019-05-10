package com.xxx.ency.di.component;

import com.xxx.ency.di.module.SendActivityModule;
import com.xxx.ency.di.module.TaskManagerActivityModule;
import com.xxx.ency.di.scope.ActivityScope;
import com.xxx.ency.view.send.SendActivity;
import com.xxx.ency.view.task.TaskManagerActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class,
        modules = TaskManagerActivityModule.class)
public interface TaskManagerActivityComponent {
    void inject(TaskManagerActivity taskManagerActivity);
}
