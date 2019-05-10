package com.xxx.ency.presenter;

import android.content.Context;
import android.view.View;


import com.amap.api.services.core.LatLonPoint;
import com.google.gson.Gson;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.xxx.ency.base.RxPresenter;

import com.xxx.ency.config.Constants;
import com.xxx.ency.contract.UpAdressContract;

import com.xxx.ency.model.bean.PostBeanWeituo;
import com.xxx.ency.model.bean.PostMap;
import com.xxx.ency.model.bean.SpaceBean;
import com.xxx.ency.model.bean.User;
import com.xxx.ency.model.http.BingApi;

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


public class UpAdressPresenter extends RxPresenter<UpAdressContract.View>
        implements UpAdressContract.Presenter{
    private Context context;
    private BingApi bingApi;
    @Inject
    public UpAdressPresenter(BingApi bingApi, Context context) {
        this.bingApi = bingApi;
        this.context = context;
    }


    @Override
    public void getAdress(String address) {
        mView.showAdress(address);

    }

    @Override
    public void postLation(LatLonPoint latLng,String title) {

        PostMap postMap=new PostMap();
        String method="";
        if(title.contains("区域")){
            postMap.setId(mView.getAreaId());
            method="areaUpdate";
        }else {
            postMap.setId(mView.getRoomId());
            method="grainStoreroomUpdate";
        }
        postMap.setLatitude(latLng.getLatitude()+"");
        postMap.setLongitude(latLng.getLongitude()+"");
        Gson gson=new Gson();
        String route= gson.toJson(postMap);

        Call<ResponseBody> call=bingApi.postToken(Constants.token,method,route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        mView.postLationComplete("成功");
                    }else {
                        if(mView!=null)
                        mView.showError("上传位置失败");
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
    public void getData() {
        User user=new User();
        user.setUserId(mView.getUserid());
        Gson gson=new Gson();
        String route= gson.toJson(user);
        Call<ResponseBody> call=bingApi.postToken(Constants.token,"grainStoreroomList",route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        JSONArray jsonArray=jsonObject.getJSONArray("res");
                        List<SpaceBean> list=new ArrayList<>();
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj=jsonArray.getJSONObject(i);
                            SpaceBean bean=new SpaceBean();
                            bean.setName(obj.getString("name"));
                            bean.setNo(obj.getString("id"));
                            list.add(bean);
                        }
                        mView.showSpaceBean(list);

                    }else if(code==Constants.NET_CODE_LOGIN&&mView!=null){
                        goLogin();
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

    @Override
    public void getAreaData() {
        User user=new User();
        user.setUserId(mView.getUserid());
        Gson gson=new Gson();
        String route= gson.toJson(user);
        Call<ResponseBody> call=bingApi.postToken(Constants.token,"areaList",route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        JSONArray jsonArray=jsonObject.getJSONArray("res");
                        List<SpaceBean> list=new ArrayList<>();
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj=jsonArray.getJSONObject(i);
                            SpaceBean bean=new SpaceBean();
                            bean.setName(obj.getString("name"));
                            bean.setNo(obj.getString("id"));
                            list.add(bean);
                        }
                        mView.showArea(list);

                    }else if(code==Constants.NET_CODE_LOGIN&&mView!=null){
                        goLogin();
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

    @Override
    public void postAddWeituo(String startData,String endDate,String id) {
        PostBeanWeituo postBeanWeituo=new PostBeanWeituo();
        postBeanWeituo.setUserId(mView.getUserid());
        postBeanWeituo.setToUserId(mView.getToUserid());
        postBeanWeituo.setStartDate(startData);
        postBeanWeituo.setEndDate(endDate);
        if("1".equals(mView.type())){
            postBeanWeituo.setType("2");
        }else {
            postBeanWeituo.setType("1");
        }
        postBeanWeituo.setData(id);
        Gson gson=new Gson();
        String route= gson.toJson(postBeanWeituo);
        Call<ResponseBody> call=bingApi.postToken(Constants.token,"deputeAdd",route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS){
                        mView.postLationComplete("成功");
                    }else {
                        mView.showError("失败");
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

    }
}
