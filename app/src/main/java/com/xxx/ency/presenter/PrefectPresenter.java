package com.xxx.ency.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.xxx.ency.base.RxPresenter;
import com.xxx.ency.config.Constants;
import com.xxx.ency.contract.PerfectContract;
import com.xxx.ency.model.bean.LoginBean;
import com.xxx.ency.model.http.BingApi;
import com.xxx.ency.view.perfect.Upload;
import com.xxx.ency.view.perfect.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrefectPresenter extends RxPresenter<PerfectContract.View>
        implements PerfectContract.Presenter{
    private Context context;
    private BingApi bingApi;
    @Inject
    public PrefectPresenter(BingApi bingApi, Context context) {
        this.bingApi = bingApi;
        this.context = context;
    }





    @Override
    public void upLoad() {
        Upload upload=new Upload();
        upload.setPortrait(mView.getBase64());
        upload.setId(mView.getUserid());
        upload.setRealName(mView.getRealName());
        Gson gson=new Gson();
        String route= gson.toJson(upload);

        Call<ResponseBody> call=bingApi.postToken(Constants.token,"userUpdate",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){

                        mView.sucuss();
                    }else if(code==Constants.NET_CODE_LOGIN&&mView!=null){
                        goLogin();
                    }
                    else {
                        mView.showError(jsonObject.getString("rtnMsg"));
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

    @Override
    public void getUser() {
        UserInfo userInfo=new UserInfo();
        userInfo.setId(mView.getUserid());
        Gson gson=new Gson();
        String route= gson.toJson(userInfo);

        Call<ResponseBody> call=bingApi.postToken(Constants.token,"findUserById",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        JSONObject resObj=jsonObject.getJSONObject("res");
                        LoginBean lb=new LoginBean();
                        if(resObj.has("portrait")){
                            lb.setPortrait(resObj.getString("portrait"));
                        }
                        lb.setUsername(resObj.getString("realName"));
                        mView.showLoginBean(lb);
                    }else {
                        if(mView!=null)
                        mView.showError(jsonObject.getString("rtnMsg"));
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
    private void goLogin(){
        mView.clearData();
    }


}
