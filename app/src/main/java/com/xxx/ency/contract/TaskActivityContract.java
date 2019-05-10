package com.xxx.ency.contract;

import com.xxx.ency.base.BasePresenter;
import com.xxx.ency.base.BaseView;


public interface TaskActivityContract {

    interface View extends BaseView {


        String getUserId();
        String getContent();
        String getSendId();
        String getStartDate();
        String getEndDate();

        String getMessageTitle();
        void Succuss();
        void clearData();



    }

    interface Presenter extends BasePresenter<View> {

       void postTask();

    }
}
