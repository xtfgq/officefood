package com.xxx.ency.model.http;
import com.xxx.ency.config.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
/**
 * 微信精选
 * Created by xiarh on 2017/11/8.
 */

public interface WeiXinApi {

    String HOST = Constants.HOST;
    @FormUrlEncoded
    @POST("api")
    Call<ResponseBody> postCms(@Field("method") String method, @Field("param") String stu);

    @FormUrlEncoded
    @POST("api")
    Call<ResponseBody> postToken(@Header("token") String authToken, @Field("method") String method,
                                 @Field("param") String stu);

}
