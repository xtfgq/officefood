package com.xxx.ency.contract;

import com.xxx.ency.base.BasePresenter;
import com.xxx.ency.base.BaseView;
public interface ScanCodeContract {

    interface View extends BaseView {

       String getRoomId();
       String getlatitude();
       String getlongitude();
       String getType();
       void showLocaiton();
       void showToast(CharSequence msg);

    }

    interface Presenter extends BasePresenter<View> {

       void checkCode();



    }
}
