package com.xxx.ency.contract;

import com.xxx.ency.base.BasePresenter;
import com.xxx.ency.base.BaseView;
import com.xxx.ency.model.bean.SpaceBean;

import java.util.List;

public interface AreaContract {
    interface View extends BaseView {



        /**
         *
         *
         * @param hotBean
         */
        void showSucuss();

        /**
         * 获取数据失败
         */
        void failGetHotData();
        String getUserid();
        String getAreaId();
        String getLatitude();
        String getLongitude();
        String getDes();
        void clearData();
        int crashType();
        int isHasCrash();

        void showSpaceBean(List<SpaceBean> list);

        void showArea(List<SpaceBean> list);

    }

    interface Presenter extends BasePresenter<View> {

      void postArea();
      void getAreaList();
      void getData();

    }

}
