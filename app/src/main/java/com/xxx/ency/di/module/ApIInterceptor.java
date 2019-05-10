package com.xxx.ency.di.module;

import com.xxx.ency.model.prefs.SharePrefManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApIInterceptor implements Interceptor {
    @Inject
    SharePrefManager sharePrefManager;
//    "token": "123123123123132",
//            "v": "1",
//            "method": "temperatureAndHumidityUpload"
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request =  chain.request()
                .newBuilder()
                .addHeader("token", sharePrefManager.getLocalToken())
                .addHeader("v", "1")
                .build();
        return chain.proceed(request);
    }

}
