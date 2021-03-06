package com.xxx.ency.model.http;

import com.xxx.ency.config.Constants;
import com.xxx.ency.model.bean.CmsResult;
import com.xxx.ency.model.bean.Result;
import com.xxx.ency.model.bean.WeatherBean;

import org.json.JSONObject;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 天气预报
 * Created by xiarh on 2017/9/25.
 */

public interface WeatherApi {

    String HOST = Constants.HOST;

    /**
     * 天气预报
     *
     *
     * @return
     */

    @FormUrlEncoded
    @POST("api")
    Flowable<Result> getWeather(@Field("method") String method, @Field("param") String body);
    @FormUrlEncoded
    @POST("api")
    Call<ResponseBody> postWeather(@Field("method") String method, @Field("param") String stu);

    @FormUrlEncoded
    @POST("api")
    Call<ResponseBody> postToken(@Header("token") String authToken,@Field("method") String method, @Field("param") String stu);

    /**
     * 新闻公告
     * @param method
     * @param param
     * @return
     */
    @Headers({"Content-Type:application/json;charset=UTF-8","Accept:application/json"})

    @POST("api")

    Flowable<CmsResult> getCms(@Query("method") String method, @Query("param") String param);

}
