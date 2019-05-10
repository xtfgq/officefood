package com.xxx.ency.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.xxx.ency.base.RxPresenter;
import com.xxx.ency.config.Constants;
import com.xxx.ency.contract.ChangePasswordContract;

import com.xxx.ency.model.bean.PostSmsBean;
import com.xxx.ency.model.bean.WeatherPostBean;
import com.xxx.ency.model.http.BingApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordPresenter extends RxPresenter<ChangePasswordContract.View>
        implements ChangePasswordContract.Presenter{

    private Context context;
    private BingApi bingApi;

    @Inject
    public ChangePasswordPresenter(BingApi bingApi, Context context) {
        this.bingApi = bingApi;
        this.context = context;
    }
    @Override
    public void changePwd() {
        PostSmsBean postSmsBean=new PostSmsBean();
        postSmsBean.setMobile(mView.getPhone());
        postSmsBean.setIdentifyingCode(mView.getCode());
        postSmsBean.setPassword(mView.getPwd());
        Gson gson=new Gson();
        String route= gson.toJson(postSmsBean);
        Call<ResponseBody> call=bingApi.postLogin("userResetPassworldByIdentifyingCode",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        mView.changSuccuss();
                    }else {
                        if(mView!=null)
                        mView.showError(jsonObject.getString("rtnMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void sendSms() {
        PostSmsBean postSmsBean=new PostSmsBean();
        postSmsBean.setMobile(mView.getPhone());
        Gson gson=new Gson();
        String route= gson.toJson(postSmsBean);
        Call<ResponseBody> call=bingApi.postLogin("userForgetPasswordSendSmsIdentifyingCode",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS){
                        mView.sendSmsBean();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


}
