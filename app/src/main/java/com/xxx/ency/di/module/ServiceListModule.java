package com.xxx.ency.di.module;

import com.xxx.ency.di.qualifier.WeatherURL;
import com.xxx.ency.di.scope.FragmentScope;
import com.xxx.ency.model.http.WeatherApi;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xiarh on 2017/11/2.
 */

@Module
public class ServiceListModule {
    @WeatherURL
    @Provides
    @FragmentScope
    Retrofit provideGankRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return builder
                .baseUrl(WeatherApi.HOST)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @FragmentScope
    WeatherApi provideGankApi(@WeatherURL Retrofit retrofit) {
        return retrofit.create(WeatherApi.class);
    }
}
