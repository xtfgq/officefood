package com.xxx.ency.view.servicelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amap.api.location.AMapLocationClientOption;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gavin.com.library.StickyDecoration;
import com.gavin.com.library.listener.GroupListener;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.xxx.ency.R;
import com.xxx.ency.base.BaseMVPFragment;

import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.ServiceListContract;

import com.xxx.ency.di.component.DaggerServiceListComponent;
import com.xxx.ency.di.module.ServiceListModule;
import com.xxx.ency.model.bean.JobBean;

import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.presenter.ServicePresenter;
import com.xxx.ency.util.DateUtil;
import com.xxx.ency.util.DensityUtil;
import com.xxx.ency.view.login.LoginActivity;
import com.xxx.ency.view.move.MoveActivity;
import com.xxx.ency.view.work.AddWorkActivity;
import com.xxx.ency.view.work.AreaActivity;
import com.xxx.ency.view.work.adapter.JobAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by xiarh on 2017/11/27.
 */

public class ServiceListFragment extends BaseMVPFragment<ServicePresenter> implements ServiceListContract.View,
        SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @Inject
    SharePrefManager sharePrefManager;

    private JobAdapter jobAdapter;

    private List<JobBean> resultsBeans = new ArrayList<>();

    private String title;

    private int page = 1;

    private static final int PAGE_SIZE = 10;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh;
    }

    @Override
    protected void initInject() {

        DaggerServiceListComponent.builder()
                .appComponent(EncyApplication.getAppComponent())
                .serviceListModule(new ServiceListModule())
                .build()
                .inject(this);
    }

    public static ServiceListFragment newInstance(String title) {
        ServiceListFragment gankFragment = new ServiceListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        gankFragment.setArguments(bundle);
        return gankFragment;
    }
    StickyDecoration decoration;
    @Override
    protected void initialize() {
        Bundle args = getArguments();
        if (args != null) {
            title=args.getString("title");
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        List<JobBean> mList=new ArrayList<>();
        jobAdapter = new JobAdapter(getActivity(),mList,title);
       decoration= StickyDecoration.Builder
                .init(new GroupListener() {
                    @Override
                    public String getGroupName(int position) {
                        if(resultsBeans.size()==0) return "";

                        //组名回调
                        if(position==0){
                            return DateUtil.StringPattern(resultsBeans.get(position).getData(),
                                    "yyyy-MM-dd",
                                    "yyyyMM");
                        }else {
                            return DateUtil.StringPattern(resultsBeans.get(position-1).getData(),
                                    "yyyy-MM-dd",
                                "yyyyMM");
                        }


                    }
                })
                .setGroupBackground(getActivity().getResources().getColor(R.color.gray1))        //背景色
                .setGroupHeight(DensityUtil.dip2px(getActivity(), 35))     //高度
                .setDivideColor(getActivity().getResources().getColor(R.color.line))            //分割线颜色
                .setDivideHeight(DensityUtil.dip2px(getActivity(), 2))     //分割线高度 (默认没有分割线)
                .setGroupTextColor(getActivity().getResources().getColor(R.color.gray))                                    //字体颜色 （默认）
                .setGroupTextSize(DensityUtil.sp2px(getActivity(), 15))    //字体大小
                .setTextSideMargin(DensityUtil.dip2px(getActivity(), 10))  // 边距   靠左时为左边距  靠右时为右边距
                .build();
        RecyclerView.LayoutManager manager;
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(jobAdapter);
        mPresenter.getData(PAGE_SIZE, page);
        jobAdapter.setOnLoadMoreListener(this, recyclerView);
        jobAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String tag=resultsBeans.get(position).getType();
                String areaId=resultsBeans.get(position).getGrainStoreRoomId();

                int code=Integer.valueOf(resultsBeans.get(position).getIsAbnormal());
                Intent i = new Intent(getActivity(), MoveActivity.class);
                i.putExtra("id", areaId);
                i.putExtra("tag", tag);
                i.putExtra("fromTo", "0");
                i.putExtra("area",resultsBeans.get(position).getAreaName());
                if("2".equals(tag)){
                    if(code==0){
                        i.putExtra("des", "检查情况："+"粮情无异常");
                    }else {
                        i.putExtra("des", "检查情况："+resultsBeans.get(position).getDes());
                    }
                }
                startActivity(i);

            }
        });

    }
    @Override
    public void clearData() {
        sharePrefManager.clearData();
        final MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity()).builder()
                .setTitle("提示")
                .setMsg("该账号在其他设备上登录，请重新登录");
        myAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                intent.putExtra("tag","exit");
                startActivity(intent);


            }
        });
        myAlertDialog.show();
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.getData(PAGE_SIZE, page);
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        jobAdapter.setEnableLoadMore(false);
    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoadMoreRequested() {
        page++;
        mPresenter.getData( PAGE_SIZE, page);
        // 防止上拉加载的时候可以下拉刷新
        swipeRefreshLayout.setEnabled(false);
    }





    @Override
    public void showData(List<JobBean> jobBean) {
        if (null != swipeRefreshLayout && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
            // 下拉刷新后可以上拉加载
            jobAdapter.setEnableLoadMore(true);
        }
        if (null != jobAdapter && jobAdapter.isLoading()) {
            // 上拉加载后可以下拉刷新
            swipeRefreshLayout.setEnabled(true);
        }
        if (page == 1) {
            resultsBeans.clear();
            resultsBeans.addAll(jobBean);
            if(resultsBeans.size()==0){
                jobAdapter.setEmptyView(R.layout.view_empty, recyclerView);
                recyclerView.removeItemDecoration(decoration);
            }else {
                jobAdapter.setNewData(resultsBeans);
            }
        } else {
            resultsBeans.addAll(jobBean);
            jobAdapter.setNewData(resultsBeans);
        }

        jobAdapter.notifyDataSetChanged();
        if (jobBean.size() == PAGE_SIZE) {
            jobAdapter.loadMoreComplete();
        } else if (jobBean.size() < PAGE_SIZE) {
            jobAdapter.loadMoreEnd();
        }
    }

    /**
     * 加载失败
     */
    @Override
    public void failGetData() {
        jobAdapter.loadMoreFail();
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public String getUserid() {
        return sharePrefManager.getUserId();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void refreshAdapter(boolean isRefresh) {
        if (isRefresh) {

            jobAdapter.notifyDataSetChanged();
        }
    }


}
