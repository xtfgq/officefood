package com.xxx.ency.presenter;

import android.content.Context;
import com.google.gson.Gson;
import com.xxx.ency.base.RxPresenter;
import com.xxx.ency.config.Constants;
import com.xxx.ency.contract.GankContract;
import com.xxx.ency.model.bean.DeputeList;
import com.xxx.ency.model.bean.JobBean;
import com.xxx.ency.model.bean.JobPostBean;
import com.xxx.ency.model.bean.PostBrow;
import com.xxx.ency.model.bean.PostCheckOfficeItem;
import com.xxx.ency.model.bean.PostMessage;
import com.xxx.ency.model.bean.ServicePost;
import com.xxx.ency.model.http.GankApi;
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
 * Created by xiarh on 2017/11/27.
 */

public class GankPresenter extends RxPresenter<GankContract.View> implements GankContract.Presenter {

    private GankApi gankApi;

    private Context context;

    @Inject
    public GankPresenter(GankApi gankApi, Context context) {
        this.gankApi = gankApi;
        this.context = context;
    }

    @Override
    public void getGankData(final String type, int size,final String typeWork, int page) {
        if(mView.getTitle().contains("作业")) {
            JobPostBean bean = new JobPostBean();
            bean.setStatus(type);
            bean.setType(typeWork);
            bean.setPageSize(size + "");
            bean.setUserId(mView.getUserid());
            bean.setCurrentPage(page + "");
            Gson gson = new Gson();
            String route = gson.toJson(bean);
            Call<ResponseBody> call = gankApi.postToken(Constants.token, "jobList",
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
                                if(object.has("refuseCause")){
                                    jobBean1.setRefuseCause(object.getString("refuseCause"));
                                }
                                jobBean1.setTitle(object.getString("title"));
                                jobBean1.setGrainStoreRoomName(object.getString("grainStoreRoomName"));
                                jobBean1.setData(object.getString("createTime"));
                                jobBean1.setId(object.getString("id"));
                                jobBean1.setGrainStoreRoomId(object.getString("grainStoreRoomId"));
                                jobBean1.setAuditorUserRealName(object.getString("auditorUserRealName"));
                                jobBean1.setAuditorUserId(object.getString("auditorUserId"));
                                jobBean1.setCreateUserRealName(object.getString("createUserRealName"));
                                jobBean1.setCurrentStep(object.getString("currentStep"));

                                jobBeans.add(jobBean1);
                            }
                            if(mView!=null)
                            mView.showGankData(jobBeans);

                        } else if (code == Constants.NET_CODE_LOGIN&&mView!=null) {
                            goLogin();
                        } else {
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
                    mView.showError(t.getMessage());
                }
            });
        }else if(mView.getTitle().contains("送检")){
            PostCheckOfficeItem postCheckOfficeItem=new PostCheckOfficeItem();
            postCheckOfficeItem.setUserId(mView.getUserid());
            postCheckOfficeItem.setIsDeal(type);
            postCheckOfficeItem.setCurrentPage(page+"");
            postCheckOfficeItem.setPageSize(size+"");
            Gson gson = new Gson();
            String route = gson.toJson(postCheckOfficeItem);
            Call<ResponseBody> call = gankApi.postToken(Constants.token, "submitCensorshipList",
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
                                int isDeal=object.getInt("isDeal");
                                jobBean1.setsDeali(isDeal);
                                if(isDeal==0) {
                                    jobBean1.setTitle(object.getString("grainStoreRoomName"));
                                    jobBean1.setCreateUserId(object.getString("createUserId"));
                                    jobBean1.setData(object.getString("createTime"));
                                    jobBean1.setId(object.getString("id"));
                                }else {
                                    jobBean1.setTitle(object.getString("grainStoreRoomName"));
                                    jobBean1.setCreateUserId(object.getString("createUserId"));
                                    jobBean1.setCensorshipUserId(object.getString("censorshipUserId"));
                                    jobBean1.setCensorshipTime(object.getString("censorshipTime"));
                                    jobBean1.setData(object.getString("createTime"));
                                    jobBean1.setId(object.getString("id"));
                                    jobBean1.setResult(object.getString("result"));
                                }
                                jobBeans.add(jobBean1);
                            }
                            mView.showGankData(jobBeans);
                        }else if (code == Constants.NET_CODE_LOGIN&&mView!=null) {
                            goLogin();
                        } else {
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
                    mView.showError(t.getMessage());
                }
            });
        }else if(mView.getTitle().contains("维修")){
            ServicePost servicePost=new ServicePost();
            servicePost.setUserId(mView.getUserid());
            servicePost.setIsDeal(type);
            servicePost.setCurrentPage(page+"");
            servicePost.setPageSize(size+"");
            Gson gson = new Gson();
            String route = gson.toJson(servicePost);
            Call<ResponseBody> call = gankApi.postToken(Constants.token, "repairList",
                    route);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.getInt("rtnCode");
                        if (code == Constants.NET_CODE_SUCCESS) {
                            JSONArray array = jsonObject.getJSONArray("res");
                            List<JobBean> jobBeans = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                JobBean jobBean1 = new JobBean();
                                int isDeal=object.getInt("isDeal");
                                if(isDeal==0) {
                                    jobBean1.setTitle(object.getString("context"));
                                    jobBean1.setData(object.getString("createTime"));
                                    jobBean1.setId(object.getString("id"));
                                    jobBean1.setCreateUserId(object.getString("createUserId"));
                                    jobBean1.setsDeali(isDeal);
                                }else {
                                    jobBean1.setTitle(object.getString("context"));
                                    jobBean1.setData(object.getString("createTime"));
                                    jobBean1.setId(object.getString("id"));
                                    jobBean1.setRepairUserId(object.getString("repairUserId"));
                                    jobBean1.setRepairTime(object.getString("repairTime"));
                                    jobBean1.setsDeali(isDeal);
                                }
                                jobBeans.add(jobBean1);
                            }
                            if(mView!=null)
                            mView.showGankData(jobBeans);
                        }else if (code == Constants.NET_CODE_LOGIN&&mView!=null) {
                            goLogin();
                        } else {
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
                    mView.showError(t.getMessage());
                }
            });

        }else if(mView.getTitle().contains("消息")){
            PostMessage servicePost=new PostMessage();
            if("1".equals(type)) {
                servicePost.setSendUserId(mView.getUserid());
            }else {
                servicePost.setReceiveUserId(mView.getUserid());
            }
            servicePost.setCurrentPage(page+"");
            servicePost.setPageSize(size+"");
            Gson gson = new Gson();
            String route = gson.toJson(servicePost);
            Call<ResponseBody> call = gankApi.postToken(Constants.token, "msgList",
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
                                jobBean1.setTitle(object.getString("title"));
                                jobBean1.setData(object.getString("createTime"));
                                jobBean1.setId(object.getString("id"));
                                jobBean1.setResult(object.getString("content"));
                                jobBean1.setStatus(object.getInt("status"));
                                jobBean1.setReceiveUserId(object.getString("receiveUserRealName"));
                                jobBean1.setSendUserId(object.getString("sendUserRealName"));
                                jobBean1.setType(type);
                                jobBeans.add(jobBean1);
                            }
                            mView.showGankData(jobBeans);
                        }else if (code == Constants.NET_CODE_LOGIN&&mView!=null) {
                            goLogin();
                        } else {
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
                    mView.showError(t.getMessage());
                }
            });

        }else if(mView.getTitle().contains("日志")){
            PostBrow pb=new PostBrow();
            pb.setUserId(mView.getUserid());
            pb.setGrainStoreRoomId(mView.strRoomId());
            pb.setCurrentPage(page+"");
            pb.setPageSize(size+"");
            pb.setType(type);
            Gson gson = new Gson();
            String route = gson.toJson(pb);
            Call<ResponseBody> call = gankApi.postToken(Constants.token, "checkReportList",
                    route);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.getInt("rtnCode");
                        if (code == Constants.NET_CODE_SUCCESS&&mView!=null) {
                            JSONArray array = jsonObject.getJSONArray("res");
//                            Log.e("vvv", array.getJSONObject(0).toString());
                            List<JobBean> jobBeans = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                JobBean jobBean1 = new JobBean();
                                jobBean1.setTitle(object.getString("grainStoreRoomName"));
                                jobBean1.setData(object.getString("createDate"));
                                jobBean1.setId(object.getString("grainStoreRoomId"));

                                jobBeans.add(jobBean1);
                            }
                            mView.showGankData(jobBeans);
                        }else if (code == Constants.NET_CODE_LOGIN) {
                            if(mView!=null)
                            goLogin();
                        } else {
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
                    if(mView!=null)
                    mView.showError(t.getMessage());

                }
            });

        }else if(mView.getTitle().contains("委托")){
            DeputeList deputeList=new DeputeList();
            if("1".equals(type)) {
                deputeList.setToUserId(mView.getUserid());
            }else {
                deputeList.setUserId(mView.getUserid());
            }
            deputeList.setCurrentPage(page+"");
            deputeList.setPageSize(size+"");
            Gson gson = new Gson();
            String route = gson.toJson(deputeList);
            Call<ResponseBody> call = gankApi.postToken(Constants.token, "deputeList",
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
                                jobBean1.setName(object.getString("name"));
                                jobBean1.setType(object.getString("type"));
                                jobBean1.setIsAbnormal(type);
                                if("1".equals(type)) {
                                    jobBean1.setCreateUserRealName( object.getString("realName"));
                                }else {
                                 jobBean1.setAuditorUserRealName(object.getString("toRealName"));
                                }
                                jobBean1.setData(object.getString("startDate"));
                                jobBean1.setId(object.getString("id"));
                                jobBean1.setDes("委托时间："+object.getString("startDate") + "-" + object.getString("endDate"));
                                jobBeans.add(jobBean1);
                            }
                            mView.showGankData(jobBeans);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if(mView!=null)
                    mView.showError(t.getMessage());
                }
            });
        }else if(mView.getTitle().contains("任务")){
            DeputeList deputeList=new DeputeList();
            if("2".equals(type)) {
                deputeList.setToUserId(mView.getUserid());
            }else {
                deputeList.setUserId(mView.getUserid());
            }
            deputeList.setCurrentPage(page+"");
            deputeList.setPageSize(size+"");
            Gson gson = new Gson();

            String route = gson.toJson(deputeList);
            Call<ResponseBody> call = gankApi.postToken(Constants.token, "taskList",
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

                                jobBean1.setIsAbnormal(type);
                                jobBean1.setCreateUserId( object.getString("userId"));
                                jobBean1.setAuditorUserId(object.getString("toUserId"));
                                jobBean1.setData(object.getString("strStartDate"));
                                jobBean1.setStatus(object.getInt("status"));
                                jobBean1.setId(object.getString("id"));
                                jobBean1.setContent(object.getString("content"));
                                jobBean1.setDes(object.getString("strStartDate") + "-" + object.getString("strEndDate"));
                                jobBeans.add(jobBean1);
                            }
                            mView.showGankData(jobBeans);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if(mView!=null)
                    mView.showError(t.getMessage());
                }
            });
        }



    }

    @Override
    public void postCheck(String id,String res) {
        PostCheckOfficeItem postCheckOfficeItem=new PostCheckOfficeItem();
        postCheckOfficeItem.setUserId(mView.getUserid());
        postCheckOfficeItem.setIsDeal("1");
        postCheckOfficeItem.setId(id);
        postCheckOfficeItem.setResult(res);
        Gson gson = new Gson();
        String route = gson.toJson(postCheckOfficeItem);
        Call<ResponseBody> call = gankApi.postToken(Constants.token, "submitCensorshipUpdate",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    int code = jsonObject.getInt("rtnCode");
                    if (code == Constants.NET_CODE_SUCCESS&&mView!=null) {
                        mView.sucuss();
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
    public void postResult(String id) {
        PostCheckOfficeItem postCheckOfficeItem=new PostCheckOfficeItem();
        postCheckOfficeItem.setUserId(mView.getUserid());
        postCheckOfficeItem.setIsDeal("1");
        postCheckOfficeItem.setId(id);
        Gson gson = new Gson();
        String route = gson.toJson(postCheckOfficeItem);
        Call<ResponseBody> call = gankApi.postToken(Constants.token, "repairUpdate",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    int code = jsonObject.getInt("rtnCode");
                    if (code == Constants.NET_CODE_SUCCESS&&mView!=null) {
                        mView.sucuss();
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
