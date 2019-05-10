package com.xxx.ency.presenter;

import android.content.Context;
import android.view.View;

import com.google.gson.Gson;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.xxx.ency.base.RxPresenter;
import com.xxx.ency.config.Constants;
import com.xxx.ency.contract.AreaContract;
import com.xxx.ency.model.bean.PostAreaManager;
import com.xxx.ency.model.bean.PostLocation;
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

public class AreaPresenter extends RxPresenter<AreaContract.View> implements
        AreaContract.Presenter{
    private Context context;
    private BingApi bingApi;
    @Inject
    public AreaPresenter(BingApi bingApi, Context context) {
        this.bingApi = bingApi;
        this.context = context;
    }
    @Override
    public void postArea() {
        PostLocation postLocation=new PostLocation();
        postLocation.setId(mView.getAreaId());
        postLocation.setType("2");
        postLocation.setLatitude(mView.getLatitude());
        postLocation.setLongitude(mView.getLongitude());
        Gson gson=new Gson();
        String route= gson.toJson(postLocation);
        Call<ResponseBody> call=bingApi.postToken(Constants.token,"locationCheck",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        boolean isCheck=jsonObject.getBoolean("res");
                        if(isCheck) postReport();
                        else {
                            if(mView!=null)
                            mView.showError("请到仓库附近工作");}

                    }else {
                        if(mView!=null)
                        mView.showError("请到仓库附近工作");
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
    public void getAreaList() {
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

    void postReport(){
        PostAreaManager postAreaManager=new PostAreaManager();
        postAreaManager.setAreaId(mView.getAreaId());
        postAreaManager.setUserId(mView.getUserid());

        if(mView.isHasCrash()==0){
            postAreaManager.setIsAbnormal("0");

        }else {
            postAreaManager.setAbnormalType(mView.crashType()+"");
            postAreaManager.setIsAbnormal("1");
            postAreaManager.setDesc(mView.getDes());

        }


        Gson gson=new Gson();
        String route= gson.toJson(postAreaManager);
        Call<ResponseBody> call=bingApi.postToken(Constants.token,"areaCheckAdd",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        mView.showSucuss();
                    }else if(code==Constants.NET_CODE_LOGIN&&mView!=null){
                        mView.clearData();
                    }
                    else {
                        if(mView!=null)
                        mView.showError(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mView.showError(e.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mView.showError(t.getMessage());
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

}
