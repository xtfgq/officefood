package com.xxx.ency.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.xxx.ency.base.RxPresenter;

import com.xxx.ency.config.Constants;
import com.xxx.ency.contract.ServiceListContract;

import com.xxx.ency.model.bean.JobBean;
import com.xxx.ency.model.bean.JobPostBean;
import com.xxx.ency.model.bean.PostReaportDeatil;
import com.xxx.ency.model.http.WeatherApi;
import com.xxx.ency.util.DateUtil;

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

public class ServicePresenter extends RxPresenter<ServiceListContract.View> implements
        ServiceListContract.Presenter{
    private WeatherApi weatherApi;

    private Context context;

    @Inject
    public ServicePresenter(WeatherApi weatherApi, Context context) {
        this.weatherApi = weatherApi;
        this.context = context;
    }
    @Override
    public void getData(int size, int page) {
        if(mView.getTitle().contains("安全")) {
            JobPostBean bean = new JobPostBean();
            bean.setPageSize(size + "");
            bean.setUserId(mView.getUserid());
            bean.setCurrentPage(page + "");
            Gson gson = new Gson();
            String route = gson.toJson(bean);
            Call<ResponseBody> call = weatherApi.postToken(Constants.token, "areaCheckList",
                    route);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.getInt("rtnCode");
                        if (code == Constants.NET_CODE_SUCCESS&&mView!=null) {
                            JSONArray array = jsonObject.getJSONArray("res");
                            List<JobBean> jobBeans = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                JobBean jobBean1 = new JobBean();
                                jobBean1.setIsAbnormal(object.getString("isAbnormal"));
                                jobBean1.setStrCreateDate(object.getString("strCreateDate"));
                                jobBean1.setAreaName(object.getString("areaName"));
                                jobBean1.setData(object.getString("strCreateDate"));
                                if(object.has("abnormalTypeName")) {
                                    jobBean1.setAbnormalTypeName(object.getString("abnormalTypeName"));
                                }
                                if(object.has("desc")) {
                                    jobBean1.setDes(object.getString("desc"));
                                }

//                                jobBean1.setData();
                                jobBeans.add(jobBean1);
                            }
                            mView.showData(jobBeans);
                        }else if(code==Constants.NET_CODE_LOGIN&&mView!=null){
                            mView.clearData();
                        }else {
                            if(mView!=null)
                            mView.showError(jsonObject.getString("msg"));
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
        }else if(mView.getTitle().contains("巡仓")){
            JobPostBean bean = new JobPostBean();
            bean.setPageSize(size + "");
            bean.setUserId(mView.getUserid());
            bean.setCurrentPage(page + "");
            Gson gson = new Gson();
            String route = gson.toJson(bean);
            Call<ResponseBody> call = weatherApi.postToken(Constants.token, "mobileCheckList",
                    route);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.getInt("rtnCode");
                        if (code == Constants.NET_CODE_SUCCESS&&mView!=null) {
                            JSONArray array = jsonObject.getJSONArray("res");
                            List<JobBean> jobBeans = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                JobBean jobBean1 = new JobBean();
                                jobBean1.setIsAbnormal(object.getString("isAbnormal"));
                                jobBean1.setAreaName(object.getString("grainStoreRoomName"));
                                jobBean1.setData(object.getString("createDate"));
                                jobBean1.setType(object.getString("type"));
                                jobBean1.setDes(object.getString("abnormalDesc"));
                                jobBean1.setGrainStoreRoomId(object.getString("grainStoreRoomId"));
                                jobBeans.add(jobBean1);
                            }
                            mView.showData(jobBeans);
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

}
