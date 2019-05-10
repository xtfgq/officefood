package com.xxx.ency.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.xxx.ency.base.RxPresenter;
import com.xxx.ency.config.Constants;
import com.xxx.ency.contract.LoginContract;
import com.xxx.ency.contract.MoveContract;
import com.xxx.ency.model.bean.LoginBean;
import com.xxx.ency.model.bean.PostLogin;
import com.xxx.ency.model.http.BingApi;
import com.xxx.ency.view.move.PostYuncheng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovePresenter extends RxPresenter<MoveContract.View>
        implements MoveContract.Presenter{
    private Context context;
    private BingApi bingApi;
    @Inject
    public MovePresenter(BingApi bingApi, Context context) {
        this.bingApi = bingApi;
        this.context = context;
    }

    @Override
    public void move() {
        PostYuncheng postYuncheng=new PostYuncheng();
        postYuncheng.setId(mView.getRoomId());
        Gson gson=new Gson();
        String route= gson.toJson(postYuncheng);
        Call<ResponseBody> call=bingApi.postToken(Constants.token,"mobileCheckView",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        mView.showSuccuss(jsonObject.getJSONObject("res").toString());
                    }else if(code==Constants.NET_CODE_LOGIN&&mView!=null){
                        mView.clearData();
                    }
                    else {
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
    public void postSave() {
        PostYuncheng postYuncheng=new PostYuncheng();
        postYuncheng.setGrainStoreRoomId(mView.getRoomId());
        postYuncheng.setAbnormalDesc(mView.getContent());
        postYuncheng.setIsAbnormal(mView.getNormal()+"");
        postYuncheng.setUserId(mView.getUserId());
        postYuncheng.setType(mView.getType());
        Gson gson=new Gson();
        String route= gson.toJson(postYuncheng);
        Call<ResponseBody> call=bingApi.postToken(Constants.token,"mobileCheckAdd",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        mView.addSucuss();
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

}
