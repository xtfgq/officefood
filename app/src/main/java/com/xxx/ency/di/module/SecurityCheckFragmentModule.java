package com.xxx.ency.di.module;

import com.xxx.ency.di.qualifier.WeiXinURL;
import com.xxx.ency.di.scope.FragmentScope;
import com.xxx.ency.model.http.WeiXinApi;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class SecurityCheckFragmentModule {
    @WeiXinURL
    @Provides
    @FragmentScope
    Retrofit provideWeiXinRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return builder
                .baseUrl(WeiXinApi.HOST)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @FragmentScope
    WeiXinApi provideWeiXinApi(@WeiXinURL Retrofit retrofit) {
        return retrofit.create(WeiXinApi.class);
    }
}
