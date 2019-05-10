package com.xxx.ency.contract;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.xxx.ency.base.BasePresenter;
import com.xxx.ency.base.BaseView;
import com.xxx.ency.model.bean.SpaceBean;

import java.util.List;


public interface UpAdressContract {
    interface View extends BaseView {
    void showAdress(String address);
    void postLationComplete(String msg);
    String getUserid();
        void showSpaceBean(List<SpaceBean> results);
        void clearData();
        void showArea(List<SpaceBean> list);
        String getRoomId();
        String getAreaId();
        void showWeituo();
        String getToUserid();
        String type();

}

    interface Presenter extends BasePresenter<UpAdressContract.View> {

      void  getAdress(String address);
      void postLation(LatLonPoint latLng,String title);
        /**
         * 获取区域管理
         */
        void getData();
       void getAreaData();
       void postAddWeituo(String startDate,String endDate,String id);

    }
}
