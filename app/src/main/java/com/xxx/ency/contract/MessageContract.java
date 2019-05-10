package com.xxx.ency.contract;

import com.xxx.ency.base.BasePresenter;
import com.xxx.ency.base.BaseView;
import com.xxx.ency.model.bean.LoginBean;


public interface MessageContract {

    interface View extends BaseView {

        /**
         *
         *
         *
         */
        void showSuccuss();
        String id();
        void  clearData();
        String getContent();
        void goFinish();




    }

    interface Presenter extends BasePresenter<View> {

       void readMessage();
       void finishTask();



    }
}
