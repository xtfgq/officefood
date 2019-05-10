package com.xxx.ency.contract;

import com.xxx.ency.base.BasePresenter;
import com.xxx.ency.base.BaseView;

import com.xxx.ency.model.bean.SpaceBean;

import java.util.List;

public interface SecurityCheckContract {
    interface View extends BaseView {

        /**
         * 获取仓库数据
         *
         * @param
         */
        void showSpaceBean(List<SpaceBean> results);

        /**
         * 获取数据失败
         */
        void failGetData();
        String getUserid();
        void postSuccuss();
        void clearData();


    }
    interface Presenter extends BasePresenter<SecurityCheckContract.View> {

        /**
         * 获取检测数据
         */
        void getData();

    }
}
