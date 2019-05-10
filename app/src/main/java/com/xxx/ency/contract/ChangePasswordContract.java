package com.xxx.ency.contract;

import com.xxx.ency.base.BasePresenter;
import com.xxx.ency.base.BaseView;
import com.xxx.ency.model.bean.LoginBean;
import com.xxx.ency.model.bean.Sms;

public interface ChangePasswordContract {
    interface View extends BaseView {


        void sendSmsBean();
        void changSuccuss();
        String getPhone();
        String getCode();
        String getPwd();


    }

    interface Presenter extends BasePresenter<ChangePasswordContract.View> {

        void changePwd();
        void sendSms();

    }
}
