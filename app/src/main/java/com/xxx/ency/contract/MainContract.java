package com.xxx.ency.contract;

import android.location.Location;

import com.xxx.ency.base.BasePresenter;
import com.xxx.ency.base.BaseView;
import com.xxx.ency.model.bean.LoginBean;
import com.xxx.ency.model.bean.UpdateBean;
import com.xxx.ency.model.bean.WeatherBean;

/**
 * Created by xiarh on 2017/9/25.
 */

public interface MainContract {

    interface View extends BaseView {

        /**
         * 更新提示框
         */
        void showUpdateDialog(UpdateBean updateBean);

        /**
         * 天气数据
         *
         * @param weatherBean
         */
        void showWeather(WeatherBean.HeWeather6Bean.NowBean weatherBean);

        /**
         * 获取天气数据
         */
        WeatherBean.HeWeather6Bean.NowBean getWeathter();

        /**
         * 未获取权限，弹出提示框
         */
        void showPermissionDialog();

        /**
         * 获取权限成功
         */
        void getPermissionSuccess();

        /**
         * 是否改变
         */
        void changeDayOrNight(boolean changed);
        void getPermissionScanSuccess();

        void clearData();
        void showMsgNum(int num);
        String getUserid();
        String roomId();
        String getlatitude();
        String getlongitude();
        void showLocaiton();
        void showLoginBean(LoginBean lb);
    }

    interface Presenter extends BasePresenter<View> {

        /**
         * 检查更新
         */
        void checkUpdate();

        /**
         * 检查权限
         */
        void checkPermissions();

        /**
         * 拉取天气权限
         */
        void getWeather(String location);

        /**
         * 设置白天/夜间模式
         */
        void setDayOrNight();
        void getMsgNum();

        void checkPermissionsScan();
        void  checkLocaiton();
        void getUser();
        void getRoomData();
        void getAreaData();
        void setFalse();
        void getPersonUser();
        void getWorkType();
    }
}
