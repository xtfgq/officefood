package com.xxx.ency.contract;

import com.xxx.ency.base.BasePresenter;
import com.xxx.ency.base.BaseView;
import com.xxx.ency.model.bean.LoginBean;


public interface LoginContract {

    interface View extends BaseView {

        /**
         *
         *
         * @param bingBean
         */
        void showLoginBean(LoginBean bingBean);
        String getUserName();
        String getPwd();
        void showMsg(String msg);



    }

    interface Presenter extends BasePresenter<View> {

       void login(String deviceId);



    }
}
