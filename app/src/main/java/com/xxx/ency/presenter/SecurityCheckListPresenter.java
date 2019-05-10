package com.xxx.ency.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.xxx.ency.base.RxPresenter;

import com.xxx.ency.config.Constants;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.SecurityCheckListContract;
import com.xxx.ency.model.bean.CheckListBean;
import com.xxx.ency.model.bean.PostCheckItem;
import com.xxx.ency.model.bean.PostCheckList;
import com.xxx.ency.model.bean.PostReaportDeatil;

import com.xxx.ency.model.bean.PostStatusBean;
import com.xxx.ency.model.bean.checkItemValueList;
import com.xxx.ency.model.http.WeiXinApi;
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
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xiarh on 2017/11/1.
 */

public class SecurityCheckListPresenter extends RxPresenter<SecurityCheckListContract.View>
        implements SecurityCheckListContract.Presenter {

    private Context context;

    private WeiXinApi weiXinApi;

    @Inject
    public SecurityCheckListPresenter(WeiXinApi weiXinApi,Context context) {
        this.weiXinApi=weiXinApi;
        this.context = context;
    }
    @Override
    public void getCheckData() {
        PostCheckItem postCheckItem=new PostCheckItem();
        postCheckItem.setType(mView.getType());
        int typeNum=Integer.valueOf(mView.getType());
        if(typeNum>2) {
            postCheckItem.setStep(mView.getStep());
            postCheckItem.setJobId(mView.getJobID());
        }
        Gson gson = new Gson();
        String route = gson.toJson(postCheckItem);
        Call<ResponseBody> call = weiXinApi.postToken(Constants.token, "checkList",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                List<CheckListBean> list = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    int code = jsonObject.getInt("rtnCode");

                    if (code == Constants.NET_CODE_SUCCESS) {
                        JSONArray array = jsonObject.getJSONArray("res");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String parentTitle = object.getString("name");
                            String parentHead="";
                            if(object.has("tip")) {
                                parentHead= object.getString("tip");
                            }
                            JSONArray childObjArray = object.getJSONArray("children");
                            for (int m = 0; m < childObjArray.length(); m++) {
                                JSONObject childObj = childObjArray.getJSONObject(m);
                                CheckListBean beanlist = new CheckListBean();
                                beanlist.setParentTitle(parentTitle);
                                beanlist.setParentTip(parentHead);
                                beanlist.setChildTitle(childObj.getString("lable"));
                                beanlist.setChildHints(childObj.getString("valueDesc"));
                                beanlist.setName(childObj.getString("name"));
                               if(childObj.has("tip")) {
                                   beanlist.setTip(childObj.getString("tip"));
                               }else {
                                   beanlist.setTip("");
                               }
                                int type = childObj.getInt("dataType");
                                beanlist.setType(type);
                                beanlist.setCheckId(childObj.getString("checkId"));
                                beanlist.setId(childObj.getString("id"));
                                if (type == 5) {
                                    String[] strsValues = {"是", "否"};
                                    String[] strsItems = {"1", "0"};
                                    beanlist.setStrValues(strsValues);
                                    beanlist.setStritems(strsItems);
                                    beanlist.setTxtValue("");
                                } else if (type == -5) {
                                    String[] strsValues = {"是", "否"};
                                    String[] strsItems = {"0", "1"};
                                    beanlist.setStrValues(strsValues);
                                    beanlist.setStritems(strsItems);
                                    beanlist.setTxtValue("");
                                } else {
                                    if (childObj.has("data") && !TextUtils.isEmpty(childObj.getString(
                                            "data"))) {
                                        if(type==8){
                                            String data = childObj.getString("data");
                                            JSONObject childObj1= new JSONObject(data);
                                            List<String> strValues = new ArrayList<>();
                                            List<String> strItems = new ArrayList<>();
                                            strValues.add(childObj1.getString("start"));
                                            strValues.add(childObj1.getString("end"));
                                            strItems.add(childObj1.getString("start"));
                                            strItems.add(childObj1.getString("end"));

                                            String[] strsValues = strValues.toArray(new String[strValues.size()]);
                                            String[] strsItems = strItems.toArray(new String[strItems.size()]);
                                            beanlist.setStrValues(strsValues);
                                            beanlist.setStritems(strsItems);
                                        }else {
                                            String data = childObj.getString("data");
                                            JSONArray childObjData = new JSONArray(data);
                                            List<String> strValues = new ArrayList<>();
                                            List<String> strItems = new ArrayList<>();
                                            for (int n = 0; n < childObjData.length(); n++) {
                                                JSONObject childData = childObjData.getJSONObject(n);
                                                strValues.add(childData.getString("item"));
                                                strItems.add(childData.getString("value"));
                                            }
                                            String[] strsValues = strValues.toArray(new String[strValues.size()]);
                                            String[] strsItems = strItems.toArray(new String[strItems.size()]);
                                            beanlist.setStrValues(strsValues);
                                            beanlist.setStritems(strsItems);
                                        }
                                    } else {

                                        String[] strsValues = {""};
                                        String[] strsItems = {""};
                                        beanlist.setStrValues(strsValues);
                                        beanlist.setStritems(strsItems);
                                    }
                                }
                                if (type == 1) {
                                    beanlist.setTxtValue("否");
                                }
                                list.add(beanlist);
                            }

                        }
                        mView.getCheckList(list);
                    } else if (code == Constants.NET_CODE_LOGIN) {
                        goLogin();
                    } else {
                        mView.showError(jsonObject.getString("rtnMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(mView!=null&&list.size()>0)
                    mView.getCheckList(list);
                } catch (IOException e) {
                    e.printStackTrace();
                    if(mView!=null&&list.size()>0)
                    mView.getCheckList(list);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mView.showError("操作失败");
            }
        });



    }




    @Override
    public void postCheck(Map<String,List<CheckListBean>> saveMap) {
        PostCheckList postCheckList=new PostCheckList();
        postCheckList.setUserId(mView.getUserid());
        postCheckList.setGrainStoreRoomId(mView.getRoomId());
        if(!TextUtils.isEmpty(mView.getStep())){
            postCheckList.setJobId(mView.getJobID());
        }
        List<checkItemValueList> list=new ArrayList<>();
        Gson gson=new Gson();
        for (Map.Entry<String, List<CheckListBean>> entry : saveMap.entrySet()) {
            for(CheckListBean bean:entry.getValue()){
                if(!TextUtils.isEmpty(bean.getShowText())&&!bean.getShowText().equals(bean.getChildHints())) {
                    checkItemValueList checkItem=new checkItemValueList();
                    checkItem.setCheckId(bean.getCheckId());
                    checkItem.setCheckItemId(bean.getId());
                    checkItem.setCheckItemValue(bean.getTxtValue().trim());
                    list.add(checkItem);
                }
            }
        }

        postCheckList.setCheckItemValueList(list);
        String route= gson.toJson(postCheckList);
        Call<ResponseBody> call=weiXinApi.postToken(Constants.token,"checkItemSubmit",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        mView.setEnable();
                         mView.showPostSucuss();
                    }
                    else if(code==Constants.NET_CODE_LOGIN&&mView!=null){
                        mView.setEnable();
                        goLogin();
                    }else {
                        if(mView!=null) {
                            mView.showError(jsonObject.getString("rtnMsg"));
                            mView.setEnable();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(mView!=null) mView.setEnable();
                } catch (Exception e) {
                    e.printStackTrace();
                    if(mView!=null) mView.setEnable();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(mView!=null) mView.setEnable();
            }
        });
    }

    @Override
    public void getReport() {
        PostReaportDeatil pr=new PostReaportDeatil();
        if(TextUtils.isEmpty(mView.getStep())) {
            pr.setType(mView.getType());

            if("3".equals(mView.fromTo())){
                pr.setJobId(mView.getJobID());
            }else {
                pr.setGrainStoreRoomId(mView.getRoomId());
                pr.setCreateDate(mView.getCreateData());
                pr.setUserId(mView.getUserid());
            }

        }else {
            pr.setJobId(mView.getJobID());
            pr.setStep(mView.getStep());
        }

        Gson gson=new Gson();
        String route= gson.toJson(pr);
        Call<ResponseBody> call=weiXinApi.postToken(Constants.token,"checkReport",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        List<CheckListBean> list=new ArrayList<>();
                        JSONArray array=jsonObject.getJSONArray("res");
                        for(int i=0;i<array.length();i++) {
                            JSONObject object = array.getJSONObject(i);
                            String parentTitle=object.getString("checkName");
                            CheckListBean beanlist=new CheckListBean();
                            if(object.has("checkTip")) {
                                beanlist.setParentTip(object.getString("checkTip"));
                            }else {
                                beanlist.setTip("");
                            }
                            if(object.has("checkItemTip")) {
                                beanlist.setTip(object.getString("checkItemTip"));
                            }else {
                                beanlist.setTip("");
                            }
                            beanlist.setParentTitle(parentTitle);
                            beanlist.setChildTitle(object.getString("checkItemName"));
                            if(object.has("checkItemValueDesc")) {
                                beanlist.setShowText(object.getString("checkItemValueDesc"));
                            }
                            list.add(beanlist);
                        }
                        mView.getCheckReport(list);
                    }else if(code==Constants.NET_CODE_LOGIN&&mView!=null){
                        mView.clearData();
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
    public void getReportFan(){
        PostReaportDeatil pr=new PostReaportDeatil();
        pr.setJobId(mView.getJobID());
        pr.setStep("1");
        pr.setType(mView.getType());
        Gson gson=new Gson();
        String route= gson.toJson(pr);
        Call<ResponseBody> call=weiXinApi.postToken(Constants.token,"checkReport",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String funNum="0";
                    String singlePT="0";
                    String tempBefore="0";
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        List<CheckListBean> list=new ArrayList<>();
                        JSONArray array=jsonObject.getJSONArray("res");
                        for(int i=0;i<array.length();i++) {
                            JSONObject object = array.getJSONObject(i);
                            String parentTitle=object.getString("checkName");

                            if(object.getInt("checkItemId")==50){
                                singlePT=object.getString("checkItemValue");
                            }
                            if(object.getInt("checkItemId")==53){
                                funNum=object.getString("checkItemValue");
                            }
                            if(object.getInt("checkItemId")==63){
                                tempBefore=object.getString("checkItemValue");
                            }
                            CheckListBean beanlist=new CheckListBean();
                            beanlist.setParentTitle(parentTitle);
                            beanlist.setChildTitle(object.getString("checkItemName"));
                            beanlist.setShowText(object.getString("checkItemValueDesc"));
                            list.add(beanlist);
                        }
                        mView.getFunNumAndWT(funNum,singlePT,tempBefore);


                    }else if(code==Constants.NET_CODE_LOGIN&&mView!=null){
                        mView.clearData();
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
    public void jobApplyStep(String roleId,String multipleTypeStr ,String
                             jobid) {
        MultipleType pd=new MultipleType();
        pd.setAuditorUserId(roleId);
        pd.setId(jobid);
        pd.setStep(multipleTypeStr);
        Gson gson=new Gson();
        String route= gson.toJson(pd);
        Call<ResponseBody> call=weiXinApi.postToken(Constants.token,"jobApplyStep",route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        mView.sucussJobApply();

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
    public void postAddWork(String roleId) {
        PostAdd pd=new PostAdd();
        pd.setCreateUserId(mView.getUserid());
        pd.setGrainStoreRoomId(mView.getRoomId());
        pd.setAuditorUserId(roleId);
        pd.setStatus("1");
        pd.setStep("1");
        WorkTypeData workTypeData= (WorkTypeData) EncyApplication.getInstance().workMap.
                get(Integer.valueOf(mView.getType()));
        pd.setType(mView.getType());
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
                        mView.setEnable();
                       String jobId= jsonObject.getString("res");
                       mView.postAddSucuss(jobId);
//                            jobUp(roleId,typeStr);
                    }else {
                        if(mView!=null) {
                            mView.setEnable();
                            mView.showError(jsonObject.getString("rtnMsg"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(mView!=null) {
                    mView.setEnable();
                }
            }
        });
    }

    @Override
    public void jobUpdate(String jobId, final String status,String constent) {
        PostStatusBean postStatusBean=new PostStatusBean();
        postStatusBean.setAuditorUserId(mView.getUserid());
        postStatusBean.setId(jobId);
        postStatusBean.setStatus(status);
        postStatusBean.setType(mView.getType());
        if(!TextUtils.isEmpty(constent))
            postStatusBean.setRefuseCause(constent);
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

                        if("4".equals(status)){
                            mView.showMsg("驳回成功");
                        }else if("2".equals(status)){
                            mView.showMsg("通过审核");
                        }
                        mView.jobUpRes();
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

    private void goLogin(){
       mView.clearData();
    }


}