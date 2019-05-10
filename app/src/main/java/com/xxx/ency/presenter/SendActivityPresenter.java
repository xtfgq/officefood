package com.xxx.ency.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.xxx.ency.base.RxPresenter;
import com.xxx.ency.config.Constants;
import com.xxx.ency.contract.ScanCodeContract;
import com.xxx.ency.contract.SendActivityContract;
import com.xxx.ency.model.http.BingApi;
import com.xxx.ency.view.send.SendMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendActivityPresenter extends RxPresenter<SendActivityContract.View>
        implements SendActivityContract.Presenter{
    private Context context;
    private BingApi bingApi;
    @Inject
    public SendActivityPresenter(BingApi bingApi, Context context) {
        this.bingApi = bingApi;
        this.context = context;
    }


    @Override
    public void postMessage() {
           SendMessage cmsBean=new SendMessage();
           cmsBean.setSendUserId(mView.getUserId());
           cmsBean.setReceiveUserId(mView.getSendUserId());
           cmsBean.setContent(mView.getContent());
           cmsBean.setTitle(mView.getMessageTitle());
           Gson gson=new Gson();
            String route= gson.toJson(cmsBean);
            Call<ResponseBody> call=bingApi.postToken(Constants.token,"msgAdd",
                    route);
            call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                       mView.Succuss();

                    }else if(code==Constants.NET_CODE_LOGIN&&mView!=null){
                        mView.clearData();
                    }
                    else {
                        if(mView!=null)
                        mView.showError(jsonObject.getString("rtnMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(mView!=null)
                    mView.showError(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    if(mView!=null)
                    mView.showError(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mView.showError(t.getMessage());
            }
        });
    }
}
