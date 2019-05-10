package com.xxx.ency.presenter;

import android.content.Context;
import android.view.View;

import com.google.gson.Gson;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.xxx.ency.base.BaseSubscriber;
import com.xxx.ency.base.RxPresenter;
import com.xxx.ency.config.Constants;
import com.xxx.ency.contract.OneContract;
import com.xxx.ency.contract.SecurityCheckContract;
import com.xxx.ency.model.bean.OneBean;
import com.xxx.ency.model.bean.SpaceBean;
import com.xxx.ency.model.bean.User;
import com.xxx.ency.model.http.WeatherApi;
import com.xxx.ency.model.http.WeiXinApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xiarh on 2017/11/1.
 */

public class SecurityCheckPresenter extends RxPresenter<SecurityCheckContract.View>
        implements SecurityCheckContract.Presenter {

    private Context context;
    private WeiXinApi weiXinApi;

    @Inject
    public SecurityCheckPresenter(WeiXinApi weiXinApi,Context context) {
        this.weiXinApi=weiXinApi;
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


    private void goLogin(){
        mView.clearData();
    }

//    @Override
//    public void getData() {
//        addSubscribe(Flowable.create
//                (new FlowableOnSubscribe<OneBean>() {
//                    @Override
//                    public void subscribe(FlowableEmitter<OneBean> emitter) throws Exception {
//                        OneBean oneBean = new OneBean();
//                        StringBuffer buffer = new StringBuffer();
//                        Document doc = Jsoup.connect(url).get();
//                        // 解析标题
//                        Element article_show = doc.getElementById("article_show");
//                        oneBean.setTitle(article_show.select("h1").text());
//                        Elements elements = article_show.select("p");
//                        for (int i = 0; i < elements.size(); i++) {
//                            // 解析作者
//                            if (i == 0) {
//                                oneBean.setAuthor(elements.get(i).text());
//                            }
//                            // 解析内容
//                            else {
//                                buffer.append("\u3000\u3000");
//                                buffer.append(elements.get(i).text() + "\n");
//                                buffer.append("\n");
//                            }
//                        }
//                        oneBean.setContent(buffer.toString());
//                        emitter.onNext(oneBean);
//                        emitter.onComplete();
//                    }
//                }, BackpressureStrategy.ERROR)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseSubscriber<OneBean>(context, mView) {
//                    @Override
//                    public void onNext(OneBean oneBean) {
//                        mView.showOneBean(oneBean);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        super.onError(t);
//                        mView.failGetData();
//                    }
//                }));
//    }
}