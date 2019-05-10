package com.xxx.ency.contract;

import com.xxx.ency.base.BasePresenter;
import com.xxx.ency.base.BaseView;
import com.xxx.ency.model.bean.LoginBean;

import java.io.File;

public interface PerfectContract {
    interface View extends BaseView {
        String getUserid();
        String getBase64();
        String getRealName();
        void showLoginBean(LoginBean lb);
        void clearData();
        void sucuss();

    }

    interface Presenter extends BasePresenter<PerfectContract.View> {
        void upLoad();
        void getUser();


    }
}
