package com.xxx.ency.contract;

import com.xxx.ency.base.BasePresenter;
import com.xxx.ency.base.BaseView;
import com.xxx.ency.model.bean.CheckListBean;
import com.xxx.ency.model.bean.SpaceBean;

import java.util.List;
import java.util.Map;

public interface SecurityCheckListContract {
    interface View extends BaseView {

        /**
         * 获取检测类型数据
         *
         * @param
         */
        void getCheckList(final List<CheckListBean> results);

        /**
         * 获取数据失败
         */
        void failGetData();

        String getType();
        String getUserid();
        String getRoomId();
        String getCreateData();
        String getStep();
        String getJobID();
        String getRoleTye();
        String fromTo();
        void getFunNumAndWT(String funNum,String wt,String beforeTemp);
        void showPostSucuss();
        void getCheckReport(List<CheckListBean> list);
        void jobUpRes();
        void goLoginFaile();
        void clearData();
        void sucussJobApply();
        void postAddSucuss(String jobId);
        void setEnable();


    }
    interface Presenter extends BasePresenter<SecurityCheckListContract.View> {

        /**
         * 获取检测数据
         */
        void getCheckData();
        void postCheck(Map<String,List<CheckListBean>> saveMap);
        void getReport();
        void jobUpdate(String jobId,String status,String content);
        void getReportFan();
        void jobApplyStep(String roleId,String multipleTypeStr,String jobid);
        void postAddWork(String roleId);
    }
}
