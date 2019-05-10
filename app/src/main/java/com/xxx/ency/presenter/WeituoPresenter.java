package com.xxx.ency.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xxx.ency.base.BaseSubscriber;
import com.xxx.ency.base.RxBus;
import com.xxx.ency.base.RxPresenter;
import com.xxx.ency.config.Constants;
import com.xxx.ency.contract.LoginContract;

import com.xxx.ency.contract.WeituoContract;
import com.xxx.ency.model.bean.LoginBean;
import com.xxx.ency.model.bean.PostGetUser;
import com.xxx.ency.model.http.BingApi;
import com.xxx.ency.view.work.MultipleType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by xiarh on 2017/11/8.
 */

public class WeituoPresenter extends RxPresenter<WeituoContract.View> implements WeituoContract.Presenter {
    private Context context;
    private BingApi bingApi;
    @Inject
    public WeituoPresenter(BingApi bingApi, Context context) {
        this.bingApi = bingApi;
        this.context = context;
    }

    @Override
    public void postAdd() {

        PostGetUser postGetUser=new PostGetUser();
        postGetUser.setUserId(mView.getUserId());
        Gson gson=new Gson();
        String route= gson.toJson(postGetUser);
        Call<ResponseBody> call=bingApi.postToken(Constants.token,"subordinate",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        JSONArray array = jsonObject.getJSONArray("res");
                        List<LoginBean> jobBeans = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            if(!object.getString("id").equals(mView.getUserId())) {
                                LoginBean jobBean1 = new LoginBean();
                                jobBean1.setUserid(object.getString("id"));
                                jobBean1.setUsername(object.getString("realName"));
                                jobBeans.add(jobBean1);
                            }
                        }
                        mView.showUser(jobBeans);

                    }else if(code==Constants.NET_CODE_LOGIN&&mView!=null){

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
    public void getUser() {
        PostGetUser postGetUser=new PostGetUser();
        postGetUser.setUserId(mView.getUserId());
        if(!TextUtils.isEmpty(mView.getRoleId())){
            postGetUser.setRoleId(mView.getRoleId());
        }
        Gson gson=new Gson();
        String route= gson.toJson(postGetUser);
        Call<ResponseBody> call=bingApi.postToken(Constants.token,"findByRealNameLike",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        JSONArray array = jsonObject.getJSONArray("res");
                        List<LoginBean> jobBeans = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            if(!object.getString("id").equals(mView.getUserId())) {
                                LoginBean jobBean1 = new LoginBean();
                                jobBean1.setUserid(object.getString("id"));
                                jobBean1.setUsername(object.getString("realName"));
                                jobBeans.add(jobBean1);
                            }
                        }
                        mView.showUser(jobBeans);

                    }else if(code==Constants.NET_CODE_LOGIN){

                    }
                    else {
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
    @Override
    public void postMultipleType(String roleId, final String multipleTypeStr, String id) {
        MultipleType pd=new MultipleType();
        pd.setAuditorUserId(roleId);
        pd.setId(id);
        pd.setStep(multipleTypeStr);
        Gson gson=new Gson();
        String route= gson.toJson(pd);
        Call<ResponseBody> call=bingApi.postToken(Constants.token,"jobApplyStep",route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                       mView.showScuss();

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
