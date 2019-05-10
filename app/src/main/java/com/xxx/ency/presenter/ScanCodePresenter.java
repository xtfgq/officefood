package com.xxx.ency.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.xxx.ency.base.RxPresenter;
import com.xxx.ency.config.Constants;
import com.xxx.ency.contract.LoginContract;
import com.xxx.ency.contract.ScanCodeContract;
import com.xxx.ency.model.bean.LoginBean;
import com.xxx.ency.model.bean.PostLocation;
import com.xxx.ency.model.bean.PostLogin;
import com.xxx.ency.model.http.BingApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanCodePresenter extends RxPresenter<ScanCodeContract.View>
        implements ScanCodeContract.Presenter{
    private Context context;
    private BingApi bingApi;
    @Inject
    public ScanCodePresenter(BingApi bingApi, Context context) {
        this.bingApi = bingApi;
        this.context = context;
    }
//    @Override
//    public void login(String deviceid) {
//
//        PostLogin cmsBean=new PostLogin();
//        cmsBean.setDeviceId(deviceid);
//        cmsBean.setPassword(mView.getPwd());
//        cmsBean.setUserName(mView.getUserName());
//        cmsBean.setToken(Constants.XINGE);
//        Gson gson=new Gson();
//        String route= gson.toJson(cmsBean);
//        Call<ResponseBody> call=bingApi.postLogin("userLoginByNameAndPassword",
//                route);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    JSONObject jsonObject=new JSONObject(response.body().string());
//                    int code=jsonObject.getInt("rtnCode");
//                    if(code==Constants.NET_CODE_SUCCESS){
//                        JSONObject resObj=jsonObject.getJSONObject("res");
//                        LoginBean lb=new LoginBean();
//                        lb.setToken(resObj.getString("token"));
//                        lb.setModel(resObj.getInt("roleId"));
//                        lb.setUsername(mView.getUserName());
//                        lb.setUserid(resObj.getString("userId"));
//                        mView.showLoginBean(lb);
//                    }else {
//                        mView.showError(jsonObject.getString("rtnMsg"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    mView.showError(e.getMessage());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    mView.showError(e.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                mView.showError(t.getMessage());
//            }
//        });
//
//
//
//    }


    @Override
    public void checkCode() {
        PostLocation postLocation=new PostLocation();
        postLocation.setId(mView.getRoomId());
        postLocation.setType(mView.getType());
        postLocation.setLatitude(mView.getlatitude());
        postLocation.setLongitude(mView.getlongitude());
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
                        if(isCheck)   mView.showLocaiton();
                        else mView.showToast("请到仓库附近工作");

                    }else {
                        if(mView!=null)
                        mView.showToast("请到仓库附近工作");
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
}
