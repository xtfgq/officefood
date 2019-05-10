package com.xxx.ency.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.xxx.ency.base.RxPresenter;
import com.xxx.ency.config.Constants;
import com.xxx.ency.contract.LoginContract;
import com.xxx.ency.contract.MessageContract;
import com.xxx.ency.model.bean.LoginBean;
import com.xxx.ency.model.bean.PostLogin;
import com.xxx.ency.model.bean.PostMessage;
import com.xxx.ency.model.bean.PostMessageBean;
import com.xxx.ency.model.http.BingApi;
import com.xxx.ency.view.task.FinishTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagePresenter extends RxPresenter<MessageContract.View>
        implements MessageContract.Presenter{
    private Context context;
    private BingApi bingApi;
    @Inject
    public MessagePresenter(BingApi bingApi, Context context) {
        this.bingApi = bingApi;
        this.context = context;
    }


    @Override
    public void readMessage() {
        PostMessageBean postMessageBean=new PostMessageBean();
        postMessageBean.setId(mView.id());
        postMessageBean.setStatus("1");
        Gson gson=new Gson();
        String route= gson.toJson(postMessageBean);
        Call<ResponseBody> call=bingApi.postToken(Constants.token,"msgChange",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        mView.showSuccuss();

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
    public void finishTask() {
        FinishTask finishTask=new FinishTask();
        finishTask.setId(mView.id());
        finishTask.setStatus("2");
        finishTask.setDemo(mView.getContent());
        Gson gson=new Gson();
        String route= gson.toJson(finishTask);
        Call<ResponseBody> call=bingApi.postToken(Constants.token,"taskUpdate",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){

                        mView.goFinish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(mView!=null)
                    mView.showMsg(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    if(mView!=null)
                    mView.showMsg(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(mView!=null)
                mView.showMsg(t.getMessage());
            }
        });
    }
}
