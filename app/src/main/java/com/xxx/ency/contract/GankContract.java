package com.xxx.ency.contract;

import com.xxx.ency.base.BasePresenter;
import com.xxx.ency.base.BaseView;
import com.xxx.ency.model.bean.GankBean;
import com.xxx.ency.model.bean.JobBean;

import java.util.List;

/**
 * Created by xiarh on 2017/11/27.
 */

public interface GankContract {

    interface View extends BaseView {

        /**
         * 成功获取数据
         *
         * @param gankBean
         */
        void showGankData(List<JobBean> jobBean);

        /**
         * 获取数据失败
         */
        void failGetData();

        /**
         * 刷新Adapter
         */
        void refreshAdapter(boolean isRefresh);
        String getUserid();
        void clearData();
        String getTitle();
        void sucuss();
        String strRoomId();


    }

    interface Presenter extends BasePresenter<View> {

        /**
         * 获取作业数据
         *
         * @param type
         * @param size
         * @param page
         */
        void getGankData(String type, int size, String typeWork,int page);
        void postCheck(String id,String res);
        void postResult(String id);


    }
}
