package com.xxx.ency.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xxx.ency.base.RxPresenter;
import com.xxx.ency.config.Constants;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.AddWorkActivityContract;
import com.xxx.ency.model.bean.PostCheckOfficeItem;
import com.xxx.ency.model.bean.PostLocation;
import com.xxx.ency.model.bean.PostStatusBean;
import com.xxx.ency.model.bean.SpaceBean;
import com.xxx.ency.model.bean.User;
import com.xxx.ency.model.http.BingApi;
import com.xxx.ency.model.http.WeiXinApi;
import com.xxx.ency.view.work.AddWorkActivity;
import com.xxx.ency.view.work.MultipleType;
import com.xxx.ency.view.work.PostAdd;
import com.xxx.ency.view.work.WorkTypeData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddWorkPresenter extends RxPresenter<AddWorkActivityContract.View> implements
        AddWorkActivityContract.Presenter{
    private Context context;
    private BingApi weiXinApi;
    @Inject
    public AddWorkPresenter(BingApi bingApi, Context context) {
        this.weiXinApi = bingApi;
        this.context = context;
    }
    @Override
    public void getData() {
        User user=new User();
        user.setUserId(mView.getUserid());
        Gson gson=new Gson();
        String route= gson.toJson(user);
        Call<ResponseBody> call=weiXinApi.postToken(Constants.token,"grainStoreroomList",route);
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
    public void postAdd(String roleId,String type) {
        postReport(roleId,type);

    }

    /**
     * 申请作业
     */
    void postReport(final String roleId,final String typeStr){

            PostAdd pd=new PostAdd();
            pd.setCreateUserId(mView.getUserid());
            pd.setGrainStoreRoomId(mView.getRoomId());
            pd.setAuditorUserId(roleId);
            pd.setStatus("1");
            pd.setStep("1");
             WorkTypeData workTypeData= (WorkTypeData) EncyApplication.getInstance().workMap.
                get(mView.getType());
            pd.setType(typeStr);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            pd.setTitle(df.format(new Date())+workTypeData.getName());
            Gson gson=new Gson();
            String route= gson.toJson(pd);
            Call<ResponseBody> call=weiXinApi.postToken(Constants.token,"JobAdd",route);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObject=new JSONObject(response.body().string());
                        int code=jsonObject.getInt("rtnCode");
                        if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                            mView.sucuss(jsonObject.getString("res"));
//                            jobUp(roleId,typeStr);
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
    public void postCheck() {
        PostCheckOfficeItem postCheckOfficeItem=new PostCheckOfficeItem();
        postCheckOfficeItem.setUserId(mView.getUserid());
        postCheckOfficeItem.setIsDeal("1");
        postCheckOfficeItem.setGrainStoreRoomid(mView.getRoomId());
        Gson gson=new Gson();
        String route= gson.toJson(postCheckOfficeItem);
        Call<ResponseBody> call=weiXinApi.postToken(Constants.token,"submitCensorshipAdd",route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        mView.sucuss();

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
    public void jobUp(String jobId,String typeStr){
        PostStatusBean postStatusBean=new PostStatusBean();
        postStatusBean.setAuditorUserId(mView.getUserid());
        postStatusBean.setId(jobId);
        postStatusBean.setStatus("0");
        postStatusBean.setType(typeStr);

        Gson gson=new Gson();
        String route= gson.toJson(postStatusBean);
        Call<ResponseBody> call=weiXinApi.postToken(Constants.token,"jobUpdate",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){


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
