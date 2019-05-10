package com.xxx.ency.presenter;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xxx.ency.base.BaseSubscriber;
import com.xxx.ency.base.RxBus;
import com.xxx.ency.base.RxPresenter;
import com.xxx.ency.config.Constants;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.MainContract;
import com.xxx.ency.model.bean.LoginBean;
import com.xxx.ency.model.bean.PostCheckItem;
import com.xxx.ency.model.bean.PostGetUser;
import com.xxx.ency.model.bean.PostLocation;
import com.xxx.ency.model.bean.PostMsgNewBean;
import com.xxx.ency.model.bean.Result;
import com.xxx.ency.model.bean.SpaceBean;
import com.xxx.ency.model.bean.UpdateBean;
import com.xxx.ency.model.bean.User;
import com.xxx.ency.model.bean.WeatherBean;
import com.xxx.ency.model.bean.WeatherPostBean;
import com.xxx.ency.model.bean.WeiXinBean;
import com.xxx.ency.model.http.UpdateApi;
import com.xxx.ency.model.http.WeatherApi;
import com.xxx.ency.util.AppApplicationUtil;
import com.xxx.ency.util.DateUtil;
import com.xxx.ency.util.JsonUtil;
import com.xxx.ency.view.main.MainActivity;
import com.xxx.ency.view.main.WorkType;
import com.xxx.ency.view.perfect.UserInfo;
import com.xxx.ency.view.work.WorkTypeData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xiarh on 2017/9/25.
 */

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {

    private WeatherApi weatherApi;

    private UpdateApi updateApi;

    private RxPermissions rxPermissions;

    private Context context;

    @Inject
    public MainPresenter(WeatherApi weatherApi, UpdateApi updateApi, RxPermissions rxPermissions, Context context) {
        this.weatherApi = weatherApi;
        this.updateApi = updateApi;
        this.rxPermissions = rxPermissions;
        this.context = context;
    }

    @Override
    public void checkUpdate() {
        addSubscribe(updateApi.getVersionInfo(Constants.FIR_IM_API_TOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseSubscriber<UpdateBean>(context, mView) {

                    @Override
                    public void onNext(UpdateBean updateBean) {
                        if (null != updateBean) {
                            if (AppApplicationUtil.getVersionCode(context) < updateBean.getVersion()) {
                                mView.showUpdateDialog(updateBean);
                            } else if (AppApplicationUtil.getVersionCode(context) == updateBean.getVersion()) {
                                mView.showUpdateDialog(null);
                            }
                        }
                    }
                }));
    }

    @Override
    public void checkPermissions() {
        addSubscribe(rxPermissions.request(Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA
                , Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        // 当所有权限都允许之后，返回true
                        if (aBoolean) {
                            mView.getPermissionSuccess();
                        }
                        // 只要有一个权限禁止，返回false，
                        // 下一次申请只申请没通过申请的权限
                        else {
                            mView.showPermissionDialog();
                        }
                    }
                }));
    }
    @Override
    public void checkPermissionsScan() {
        addSubscribe(rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        // 当所有权限都允许之后，返回true
                        if (aBoolean) {
                            mView.getPermissionScanSuccess();

                        }
                        // 只要有一个权限禁止，返回false，
                        // 下一次申请只申请没通过申请的权限
                        else {
                            mView.showPermissionDialog();
                        }
                    }
                }));
    }



    @Override
    public void checkLocaiton() {
        PostLocation postLocation=new PostLocation();
        postLocation.setId(mView.roomId());
        postLocation.setType("1");
        postLocation.setLatitude(mView.getlatitude());
        postLocation.setLongitude(mView.getlongitude());
        Gson gson=new Gson();
        String route= gson.toJson(postLocation);
        Call<ResponseBody> call=weatherApi.postToken(Constants.token,"locationCheck",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS){
                        boolean isCheck=jsonObject.getBoolean("res");
                        if(isCheck)   mView.showLocaiton();
                        else mView.showError("请到仓库附近工作");

                    }else {
                        mView.showError("请到仓库附近工作");
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
    public void getWeather(final String location) {

        WeatherPostBean cmsBean=new WeatherPostBean();
        cmsBean.setCity(location);
        Gson gson=new Gson();
        String route= gson.toJson(cmsBean);
        Call<ResponseBody> call=weatherApi.postWeather("weatherForecast",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS){
                        JSONObject resultResultAll=jsonObject.getJSONObject("res");
                        WeatherBean.HeWeather6Bean.NowBean nowBean=new WeatherBean.HeWeather6Bean.NowBean();
                        JSONObject resultResult=resultResultAll.getJSONObject("weather");
                        nowBean.setCloud(resultResult.getString("cloud"));
                        nowBean.setCond_code(resultResult.getString("cond_code"));
                        nowBean.setCond_txt(resultResult.getString("cond_txt"));
                        nowBean.setFl(resultResult.getString("fl"));
                        nowBean.setHum(resultResult.getString("hum"));
                        nowBean.setTmp(resultResult.getString("tmp"));
                        nowBean.setWind_deg(resultResult.getString("wind_deg"));
                        nowBean.setVis(resultResult.getString("vis"));
                        nowBean.setWind_dir(resultResult.getString("wind_dir"));
                        nowBean.setPcpn(resultResult.getString("pcpn"));
                        nowBean.setPres(resultResult.getString("pres"));
                        nowBean.setWind_sc(resultResult.getString("wind_sc"));
                        JSONObject air=resultResultAll.getJSONObject("air");
                        nowBean.setPm25(air.getString("pm25"));
                        nowBean.setCtime(DateUtil.StringPattern(air.getString("pub_time"),"yyyy-MM-dd HH:mm",
                                "yyyy年MM月dd日"));
                        nowBean.setCity(location);
                        mView.showWeather(nowBean);
                    }else if(code==Constants.NET_CODE_LOGIN){
                        goLogin();
                    }
                    else {
                        mView.showError(jsonObject.getString("rtnMsg"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mView.showError("操作失败");

            }
        });


    }
    boolean isShow=false;
    private void goLogin(){
        if(!isShow) {
            mView.clearData();
            isShow=true;
        }

    }
    @Override
    public void getPersonUser() {
        UserInfo userInfo=new UserInfo();
        userInfo.setId(mView.getUserid());
        Gson gson=new Gson();
        String route= gson.toJson(userInfo);

        Call<ResponseBody> call=weatherApi.postToken(Constants.token,"findUserById",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS){
                        JSONObject resObj=jsonObject.getJSONObject("res");
                        LoginBean lb=new LoginBean();
                        if(resObj.has("portrait")){
                            lb.setPortrait(resObj.getString("portrait"));
                        }
                        lb.setUsername(resObj.getString("realName"));
                        mView.showLoginBean(lb);
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

    @Override
    public void getWorkType() {
        WorkType workType=new WorkType();
        workType.setType("17");
        Gson gson=new Gson();
        String route= gson.toJson(workType);
        Call<ResponseBody> call=weatherApi.postToken(Constants.token,"dicQuery",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        JSONArray jsonArray=jsonObject.getJSONArray("res");
                        EncyApplication.getInstance().workMap.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            WorkTypeData workTypeData=new WorkTypeData();
                            workTypeData.setCode(obj.getString("code"));
                            workTypeData.setName(obj.getString("name"));
                            EncyApplication.getInstance().workMap.put(obj.getInt("code"),workTypeData);

                        }
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
    public void setDayOrNight() {
        addSubscribe(RxBus.getInstance().register(Integer.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseSubscriber<Integer>(context, mView) {
                    @Override
                    public void onNext(Integer integer) {
                        if (integer == 1000) {
                            mView.changeDayOrNight(true);
                        }
                    }
                }));
    }

    @Override
    public void getMsgNum() {
        PostMsgNewBean cmsBean=new PostMsgNewBean();
        cmsBean.setReceiveUserId(mView.getUserid());
        cmsBean.setStatus("0");
        Gson gson=new Gson();
        String route= gson.toJson(cmsBean);
        Call<ResponseBody> call=weatherApi.postToken(Constants.token,"msgCount",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        int num=jsonObject.getInt("res");
                        mView.showMsgNum(num);
                    }else if(code==Constants.NET_CODE_LOGIN&&mView!=null){
                        goLogin();
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
        postGetUser.setUserId(mView.getUserid());
        Gson gson=new Gson();
        String route= gson.toJson(postGetUser);
        Call<ResponseBody> call=weatherApi.postToken(Constants.token,"findByRealNameLike",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        JSONArray array = jsonObject.getJSONArray("res");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            EncyApplication.getInstance().userMap.put(object.getString("id"),
                                    object.getString("realName"));
                        }


                     } else if(code==Constants.NET_CODE_LOGIN&&mView!=null){
                        goLogin();
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
    public void getRoomData() {
        User user=new User();
        user.setUserId(mView.getUserid());
        Gson gson=new Gson();
        String route= gson.toJson(user);
        Call<ResponseBody> call=weatherApi.postToken(Constants.token,"grainStoreroomList",route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        JSONArray jsonArray=jsonObject.getJSONArray("res");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj=jsonArray.getJSONObject(i);
                            EncyApplication.getInstance().
                                    roomMap.put(obj.getString("id"),obj.getString("name"));

                        }


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
        Call<ResponseBody> call=weatherApi.postToken(Constants.token,"areaList",route);
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
                          EncyApplication.getInstance().areaMap.put(obj.getString("id"),
                                  obj.getString("name"));


                        }


                    }else if(code==Constants.NET_CODE_LOGIN&&mView!=null){
                        goLogin();
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
    public void setFalse() {
        isShow=false;
    }


}