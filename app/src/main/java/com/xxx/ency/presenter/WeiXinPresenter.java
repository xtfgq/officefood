package com.xxx.ency.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.xxx.ency.base.BaseSubscriber;
import com.xxx.ency.base.RxBus;
import com.xxx.ency.base.RxPresenter;
import com.xxx.ency.config.Constants;
import com.xxx.ency.contract.WeiXinContract;
import com.xxx.ency.model.bean.CmsPostBean;
import com.xxx.ency.model.bean.CmsResult;
import com.xxx.ency.model.bean.Result;
import com.xxx.ency.model.bean.WeiXinBean;
import com.xxx.ency.model.http.WeatherApi;
import com.xxx.ency.model.http.WeiXinApi;
import com.xxx.ency.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xiarh on 2017/11/8.
 */

public class WeiXinPresenter extends RxPresenter<WeiXinContract.View> implements WeiXinContract.Presenter {

    private WeiXinApi weiXinApi;

    private Context context;

    @Inject
    public WeiXinPresenter(WeiXinApi weiXinApi,Context context) {
        this.weiXinApi = weiXinApi;
        this.context = context;
    }

    @Override
    public void getWeiXinData() {
        final CmsPostBean bean=new CmsPostBean();
        bean.setTypeId("1");
        Gson gson=new Gson();
        String route= gson.toJson(bean);
        Call<ResponseBody> call=weiXinApi.postCms("cmsListByTypeId",
                route);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("rtnCode");
                    if(code==Constants.NET_CODE_SUCCESS&&mView!=null){
                        JSONArray jsonArray=jsonObject.getJSONArray("res");
                        List<WeiXinBean.NewslistBean> list=new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                WeiXinBean.NewslistBean beanNews = new WeiXinBean.NewslistBean();
                                beanNews.setPicUrl(obj.getString("img"));
                                beanNews.setTitle(obj.getString("title"));
                                beanNews.setCtime(obj.getString("createTime"));
                                beanNews.setId(obj.getString("id"));
                                beanNews.setUrl(obj.getString("url"));
                                list.add(beanNews);
                            }
                           WeiXinBean weiXinBean=new WeiXinBean();
                            weiXinBean.setNewslist(list);
                            mView.showWeiXinData(weiXinBean);

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

    @Override
    public void getPTP() {
        addSubscribe(RxBus.getInstance().register(Integer.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseSubscriber<Integer>(context, mView) {
                    @Override
                    public void onNext(Integer integer) {
                        if (integer == 1001) {
                            mView.refreshAdapter(true);
                        }
                    }
                }));
    }
}
