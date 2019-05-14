package com.xxx.ency.model.http;

import com.xxx.ency.config.Constants;
import com.xxx.ency.model.bean.GankBean;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 干货热门
 * Created by xiarh on 2017/11/27.
 */

public interface GankApi {

    String HOST = Constants.HOST;

    /**
     *
     *
     */
    @FormUrlEncoded
    @POST("api")
    Call<ResponseBody> postToken(@Header("token") String authToken, @Field("method") String method,
                                 @Field("param") String stu);
}
