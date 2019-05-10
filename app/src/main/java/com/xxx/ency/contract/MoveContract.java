package com.xxx.ency.contract;

import com.xxx.ency.base.BasePresenter;
import com.xxx.ency.base.BaseView;
import com.xxx.ency.model.bean.LoginBean;


public interface MoveContract {

    interface View extends BaseView {
        String getRoomId();
        void showSuccuss(String str);
        String getContent();
        int getNormal();
        String getUserId();
        String getType();
        void addSucuss();
        void clearData();
    }

    interface Presenter extends BasePresenter<View> {

       void move();
       void postSave();

    }
}
