package com.xxx.ency.contract;

import com.xxx.ency.base.BasePresenter;
import com.xxx.ency.base.BaseView;
import com.xxx.ency.model.bean.LoginBean;


public interface SendActivityContract {

    interface View extends BaseView {


        String getUserId();
        String getContent();
        String getSendUserId();
        String getMessageTitle();
        void Succuss();
        void clearData();



    }

    interface Presenter extends BasePresenter<View> {

       void postMessage();



    }
}
