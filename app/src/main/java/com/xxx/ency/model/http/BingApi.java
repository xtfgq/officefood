package com.xxx.ency.model.http;

import com.xxx.ency.config.Constants;
import com.xxx.ency.model.bean.BingBean;
import com.xxx.ency.model.bean.LoginBean;
import com.xxx.ency.model.bean.Result;

import org.json.JSONObject;

import io.reactivex.Flowable;
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

/**login
 * Bing壁纸
 * Created by xiarh on 2017/11/3.
 */

public interface BingApi {

    String HOST = Constants.HOST;

    /**
     * Bing随机壁纸
     *
     * @param width
     * @param height
     * @param type
     * @return
     */


    @FormUrlEncoded
    @POST("api")
    Call<ResponseBody> postLogin(@Field("method") String method, @Field("param") String stu);
    @FormUrlEncoded
    @POST("api")
    Call<ResponseBody> postToken(@Header("token") String authToken, @Field("method") String method,
                                 @Field("param") String stu);

}
