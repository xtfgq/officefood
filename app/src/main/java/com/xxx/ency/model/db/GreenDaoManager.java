package com.xxx.ency.model.db;

import android.content.Context;

import com.xxx.ency.model.bean.CheckWarehouse;
import com.xxx.ency.model.bean.CheckWork;
import com.xxx.ency.model.bean.FixedCheckBean;
import com.xxx.ency.model.bean.LikeBean;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by xiarh on 2017/11/10.
 */

public class GreenDaoManager {

    private DaoMaster mDaoMaster;

    private DaoSession mDaoSession;

    @Inject
    public GreenDaoManager(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "like", null);//此处为自己需要处理的表
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

    public LikeBeanDao getLikeBeanDao() {
        return getSession().getLikeBeanDao();
    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<LikeBean> queryAll() {
        return getLikeBeanDao()
                .queryBuilder()
                .orderDesc(LikeBeanDao.Properties.Time)
                .build()
                .list();
    }

    /**
     * 新增
     *
     * @param likeBean
     */
    public void insert(LikeBean likeBean) {
        getLikeBeanDao().insert(likeBean);
    }

    /**
     * 根据Guid查询
     *
     * @param guid
     * @return
     */
    public boolean queryByGuid(String guid) {
        LikeBean bean = getLikeBeanDao()
                .queryBuilder()
                .where(LikeBeanDao.Properties.Guid.eq(guid))
                .build()
                .unique();
        if (null == bean) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据guid删除
     */
    public void deleteByGuid(String guid) {
        LikeBean bean = getLikeBeanDao()
                .queryBuilder()
                .where(LikeBeanDao.Properties.Guid.eq(guid))
                .build()
                .unique();
        if (null != bean) {
            getLikeBeanDao().delete(bean);
        }
    }

    /**
     * 删除
     *
     * @param likeBean
     */
    public void delete(LikeBean likeBean) {
        getLikeBeanDao().delete(likeBean);
    }
    public CheckWarehouseDao getCheckWarehouseDao() {
        return getSession().getCheckWarehouseDao();
    }

    /**
     * 作业dao
     * @return
     */
    public CheckWorkDao getCheckWorkDao() {
        return getSession().getCheckWorkDao();
    }
    /**
     * 作业dao
     * @return
     */
    public FixedCheckBeanDao getFixedCheckDao() {
        return getSession().getFixedCheckBeanDao();
    }
    /**
     * 新增仓库
     *
     * @param
     */
    public void insertCheckWarehouse(CheckWarehouse cw) {
        getCheckWarehouseDao().insert(cw);
    }
    public void insertCheckWork(CheckWork cw) {
        getCheckWorkDao().insert(cw);
    }
    public void insertCheckFixedWork(FixedCheckBean cw) {
        getFixedCheckDao().insert(cw);
    }

    /**
     * 查询缓存json
     * @param no
     * @param userid
     * @return
     */
    public boolean queryByNo(String no,String userid) {
        CheckWarehouse bean=getCheckWarehouseDao().
                queryBuilder().where(CheckWarehouseDao.Properties.WarehouseNo
           .eq(no),CheckWarehouseDao.Properties.Userid
                .eq(userid)).build().unique();

        if (null == bean) {
            return false;
        } else {
            return true;
        }
    }
    public void updateByNo(CheckWarehouse checkWarehouse){
            getCheckWarehouseDao().update(checkWarehouse);

    }
    public CheckWarehouse queryByNoBean(String no,String userid) {
        CheckWarehouse bean=getCheckWarehouseDao().
                queryBuilder().where(CheckWarehouseDao.Properties.WarehouseNo
                .eq(no),CheckWarehouseDao.Properties.Userid
                .eq(userid)).build().unique();
        return bean;

    }


    /**
     * 根据日期查询当天工作josn
     * @param no
     * @param userid
     * @param data
     * @return
     */
    public CheckWork queryByNoBeanCheckWork(String no, String userid,String data) {
        CheckWork bean=getCheckWorkDao().
                queryBuilder().where(CheckWorkDao.Properties.WarehouseNo
                .eq(no),CheckWorkDao.Properties.Userid
                .eq(userid),CheckWorkDao.Properties.Date.eq(data)).build().unique();
        return bean;

    }
    public FixedCheckBean queryByNoBeanFixedWork(String no, String userid,String data) {
        FixedCheckBean bean=getFixedCheckDao().
                queryBuilder().where(FixedCheckBeanDao.Properties.WarehouseNo
                .eq(no),FixedCheckBeanDao.Properties.Userid
                .eq(userid),FixedCheckBeanDao.Properties.Date.eq(data)).build().unique();
        return bean;

    }

    /**
     * 删除提交项目
     * @param no
     * @param userid
     */
    public void deleteByUseridAndNo(String no,String userid) {
        CheckWarehouse bean=getCheckWarehouseDao().
                queryBuilder().where(CheckWarehouseDao.Properties.WarehouseNo
                .eq(no),CheckWarehouseDao.Properties.Userid
                .eq(userid)).build().unique();
        if (null != bean) {
           getCheckWarehouseDao().delete(bean);
        }
    }
    public void deleteByUseridAndNoCheckWork(String no,String userid,String data) {
        CheckWork bean=getCheckWorkDao().
                queryBuilder().where(CheckWorkDao.Properties.WarehouseNo
                .eq(no),CheckWorkDao.Properties.Userid
                .eq(userid),CheckWorkDao.Properties.Date.eq(data)).build().unique();
        if (null != bean) {
           getCheckWorkDao().delete(bean);
        }
    }
    public void deleteByUseridAndNoFixedWork(String no,String userid,String data) {
        FixedCheckBean bean=getFixedCheckDao().
                queryBuilder().where(FixedCheckBeanDao.Properties.WarehouseNo
                .eq(no),FixedCheckBeanDao.Properties.Userid
                .eq(userid),FixedCheckBeanDao.Properties.Date.eq(data)).build().unique();
        if (null != bean) {
            getFixedCheckDao().delete(bean);
        }
    }
}