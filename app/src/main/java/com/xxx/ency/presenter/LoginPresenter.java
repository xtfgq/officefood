package com.xxx.ency.presenter;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;
import com.xxx.ency.base.BaseSubscriber;
import com.xxx.ency.base.RxPresenter;
import com.xxx.ency.config.Constants;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.LoginContract;
import com.xxx.ency.model.bean.BingBean;
import com.xxx.ency.model.bean.LoginBean;
import com.xxx.ency.model.bean.PostLogin;
import com.xxx.ency.model.bean.Result;
import com.xxx.ency.model.bean.User;
import com.xxx.ency.model.http.BingApi;
import com.xxx.ency.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter extends RxPresenter<LoginContract.View>
        implements LoginContract.Presenter{
    private Context context;
    private BingApi bingApi;
    @Inject
    public LoginPresenter(BingApi bingApi, Context context) {
        this.bingApi = bingApi;
        this.context = context;
    }
    @Override
    public void login(String deviceid) {

        PostLogin cmsBean=new PostLogin();
        cmsBean.setDeviceId(deviceid);
        cmsBean.setPassword(mView.getPwd());
        cmsBean.setUserName(mView.getUserName());
        cmsBean.setToken(Constants.XINGE);
        Gson gson=new Gson();
        String route= gson.toJson(cmsBean);
        Call<ResponseBody> call=bingApi.postLogin("userLoginByNameAndPassword",
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
                        lb.setToken(resObj.getString("token"));
                        lb.setModel(resObj.getInt("roleId"));
                        lb.setUsername(resObj.getString("realName"));
                        lb.setUserid(resObj.getString("userId"));
                        mView.showLoginBean(lb);
                    }else {
                        if(mView!=null)
                        mView.showMsg(jsonObject.getString("rtnMsg"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(mView!=null)
                    mView.showError(e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    if(mView!=null)
                    mView.showError(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(mView!=null)
                mView.showError(t.getMessage());
            }
        });



    }
    public void getRoomData() {
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
                        EncyApplication.getInstance().roomMap.clear();
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj=jsonArray.getJSONObject(i);
                            EncyApplication.getInstance().
                                    roomMap.put(obj.getString("id"),obj.getString("name"));

                        }
                        mView.showSucussFinsh();
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
