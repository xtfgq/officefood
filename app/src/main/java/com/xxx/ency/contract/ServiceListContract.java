package com.xxx.ency.contract;

import com.xxx.ency.base.BasePresenter;
import com.xxx.ency.base.BaseView;
import com.xxx.ency.model.bean.JobBean;

import java.util.List;

/**
 * Created by xiarh on 2017/11/27.
 */

public interface ServiceListContract {

    interface View extends BaseView {

        /**
         * 成功获取维修数据
         *
         * @param
         */
        void showData(List<JobBean> jobBean);

        /**
         * 获取数据失败
         */
        void failGetData();
        String getUserid();
        String getTitle();
        void clearData();

        /**
         * 刷新Adapter
         */
        void refreshAdapter(boolean isRefresh);
    }

    interface Presenter extends BasePresenter<View> {

        /**
         * 获取作业数据
         *
         * @param size
         * @param page
         */
        void getData(int size, int page);


    }
}
