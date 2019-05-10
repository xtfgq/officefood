package com.xxx.ency.view.work;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gavin.com.library.StickyDecoration;
import com.gavin.com.library.listener.GroupListener;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.xxx.ency.R;
import com.xxx.ency.base.BaseMVPFragment;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.GankContract;
import com.xxx.ency.di.component.DaggerGankFragmentComponent;
import com.xxx.ency.di.module.GankFragmentModule;
import com.xxx.ency.model.bean.JobBean;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.presenter.GankPresenter;
import com.xxx.ency.util.AppActivityTaskManager;
import com.xxx.ency.util.DateUtil;
import com.xxx.ency.util.DensityUtil;
import com.xxx.ency.view.SecurityCheck.SecurityListActivity;
import com.xxx.ency.view.login.LoginActivity;
import com.xxx.ency.view.main.MainActivity;
import com.xxx.ency.view.message.MessageActivity;
import com.xxx.ency.view.work.adapter.JobAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by xiarh on 2017/11/27.
 */

public class GankFragment extends BaseMVPFragment<GankPresenter> implements GankContract.View,
        SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @Inject
    SharePrefManager sharePrefManager;

    private JobAdapter jobAdapter;
    String title;
    String typeWork;

    private List<JobBean> resultsBeans = new ArrayList<>();

    private String type;

    private int page = 1;

    private static final int PAGE_SIZE = 20;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh;
    }

    @Override
    protected void initInject() {
        DaggerGankFragmentComponent
                .builder()
                .appComponent(EncyApplication.getAppComponent())
                .gankFragmentModule(new GankFragmentModule())
                .build()
                .inject(this);
    }

    public static GankFragment newInstance(String type,String title,String typeWork,String id) {


        GankFragment gankFragment = new GankFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("title", title);
        bundle.putString("typeWork", typeWork);
        bundle.putString("id", id);
        gankFragment.setArguments(bundle);
        return gankFragment;
    }
    StickyDecoration decoration;
    boolean isView=false;
    String id="";
    WorkTypeData workTypeData;
    int code=0;

    @Override
    protected void initialize() {
        Bundle args = getArguments();
        if (args != null) {
            type = args.getString("type");
            title=args.getString("title");
            typeWork=args.getString("typeWork");
            id=args.getString("id");

        }
        if(!TextUtils.isEmpty(typeWork)){
            int typeNue=Integer.valueOf(typeWork);
            if(typeNue>2) {

                workTypeData = (WorkTypeData) EncyApplication.getInstance().
                        workMap.get(Integer.valueOf(typeWork));
                code = Integer.valueOf(workTypeData.getCode());
            }
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        List<JobBean> jobBeans =new ArrayList<>();
        if(title.contains("任务")){
            jobAdapter = new JobAdapter(getActivity(),R.layout.item_task, jobBeans, title);
        }else {
            jobAdapter = new JobAdapter(getActivity(), jobBeans, title);
        }
        decoration= StickyDecoration.Builder
                .init(new GroupListener() {
                    @Override
                    public String getGroupName(int position) {
                        if(resultsBeans.size()==0) return "";
                        //组名回调

                        try {
                            if(position%2==0) {
                                return DateUtil.StringPattern(resultsBeans.get(position).getData(),"yyyy-MM-dd",
                                        "yyyyMM");

                            }else {


                                return DateUtil.StringPattern(resultsBeans.get(position - 1).getData(),
                                        "yyyy-MM-dd",
                                        "yyyyMM");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
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
        mPresenter.getGankData(type, PAGE_SIZE,typeWork, page);
        jobAdapter.setOnLoadMoreListener(this, recyclerView);
        jobAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if(title.contains("送检")&&"0".equals(type)&&sharePrefManager.getLocalMode()==2){
                    showDialog(position);
                }else if(title.contains("维修")&&"0".equals(type)&&sharePrefManager.getLocalMode()==3){
                    setState(position);
                }else if(title.contains("日志")){
                    Intent intent1=new Intent(getActivity(),SecurityListActivity.class);
                    if ("1".equals(type)) {
                        intent1.putExtra("title","定项检查");
                        intent1.putExtra("type","1");

                    } else if ("2".equals(type)) {
                        intent1.putExtra("title","入仓检查");
                        intent1.putExtra("type","2");

                    }
                    intent1.putExtra("id",resultsBeans.get(position).getId());
                    intent1.putExtra("isBrow",resultsBeans.get(position).getData());
                    startActivity(intent1);
                }else if(title.contains("作业")){
                    Intent intent1=new Intent(getActivity(),SecurityListActivity.class);
                    intent1.putExtra("title",workTypeData.getName());
                    intent1.putExtra("type",typeWork);
                    intent1.putExtra("jobId",resultsBeans.get(position).getId());
                    intent1.putExtra("id",resultsBeans.get(position).getGrainStoreRoomId());

                    //保管员已批准作业，step=2
                    if(sharePrefManager.getLocalMode()==1&&"2".equals(type)){
                        intent1.putExtra("isBrow","");
                        intent1.putExtra("step","2");
                        intent1.putExtra("fromTo","2");
                        if(code==6||code==7) {
                            intent1.putExtra("title", workTypeData.getName() +
                                    getStats(code, Integer.valueOf(resultsBeans.get(position).getCurrentStep())) +
                                    "登记");
                            intent1.putExtra("roleId",resultsBeans.get(position).getAuditorUserId());
                            intent1.putExtra("currentStep",resultsBeans.get(position).getCurrentStep());
                        }
                        else intent1.putExtra("title",workTypeData.getName()+"登记");


                    }else if((sharePrefManager.getLocalMode()==5&&"1".equals(type))||
                            (sharePrefManager.getLocalMode()==1&&"1".equals(type))) {
                        //科长待批准作业，step=1
                        intent1.putExtra("isBrow", "brow");
                        intent1.putExtra("step","1");
                        intent1.putExtra("fromTo","1");
                        if(code==6||code==7) {
                            if(sharePrefManager.getLocalMode()==1) {

                                intent1.putExtra("currentStep",
                                        resultsBeans.get(position).getCurrentStep());
                            }
                            intent1.putExtra("title", workTypeData.getName() +
                                    getStats(code, Integer.valueOf(resultsBeans.get(position).getCurrentStep())) + "" +
                                    "申请");

                        }
                        else intent1.putExtra("title",workTypeData.getName()+"申请");


                    }else if(sharePrefManager.getLocalMode()==5&&"2".equals(type)){
                        intent1.putExtra("isBrow", "brow");
                        intent1.putExtra("step","1");
                        intent1.putExtra("fromTo","2");
                        if(code==6||code==7) {
                            if(sharePrefManager.getLocalMode()==1) {

                                intent1.putExtra("currentStep",
                                        resultsBeans.get(position).getCurrentStep());
                            }
                            intent1.putExtra("title", workTypeData.getName() +
                                    getStats(code, Integer.valueOf(resultsBeans.get(position).getCurrentStep())) + "" +
                                    "申请");

                        }
                        else intent1.putExtra("title",workTypeData.getName()+"申请");
                    }else if("4".equals(type)){
                        intent1.putExtra("isBrow", "brow");
                        intent1.putExtra("step","1");
                        if(code==6||code==7) {
                            if(sharePrefManager.getLocalMode()==1) {

                                intent1.putExtra("currentStep",
                                        resultsBeans.get(position).getCurrentStep());
                            }
                            intent1.putExtra("title", workTypeData.getName() +
                                    getStats(code, Integer.valueOf(resultsBeans.get(position).getCurrentStep())) + "" +
                                    "申请");

                        }
                        else intent1.putExtra("title",workTypeData.getName()+"申请");

                    }else if("3".equals(type)){
                        intent1.putExtra("isBrow", "brow");
                        intent1.putExtra("fromTo",type);
                        if(code==6||code==7) {
                            intent1.putExtra("title", workTypeData.getName() +
                                    getStats(code, Integer.valueOf(resultsBeans.get(position).getCurrentStep())) +
                                    "登记");
                            intent1.putExtra("roleId",resultsBeans.get(position).getAuditorUserId());
                            intent1.putExtra("currentStep",resultsBeans.get(position).getCurrentStep());
                        } else intent1.putExtra("title",workTypeData.getName()+"登记");

                    }
                    else {
                        intent1.putExtra("isBrow", "brow");
                        intent1.putExtra("step","2");
                    }
                    startActivity(intent1);
                }else if(title.contains("消息")){
                    Intent intent1=new Intent(getActivity(),MessageActivity.class);
                    intent1.putExtra("title",resultsBeans.get(position).getResult());
                    intent1.putExtra("type",type);
                    intent1.putExtra("id",resultsBeans.get(position).getId());
                    startActivity(intent1);
                }else if(title.contains("任务")){
                    Intent intent1=new Intent(getActivity(),MessageActivity.class);
                    intent1.putExtra("title",resultsBeans.get(position).getContent());
                    intent1.putExtra("status",resultsBeans.get(position).getStatus());
                    intent1.putExtra("fromTo","task");
                    intent1.putExtra("id",resultsBeans.get(position).getId());
                    intent1.putExtra("type",resultsBeans.get(position).getIsAbnormal());
                    startActivity(intent1);
                }
            }
        });
        isView=true;

    }
    private String getStats(int code,int currentStep){
        String res="";
        if(code==6||code==7){
            if(currentStep==3) res="补药";
            if(currentStep==4) res="散气";
        }

        return res;
    }
    private void setState(final int pos){
        MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity()).builder()
                .setTitle("提示")
                .setMsg("维修是否完毕")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.postResult(resultsBeans.get(pos).getId());
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

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
        mPresenter.getGankData(type, PAGE_SIZE,typeWork, page);
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        jobAdapter.setEnableLoadMore(false);

    }


    /**
     * 上拉加载
     */
    @Override
    public void onLoadMoreRequested() {
        page++;
        mPresenter.getGankData(type, PAGE_SIZE, typeWork,page);
        // 防止上拉加载的时候可以下拉刷新
        swipeRefreshLayout.setEnabled(false);
    }



    @Override
    public void showGankData(List<JobBean> jobBean) {
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
    public void refreshAdapter(boolean isRefresh) {
        if (isRefresh) {
            jobAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public String getUserid() {
        return sharePrefManager.getUserId();
    }
    @Override
    public void clearData(){

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
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        myAlertDialog.show();

    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void sucuss() {
        onRefresh();
    }

    @Override
    public String strRoomId() {
        return id;
    }




    private void showDialog(final int pos) {
        final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(getActivity()).builder()
                .setTitle("请输入检测结果").setEditType(8194)
                .setEditText("");
        myAlertInputDialog.getPositiveButton().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res=myAlertInputDialog.getResult();
                if(TextUtils.isEmpty(res)||res.contains("请输入")){
                    showMsg("请输入检测结果");
                    return;
                }
                double r=Double.valueOf(res);
                if(Double.compare(r,8)>=0&&Double.compare(r,20)<=0) {

                    mPresenter.postCheck(resultsBeans.get(pos).getId(), myAlertInputDialog.getResult());
                }else {
                    showMsg("超出范围");
                }
                myAlertInputDialog.dismiss();
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myAlertInputDialog.dismiss();
            }
        });
        myAlertInputDialog.show();
    }
}
