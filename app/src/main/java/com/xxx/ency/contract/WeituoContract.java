package com.xxx.ency.contract;

import com.xxx.ency.base.BasePresenter;
import com.xxx.ency.base.BaseView;
import com.xxx.ency.model.bean.LoginBean;

import java.util.List;

public interface WeituoContract {
    interface View extends BaseView {



        /**
         * 获取数据失败
         */
        void failGetData();
        void showUser(List<LoginBean> list);
        String getUserId();
        String getRoleId();
        void showScuss();
        void clearData();



    }

    interface Presenter extends BasePresenter<View> {

        void postAdd();


        void getUser();
        void postMultipleType(String roleId, String multipleTypeStr,String id);
    }
}
