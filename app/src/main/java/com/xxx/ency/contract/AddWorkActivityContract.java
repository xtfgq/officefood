package com.xxx.ency.contract;

import com.xxx.ency.base.BasePresenter;
import com.xxx.ency.base.BaseView;
import com.xxx.ency.model.bean.SpaceBean;

import java.util.List;

public interface AddWorkActivityContract {
    interface View extends BaseView {

        /**
         * 获取仓库数据
         *
         * @param
         */
        void showSpaceBean(List<SpaceBean> results);
        void sucuss(String jobId);
        void sucuss();
        String getUserid();
        String getRoomId();
        int getType();
        void clearData();


    }

    interface Presenter extends BasePresenter<View> {

        /**
         * 获取仓库数据
         */
        void getData();

        /**
         * 新增作业
         */
        void postAdd(String roleId,String type);
        /**
         * 新增送检
         */
        void  postCheck();




    }
}
