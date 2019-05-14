package com.xxx.ency.view.SecurityCheck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gavin.com.library.StickyDecoration;
import com.gavin.com.library.listener.GroupListener;
import com.gavin.com.library.listener.OnGroupClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hb.dialog.dialog.LoadingDialog;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.jaygoo.selector.MultiSelectPopWindow;
import com.xxx.ency.R;
import com.xxx.ency.base.BaseMVPFragment;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.SecurityCheckListContract;
import com.xxx.ency.di.component.DaggerSecurityCheckFragmentListComponent;
import com.xxx.ency.di.module.SecurityCheckFragmentListModule;
import com.xxx.ency.model.bean.CheckListBean;
import com.xxx.ency.model.bean.CheckWarehouse;
import com.xxx.ency.model.bean.CheckWork;
import com.xxx.ency.model.bean.FixedCheckBean;
import com.xxx.ency.model.bean.checkItemValueList;
import com.xxx.ency.model.db.GreenDaoManager;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.presenter.SecurityCheckListPresenter;
import com.xxx.ency.util.DensityUtil;
import com.xxx.ency.view.login.LoginActivity;

import com.xxx.ency.view.work.WorkTypeData;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SecurityCheckListFragment extends BaseMVPFragment<SecurityCheckListPresenter> implements
        SecurityCheckListContract.View {
    @BindView(R.id.recycleview)
    RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    Unbinder unbinder;
    @BindView(R.id.btn_no_agree)
    Button btnNoAgree;
    @BindView(R.id.btn_agree)
    Button btnAgree;
    @BindView(R.id.llshenpi)
    LinearLayout llshenpi;
    @BindView(R.id.llwork)
    LinearLayout llwork;
    private OptionsPickerView pvCustomOptionsPalce;
    ArrayList<String> str = new ArrayList<>();
    String value = "";
    /**
     * 项目位置
     */
    int position = -1;
    private List<CheckListBean> res;

    private String type;
    private String step;
    private String currentStep;
    private String roleId;
    /**
     * 作业id
     */
    private String jobId;
    /**
     * 项目列表归类
     */
    private Map<String, List<CheckListBean>> cachMap;

    /**
     * 以保存项目
     */
    private Map<String, List<CheckListBean>> saveMap = new HashMap<>();

    @Inject
    SharePrefManager sharePrefManager;
    /**
     * 风机数量
     */
    int funNum;

    /**
     *
     *风机功率
     * @return
     */
    double wt;
    double beforeTemp;

    @Override
    public String getUserid() {
        return sharePrefManager.getUserId();
    }

    @Override
    public String getRoomId() {
        return id;
    }

    @Override
    public String getCreateData() {
        return brow;
    }

    @Override
    public String getStep() {
        if("1".equals(step)){
            return step;
        }else {
            if (!TextUtils.isEmpty(currentStep)) {
                int st = Integer.valueOf(currentStep);
                if (st == 3 || st == 4) {
                    step = st + "";
                }
            }
            return step;
        }



    }

    @Override
    public String getJobID() {
        return jobId;
    }

    @Override
    public String getRoleTye() {
        return sharePrefManager.getLocalMode()+"";
    }

    @Override
    public String fromTo() {
        return fromTo;
    }

    @Override
    public void getFunNumAndWT(String Num, String t,String before) {
        funNum=Integer.valueOf(Num);
        wt=Double.valueOf(t)/1000;
        beforeTemp=Double.valueOf(before);
    }

    private GreenDaoManager daoManager;

    @Override
    public void showPostSucuss() {
        showMsg("提交成功");
        new Handler().postDelayed(new Runnable(){
            public void run() {
                EventBus.getDefault().post(100);
                if (!TextUtils.isEmpty(currentStep)) {
                    int st = Integer.valueOf(currentStep);
                        if (st == 4) mPresenter.jobUpdate(jobId, "3", "");
                        else mActivity.finish();
                }else {
                    if(!TextUtils.isEmpty(step)) {
                        int st = Integer.valueOf(step);
                        if (st == 2) mPresenter.jobUpdate(jobId, "3", "");
                        else mActivity.finish();
                    }
                   else mActivity.finish();
                }
            }
        }, 500);



    }
    WorkTypeData workTypeData;

    @Override
    public void getCheckReport(List<CheckListBean> list) {
        if(list.size()==0){
                mPresenter.getCheckData();
                ((SecurityListActivity) getActivity()).setSaveVisule();
        }else {
//            if("1".equals(step)&&"3".equals(currentStep)&&sharePrefManager.getLocalMode()==1) {
//                //如果是3步的时候可以修改申请表
//                ((SecurityListActivity) getActivity()).setSaveVisule();
//
//            }else {
                ((SecurityListActivity) getActivity()).setSaveGone();
//            }
            getCheckList(list);
            if (sharePrefManager.getLocalMode() == 5&&"1".equals(step)&&"1".equals(fromTo)) {
                llshenpi.setVisibility(View.VISIBLE);
            }

        }

    }

    @Override
    public void jobUpRes() {
        EventBus.getDefault().post(100);
        getActivity().finish();
    }

    @Override
    public void goLoginFaile() {
        sharePrefManager.clearData();
        final MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity()).builder()
                .setTitle("提示")
                .setMsg("您的账号在其他设备上登录，请重新登录");
        myAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                intent.putExtra("tag","exit");
                startActivity(intent);
                myAlertDialog.dismiss();
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        myAlertDialog.show();

    }


    public static SecurityCheckListFragment newInstance(String str, String id, String isBrow,
                                                        String step, String jobId,String fromto,
                                                        String currentStep,String roleId) {
        SecurityCheckListFragment gankFragment = new SecurityCheckListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", str);
        bundle.putString("id", id);
        bundle.putString("isBrow", isBrow);
        bundle.putString("step", step);
        bundle.putString("jobId", jobId);
        bundle.putString("fromTo", fromto);
        bundle.putString("currentStep", currentStep);
        bundle.putString("roleId", roleId);
        gankFragment.setArguments(bundle);
        return gankFragment;
    }


    @Override
    protected void initInject() {
        DaggerSecurityCheckFragmentListComponent.builder()
                .appComponent(EncyApplication.getAppComponent())
                .securityCheckFragmentListModule(new SecurityCheckFragmentListModule())
                .build().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_check_list;
    }

    TimePickerView pvTime;
    String id;
    boolean isBrow = true;
    String brow = "";
    String fromTo="";

    @Override
    protected void initialize() {
        Bundle args = getArguments();
        if (args != null) {
            type = args.getString("type");
            id = args.getString("id");
            brow = args.getString("isBrow");
            step = args.getString("step");
            jobId = args.getString("jobId");
            fromTo = args.getString("fromTo");
            currentStep = args.getString("currentStep");
            roleId = args.getString("roleId");
        }
        daoManager = EncyApplication.getAppComponent().getGreenDaoManager();

        if(!TextUtils.isEmpty(type)) {
            if(Integer.valueOf(type)>2) {
                workTypeData = (WorkTypeData) EncyApplication.getInstance().workMap.
                        get(Integer.valueOf(type));

                int code = Integer.valueOf(workTypeData.getCode());
                if (sharePrefManager.getLocalMode() == 1 && (code == 6 || code == 7) && "2".equals(fromTo)) {
                    if("3".equals(currentStep)||"2".equals(currentStep)) {
                        llwork.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        if (TextUtils.isEmpty(brow)) {
            if ("1".equals(type)) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String data=df.format(new Date());
                FixedCheckBean bean=daoManager.queryByNoBeanFixedWork(getRoomId(),getUserid(),data);
                if(bean!=null){
                    String json = bean.getResultJson();
                    Gson gson = new Gson();
                    JsonParser parser = new JsonParser();
                    JsonArray Jarray = parser.parse(json).getAsJsonArray();
                    ArrayList<CheckListBean> lcs = new ArrayList<CheckListBean>();
                    for (JsonElement obj : Jarray) {
                        CheckListBean cse = gson.fromJson(obj, CheckListBean.class);
                        lcs.add(cse);
                        postMap(cse);
                    }
                    getCheckList(lcs);

                }else {
                    mPresenter.getCheckData();
                }
                pvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        if (position != -1) {
                            res.get(position).setTxtValue(getTime(date));
                            res.get(position).setShowText(getTime(date));

                            postMap(res.get(position));
                            getCheckList(res);

                        }
                    }
                }).setCancelColor(getResources().getColor(R.color.colorPrimary)).
                        setSubmitColor(getResources().getColor(R.color.colorPrimary)).
                        setType(new boolean[]{true, true, true, true, true, false}).
                        build();
            } else if ("2".equals(type)) {
                if (daoManager.queryByNo(getRoomId(), getUserid())) {
                    CheckWarehouse bean = daoManager.queryByNoBean(getRoomId(),
                            getUserid());
                    String json = bean.getResultJson();
                    Gson gson = new Gson();
                    JsonParser parser = new JsonParser();
                    JsonArray Jarray = parser.parse(json).getAsJsonArray();
                    ArrayList<CheckListBean> lcs = new ArrayList<CheckListBean>();
                    for (JsonElement obj : Jarray) {
                        CheckListBean cse = gson.fromJson(obj, CheckListBean.class);
                        lcs.add(cse);
                        postMap(cse);
                    }

                    getCheckList(lcs);

                } else {
                    mPresenter.getCheckData();
                }
                pvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        if (position != -1) {
                            res.get(position).setTxtValue(getTime(date));
                            res.get(position).setShowText(getTime(date));

                            postMap(res.get(position));
                            getCheckList(res);

                        }
                    }
                }).setCancelColor(getResources().getColor(R.color.colorPrimary)).
                        setSubmitColor(getResources().getColor(R.color.colorPrimary)).
                        setType(new boolean[]{true, true, true, true, true, false}).
                        build();
            } else  {

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String data=df.format(new Date());

                if(!TextUtils.isEmpty(jobId)){
                    CheckWork bean=daoManager.queryByNoBeanCheckWork(jobId,getUserid(),data);
                    if(bean!=null) {
                        String json = bean.getResultJson();
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonArray Jarray = parser.parse(json).getAsJsonArray();
                        ArrayList<CheckListBean> lcs = new ArrayList<CheckListBean>();
                        for (JsonElement obj : Jarray) {
                            CheckListBean cse = gson.fromJson(obj, CheckListBean.class);
                            lcs.add(cse);
                            postMap(cse);
                        }

                        getCheckList(lcs);
                        ((SecurityListActivity) getActivity()).setSaveVisule();
                    }else {
                            mPresenter.getReport();
                    }
                }else {
                    mPresenter.getCheckData();
                }
                pvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        if (position != -1) {
                            res.get(position).setTxtValue(getTime(date));
                            res.get(position).setShowText(getTime(date));

                            postMap(res.get(position));
                            getCheckList(res);

                        }
                    }
                }).setCancelColor(getResources().getColor(R.color.colorPrimary)).
                        setSubmitColor(getResources().getColor(R.color.colorPrimary)).
                        setType(new boolean[]{true, true, true, false, false, false}).
                        build();

                if("2".equals(step)&&"3".equals(type)) {
                    mPresenter.getReportFan();
                }

            }
        } else {
//            if("1".equals(step)&&"3".equals(currentStep)&&sharePrefManager.getLocalMode()==1&&"1".equals(fromTo)){
//                isBrow = true;
////                mPresenter.getCheckData();
//
//            }else {

                isBrow = false;
//            }
            mPresenter.getReport();

        }




    }
    double allPt=-1;

    Map<String,Double> mapVar=new HashMap<>();
    private int altPostion=-1;
    private int siglePostion=-1;


    @Override
    public void getCheckList(final List<CheckListBean> results) {

        cachMap = new HashMap<>();
        cachMap.clear();
        this.res = results;
        generateMap(results);
        if (mAdapter == null) {
            mRecyclerView.removeAllViews();
            RecyclerView.LayoutManager manager;
            manager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(manager);


            StickyDecoration decoration = StickyDecoration.Builder
                    .init(new GroupListener() {
                        @Override
                        public String getGroupName(int position) {
                            return results.get(position).getParentTitle();
                        }
                    })
                    .setGroupBackground(getActivity().getResources().getColor(R.color.gray1))        //背景色
                    .setGroupHeight(DensityUtil.dip2px(getActivity(), 35))     //高度
                    .setDivideColor(getActivity().getResources().getColor(R.color.line))            //分割线颜色
                    .setDivideHeight(DensityUtil.dip2px(getActivity(), 2))     //分割线高度 (默认没有分割线)
                    .setGroupTextColor(getActivity().getResources().getColor(R.color.gray))                                    //字体颜色 （默认）
                    .setGroupTextSize(DensityUtil.sp2px(getActivity(), 15))    //字体大小
                    .setTextSideMargin(DensityUtil.dip2px(getActivity(), 10))  // 边距   靠左时为左边距  靠右时为右边距
                    .setOnClickListener(new OnGroupClickListener() {
                        @Override
                        public void onClick(int position, int id) {
                            if(!TextUtils.isEmpty(res.get(position).getParentTip()))
                                showScrollviewDialog(res.get(position).getParentTip());
                        }
                    }).
                            build();


            mRecyclerView.addItemDecoration(decoration);

            mAdapter = new RecyclerView.Adapter() {
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view=null;
                    if(isBrow) {
                        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent,
                                false);
                    }else {
                        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_brow,
                                parent, false);
                    }
                    return new Holder(view);
                }

                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position1) {
                    final Holder holder = (Holder) viewHolder;
                    holder.mTvChild.setText(results.get(position1).getChildTitle());
                    if (TextUtils.isEmpty(results.get(position1).getShowText())) {
                        holder.mTvValue.setText(results.get(position1).getChildHints());
                    } else {
//
                        holder.mTvValue.setText(results.get(position1).getShowText());
                    }

                    if ( "75".equals(res.get(position1).getId())) {
                        altPostion=position1;
                        if(allPt!=-1)
                            holder.mTvValue.setText(allPt+"");
                        if(!TextUtils.isEmpty(results.get(position1).getShowText()))
                            mapVar.put("75",  Double.valueOf(results.get(position1).getShowText()));

                    }
                    if ( "80".equals(res.get(position1).getId())&&!TextUtils.isEmpty(results.get(position1).getShowText())) {
                        mapVar.put("80", Double.valueOf(results.get(position1).getShowText()));

                    }


                    /**
                     * 总电耗 75，累计通风时长80，数量79，通风前温度63，通风后温度66
                     */
                    if ("79".equals(res.get(position1).getId()) &&
                            !TextUtils.isEmpty(res.get(position1).getShowText())) {
                        mapVar.put("79", Double.valueOf(res.get(position1).getTxtValue()));
                    }

//                        if ("63".equals(res.get(position1).getId()) &&
//                                !TextUtils.isEmpty(res.get(position1).getShowText())) {
//                            mapVar.put("63", Double.valueOf(res.get(position1).getTxtValue()));
//                        }
                    if ("66".equals(res.get(position1).getId()) &&
                            !TextUtils.isEmpty(res.get(position1).getShowText())) {
                        mapVar.put("66", Double.valueOf(res.get(position1).getTxtValue()));
                    }
                    if ("76".equals(res.get(position1).getId())) {
                        siglePostion=position1;
                        if(mapVar.size()==4) {
                            double temp1 = beforeTemp;
                            double temp2 = Double.valueOf(mapVar.get("66"));
                            double temp = Math.abs(temp1 - temp2);
                            if (Double.compare(temp1, temp2) == 0) {
                                temp = 1;
                            }
                            double p = (mapVar.get("75") * Double.valueOf(mapVar.get("80"))) / (
                                    Double.valueOf(mapVar.get("79")) * temp);
                            holder.mTvValue.setText(String.format("%.2f", p));
                        }

                    }
                    if(position1==res.size()-1&&mapVar.size()>0){
                        getCheckList(res);
                    }
                    holder.mTvChild.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!TextUtils.isEmpty(res.get(position1).getTip()))
                                showScrollviewDialog(res.get(position1).getTip());
                        }
                    });
//                        showScrollviewDialog()
                    holder.mTvValue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(TextUtils.isEmpty(holder.mTvValue.getText().toString())){
                                return;
                            }
                            if (isBrow) {
                                position = position1;
                                setItemsList(results.get(position).getStrValues(),
                                        results.get(position).getStritems(),
                                        results.get(position).getType(), position);
                            }


                        }
                    });


                }

                @Override
                public int getItemCount() {
                    return results.size();
                }
            };

            mRecyclerView.setAdapter(mAdapter);
        } else {
            if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && (mRecyclerView.isComputingLayout() == false)) {
                mAdapter.notifyDataSetChanged();
            }

        }


    }

    /**
     * 更新缓存数据结果
     * @param res
     */

    private void generateMap(List<CheckListBean> res) {
        for (CheckListBean bean : res) {
            if (!cachMap.containsKey(bean.getCheckId())) {
                List<CheckListBean> child = new ArrayList<>();
                child.add(bean);
                cachMap.put(bean.getCheckId(), child);
            } else {
                cachMap.get(bean.getCheckId()).add(bean);
            }
        }
    }

    private void postMap(CheckListBean res) {

        if (!saveMap.containsKey(res.getCheckId())) {
            List<CheckListBean> child = new ArrayList<>();
            child.add(res);
            saveMap.put(res.getCheckId(), child);
        } else {
            //map中是否含有
            boolean isHas = false;
            for (CheckListBean bean : saveMap.get(res.getCheckId())) {
                if (res.getId().equals(bean.getId())) {
                    bean.setShowText(res.getShowText());
                    bean.setTxtValue(res.getTxtValue());
                    isHas = true;
                    break;
                }
            }
            if (!isHas) saveMap.get(res.getCheckId()).add(res);
        }

    }

    @OnClick({R.id.btn_no_agree, R.id.btn_agree,R.id.btn_bu,R.id.btn_fang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_no_agree:
                showNoAgreeDialog();
                break;
            case R.id.btn_agree:
                mPresenter.jobUpdate(jobId,"2","");
                break;
            case R.id.btn_bu:
                showBu();
                break;
            case R.id.btn_fang:
                showFang();
                break;
        }
    }
    private void showNoAgreeDialog() {
        final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(getActivity()).builder()
                .setTitle("请输入驳回原因")
                .setEditText("");
        myAlertInputDialog.getContentEditText().setMaxEms(50);
        myAlertInputDialog.getPositiveButton().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(myAlertInputDialog.getResult())){
                    showMsg("请输入驳回原因");
                    return;
                }
                if(myAlertInputDialog.getResult().length()>50){
                    showMsg("驳回原因过长，请精简");
                    return;
                }
                mPresenter.jobUpdate(jobId,"4",myAlertInputDialog.getResult());
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
    private void showBu() {
        if(Integer.valueOf(currentStep)==3) {
            showMsg("你已经进行了补药操作");
            return;
        }
        if (checkIsFinish()) return;

        final MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity()).builder()
                .setTitle("提示")
                .setMsg("确定要进行补药操作吗");
        myAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mPresenter.jobUpdate(jobId,"1","");
                mPresenter.jobApplyStep(roleId,"3",jobId);

//                Intent intent2 = new Intent(getActivity(), WeituoActivity.class);
//                intent2.putExtra("title", "作业申请");
//                intent2.putExtra("roleId", "5");
//                intent2.putExtra("type", type);
//                intent2.putExtra("id",id);
//                intent2.putExtra("multipleType", 3);
//                startActivity(intent2);
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        myAlertDialog.show();
    }
    private void showFang() {
        if(Integer.valueOf(currentStep)==4) {
            showMsg("你已经进行了散气操作");
            return;
        }
        if (checkIsFinish()) return;

        final MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity()).builder()
                .setTitle("提示")
                .setMsg("确定要进行散气操作吗");
        myAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mPresenter.jobUpdate(jobId,"1","");
                mPresenter.jobApplyStep(roleId,"4",jobId);

//                Intent intent2 = new Intent(getActivity(), WeituoActivity.class);
//                intent2.putExtra("title", "作业申请");
//                intent2.putExtra("roleId", "5");
//                intent2.putExtra("type", type);
//                intent2.putExtra("id",id);
//                intent2.putExtra("multipleType", 4);
//                startActivity(intent2);
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        myAlertDialog.show();
    }

    private boolean checkIsFinish() {
        for (Map.Entry<String, List<CheckListBean>> entry : saveMap.entrySet()) {
            if (entry.getValue().size() != cachMap.get(entry.getKey()).size()) {
                String msg = entry.getValue().get(0).getParentTitle();
                showMsg("请将" + msg + "的信息输入完整");
                return true;
            }
        }
        if (saveMap.size() != cachMap.size()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String data = sdf.format(new Date());
            CheckWork bean = daoManager.queryByNoBeanCheckWork(getJobID(), getUserid(), data);
            if (bean != null) {
                daoManager.deleteByUseridAndNoCheckWork(getJobID(), getUserid(), data);
            }
            daoManager.insertCheckWork(gotoCheck());
            showMsg("请完成登记表填写");
            return true;
        }
        return false;
    }

    static class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_child)
        TextView mTvChild;
        @BindView(R.id.tv_value)
        TextView mTvValue;
        @BindView(R.id.ll_item)
        LinearLayout mllitem;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setVisibility(boolean visible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (visible) {
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }
    }

    @Override
    public void failGetData() {

    }


    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initPvOptions();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initPvOptions() {


        pvCustomOptionsPalce = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置

                value = str.get(options1);
                if (position != -1) {
                    res.get(position).setTxtValue(res.get(position).getStritems()[options1]);
                    res.get(position).setShowText(res.get(position).getStrValues()[options1]);
                    if (res.get(position).getType() == 5) {
                        if ("是".equals(res.get(position).getStrValues()[options1])) {
                            String content = res.get(position + 1).getShowText();
                            if (TextUtils.isEmpty(content)) content = "";
                            showDialog(content);
                        } else {
                            res.get(position + 1).setShowText("");
                            res.get(position + 1).setTxtValue(" ");
                            postMap(res.get(position+1));
                            postMap(res.get(position));
                            getCheckList(res);
                        }
                    } else if (res.get(position).getType() == -5) {
                        if ("是".equals(res.get(position).getStrValues()[options1])) {
                            res.get(position + 1).setShowText("");
                            res.get(position + 1).setTxtValue(" ");
                            postMap(res.get(position));
                            postMap(res.get(position+1));
                            getCheckList(res);

                        } else {
                            String content = res.get(position + 1).getShowText();
                            if (TextUtils.isEmpty(content)) content = "";
                            showDialog(content);

                        }
                    } else if(res.get(position).getType() == 9){
                        if (!"0".equals(res.get(position).getStritems()[options1])) {
                            String content = res.get(position + 1).getShowText();
                            if (TextUtils.isEmpty(content)) content = "";
                            showDialog(content);
                        } else {
                            res.get(position + 1).setShowText("");
                            postMap(res.get(position+1));
                            postMap(res.get(position));
                            getCheckList(res);
                        }
                    }

                    else {
                        postMap(res.get(position));
                        getCheckList(res);
                    }
                }


            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);

                        TextView tvCancel = (TextView) v.findViewById(R.id.tv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptionsPalce.returnData();
                                pvCustomOptionsPalce.dismiss();
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptionsPalce.dismiss();
                                position = -1;
                            }
                        });

                    }
                })
                .isDialog(false)
                .setOutSideCancelable(false)
                .build();
    }

    /**
     * 范围是数字还是文本
     * <p>
     * 2，单选
     * 4，多选
     * 5.表示是否有异常，
     * -5.表示设备是否完好
     * 7，文本
     */
    private void setItemsList(String[] items, String[] values, int type, final int pos) {
        str.clear();

        switch (type) {
            case -5:
                str.addAll(Arrays.asList(items));
                pvCustomOptionsPalce.setPicker(str);
                position = pos;
                pvCustomOptionsPalce.show();
                break;
            case 1:
                showDialog7(res.get(position).getShowText());
                break;
            case 2:

                str.addAll(Arrays.asList(items));
                pvCustomOptionsPalce.setPicker(str);
                position = pos;
                pvCustomOptionsPalce.show();
                break;
            case 4:
                str.addAll(Arrays.asList(items));
                new MultiSelectPopWindow.Builder(getActivity())
                        .setNameArray(str)
                        .setConfirmListener(new MultiSelectPopWindow.OnConfirmClickListener() {
                            @Override
                            public void onClick(ArrayList<Integer> indexList, ArrayList<String>
                                    selectedList) {
                                position = pos;
                                StringBuilder sb = new StringBuilder();
                                StringBuilder sb1 = new StringBuilder();
                                for (int i = 0; i < indexList.size(); i++) {
                                    if (i > 0) {
                                        sb.append(",");
                                        sb1.append(",");
                                    }
                                    sb.append(res.get(position).getStritems()[indexList.get(i)]);
                                    sb1.append(res.get(position).getStrValues()[indexList.get(i)]);
                                }
                                res.get(position).setTxtValue(sb.toString());
                                res.get(position).setShowText(sb1.toString());
                                postMap(res.get(position));
                                getCheckList(res);

                            }
                        })
                        .setCancel("取消")
                        .setConfirm("完成")
                        .setTitle("类别列表")
                        .setCancelTextColor(getResources().getColor(R.color.colorPrimary))
                        .setConfirmTextColor(getResources().getColor(R.color.colorPrimary))
                        .build()
                        .show(rootView.findViewById(R.id.mBottom));
                break;
            case 5:
                str.addAll(Arrays.asList(items));
                pvCustomOptionsPalce.setPicker(str);
                position = pos;
                pvCustomOptionsPalce.show();
                break;
            case 6:
                position = pos;
                pvTime.show();
                break;
            case 7:
                showDialogTitle(res.get(position).getShowText());
                break;
            case 8:
                showDialog8(res.get(position).getShowText());
                break;
            case 9:
                /**
                 * 单选 正常，霉变，异常的情况
                 */
                str.addAll(Arrays.asList(items));
                pvCustomOptionsPalce.setPicker(str);
                position = pos;
                pvCustomOptionsPalce.show();
                break;
            case 10:
                /**
                 * 多选 异常的情况
                 */
                str.addAll(Arrays.asList(items));
                new MultiSelectPopWindow.Builder(getActivity())
                        .setNameArray(str)
                        .setConfirmListener(new MultiSelectPopWindow.OnConfirmClickListener() {
                            @Override
                            public void onClick(ArrayList<Integer> indexList, ArrayList<String>
                                    selectedList) {
                                position = pos;
                                StringBuilder sb = new StringBuilder();
                                StringBuilder sb1 = new StringBuilder();
                                for (int i = 0; i < indexList.size(); i++) {
                                    if (i > 0) {
                                        sb.append(",");
                                        sb1.append(",");
                                    }
                                    sb.append(res.get(position).getStritems()[indexList.get(i)]);
                                    sb1.append(res.get(position).getStrValues()[indexList.get(i)]);
                                }
                                if(sb1.toString().contains("正常")&&sb1.toString().length()>3){
                                    showMsg("正常情况不能和异常同时选择");
                                    return;
                                }
                                res.get(position).setTxtValue(sb.toString());
                                res.get(position).setShowText(sb1.toString());
                                if(res.get(position).getType() == 10){
                                    if("0".equals(sb.toString())){
                                        res.get(position + 1).setShowText("");
                                        postMap(res.get(position+1));

                                    }else {
                                        String content = res.get(position + 1).getShowText();
                                        if (TextUtils.isEmpty(content)) content = "";
                                        showDialog(content);
                                    }

                                }
                                postMap(res.get(position));
                                getCheckList(res);

                            }
                        })
                        .setCancel("取消")
                        .setConfirm("完成")
                        .setTitle("类别列表")
                        .setCancelTextColor(getResources().getColor(R.color.colorPrimary))
                        .setConfirmTextColor(getResources().getColor(R.color.colorPrimary))
                        .build()
                        .show(rootView.findViewById(R.id.mBottom));
                break;
            case 11:

                break;
            default:
                break;


        }
        if(allPt!=-1&&altPostion!=-1) {
            res.get(altPostion).setTxtValue(String.format("%.2f", allPt));
            res.get(altPostion).setShowText(String.format("%.2f", allPt));
        }
        if(mapVar.size()==4&&siglePostion!=-1){
            double temp1 = beforeTemp;
            double temp2 = Double.valueOf(mapVar.get("66"));
            double temp = Math.abs(temp1 - temp2);
            if (Double.compare(temp1, temp2) == 0) {
                temp = 1;
            }
            double p = (allPt * Double.valueOf(mapVar.get("80"))) / (
                    Double.valueOf(mapVar.get("79")) * temp);
            res.get(siglePostion).setTxtValue(String.format("%.2f", p));
            res.get(siglePostion).setShowText(String.format("%.2f", p));
        }


    }

    private void sumAlt(int position1){
        if ("80".equals(res.get(position1).getId()) &&
                !TextUtils.isEmpty(res.get(position1).getShowText())) {
            allPt = wt * funNum * Double.valueOf(res.get(position1).getTxtValue());
            mapVar.put("75", allPt);
            mapVar.put("80", Double.valueOf(res.get(position1).getTxtValue()));

        }
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

    @Override
    public void sucussJobApply() {
        mPresenter.postCheck(saveMap);
    }

    @Override
    public void postAddSucuss(String jobIdStr) {
        jobId=jobIdStr;
        mPresenter.postCheck(saveMap);

    }

    @Override
    public void setEnable() {
        ((SecurityListActivity) getActivity()).setEnable();
    }

    @Override
    public String getType() {
        return type;
    }

    private String getTime(Date date) {
        if("1".equals(type)||"2".equals(type)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.format(date);
        }else {
            SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
            return format3.format(date);
        }
    }

    private void showDialog(String content) {
        final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(getActivity()).builder()
                .setTitle("请输入异常原因措施")
                .setEditText("");
        myAlertInputDialog.getContentEditText().setText(content);
        myAlertInputDialog.getPositiveButton().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res.get(position + 1).setTxtValue(myAlertInputDialog.getResult());
                res.get(position + 1).setShowText(myAlertInputDialog.getResult());
                postMap(res.get(position));
                postMap(res.get(position + 1));
                getCheckList(res);
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

    private void showDialog7(String content) {
        if (TextUtils.isEmpty(content)) content = "";
        final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(getActivity()).builder()
                .setTitle("请输入异常原因措施")
                .setEditText("");
        myAlertInputDialog.getContentEditText().setText(content);
        myAlertInputDialog.getPositiveButton().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res.get(position).setTxtValue(myAlertInputDialog.getResult());
                res.get(position).setShowText(myAlertInputDialog.getResult());
                postMap(res.get(position));
                getCheckList(res);
                position = -1;

                myAlertInputDialog.dismiss();
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = -1;
                myAlertInputDialog.dismiss();
            }
        });
        myAlertInputDialog.show();

    }
    private void showDialogTitle(String content) {
        if (TextUtils.isEmpty(content)) content = "";
        final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(getActivity()).builder()
                .setTitle("请输入"+res.get(position).getChildTitle())
                .setEditText("");
        myAlertInputDialog.getContentEditText().setText(content);
        myAlertInputDialog.getPositiveButton().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res.get(position).setTxtValue(myAlertInputDialog.getResult());
                res.get(position).setShowText(myAlertInputDialog.getResult());
                postMap(res.get(position));
                getCheckList(res);
                position = -1;

                myAlertInputDialog.dismiss();
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = -1;
                myAlertInputDialog.dismiss();
            }
        });
        myAlertInputDialog.show();

    }
    private boolean isCheckList(List<CheckListBean> list){
        boolean isOk=true;
        for(CheckListBean bean:list) {

            if ((TextUtils.isEmpty(bean.getShowText()) || bean.getShowText().equals(bean.getChildHints()))&&!bean.getChildTitle().contains("异常")) {
               isOk=true;
               break;
            }else {
                isOk=false;
            }
        }
        return  isOk;
    }
    private boolean isInputList(List<CheckListBean> list){
        boolean isInput=false;
        for(CheckListBean bean:list) {
            if(bean==null||bean.getShowText()==null||"".equals(bean.getShowText())||bean.getChildHints().equals(bean.getShowText())){
                isInput=false;
            }else {
                isInput=true;
                break;
            }

        }
        return  isInput;
    }

    public void postSave() {
        for (Map.Entry<String, List<CheckListBean>> entry : saveMap.entrySet()) {
            if(isInputList(entry.getValue())) {
                if (isCheckList(entry.getValue())) {
                    String msg = entry.getValue().get(0).getParentTitle();
                    showMsg("请将" + msg + "的信息输入完整");
                    setEnable();
                    return;
                }
            }
        }

        if ("1".equals(type)) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String data = sdf.format(new Date());
            FixedCheckBean bean=daoManager.queryByNoBeanFixedWork(getRoomId(), getUserid(), data);
            if(bean!=null){
                daoManager.deleteByUseridAndNoFixedWork(getRoomId(), getUserid(),data);
            }
            FixedCheckBean checkWarehouse = gotoFixed();
            daoManager.insertCheckFixedWork(checkWarehouse);
            mPresenter.postCheck(saveMap);
            setEnable();
        } else if ("2".equals(type)) {
            if (saveMap.size() != cachMap.size()) {
                if (daoManager.queryByNo(getRoomId(), getUserid())) {
                    daoManager.deleteByUseridAndNo(getRoomId(), getUserid());
                    CheckWarehouse checkWarehouse = gotoCheckWarehouse();
                    daoManager.insertCheckWarehouse(checkWarehouse);
                } else {
                    CheckWarehouse checkWarehouse = gotoCheckWarehouse();
                    daoManager.insertCheckWarehouse(checkWarehouse);
                }
                setEnable();
                getActivity().finish();

            } else {

                List<checkItemValueList> list=new ArrayList<>();

                for (Map.Entry<String, List<CheckListBean>> entry : saveMap.entrySet()) {
                    for(CheckListBean bean:entry.getValue()){
                        checkItemValueList checkItem=new checkItemValueList();
                        checkItem.setCheckId(bean.getCheckId());
                        checkItem.setCheckItemId(bean.getId());
                        checkItem.setCheckItemValue(bean.getTxtValue());
                        checkItem.setChildTitle(bean.getChildTitle());
                        list.add(checkItem);
                    }
                }
                if (daoManager.queryByNo(getRoomId(), getUserid())) {
                    daoManager.deleteByUseridAndNo(getRoomId(), getUserid());
                    CheckWarehouse checkWarehouse = gotoCheckWarehouse();
                    daoManager.insertCheckWarehouse(checkWarehouse);
                } else {
                    CheckWarehouse checkWarehouse = gotoCheckWarehouse();
                    daoManager.insertCheckWarehouse(checkWarehouse);
                }
                for(checkItemValueList bean:list){
                    if(("").equals(bean.getCheckItemValue())||bean.getCheckItemValue()==null){
                        setEnable();
                        getActivity().finish();
                        return;
                    }
                }


                daoManager.deleteByUseridAndNo(getRoomId(), getUserid());

                mPresenter.postCheck(saveMap);


            }
        }  else  {

            if ("1".equals(step)) {
                if (saveMap.size() != cachMap.size()) {
                    //数据没有填写完
                    setEnable();
                    showMsg("请将申请表单填写完整");
                } else {

                    mPresenter.postAddWork(roleId);
                }

            } else if ("2".equals(step)&&TextUtils.isEmpty(currentStep)) {
                setEnable();
                if (saveMap.size() != cachMap.size()) {
                    saveWork();
                    showMsg("保存成功");
                    getActivity().finish();

                } else {
                    saveWorkStep();
                }

            }else {
                int current =Integer.valueOf(currentStep);
                if(current>=2) {
                    setEnable();
                            int code = Integer.valueOf(workTypeData.getCode());
                            if ( code == 6 || code == 7){
                                if(current==4){
                                    if (saveMap.size() != cachMap.size()) {
                                        saveWork();
                                        showMsg("保存成功");
                                        getActivity().finish();

                                    } else {
                                        saveWorkStep();
                                    }
                                }else {
                                    showMsg("请进行完散气操作保存！");
                                }


                            }else {
                                if (saveMap.size() != cachMap.size()) {
                                    saveWork();
                                    showMsg("保存成功");
                                    getActivity().finish();

                                } else {
                                    saveWorkStep();
                                }
                            }
                        }


                }


        }



    }

    /**
     * 保存作业
     */

    private void saveWorkStep() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String data = sdf.format(new Date());
        CheckWork bean = daoManager.queryByNoBeanCheckWork(getJobID(), getUserid(), data);
        if (bean != null) {
            daoManager.deleteByUseridAndNoCheckWork(getJobID(), getUserid(), data);
        }
        mPresenter.postCheck(saveMap);
    }

    private void saveWork() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String data = sdf.format(new Date());
        CheckWork bean = daoManager.queryByNoBeanCheckWork(getJobID(), getUserid(), data);
        if (bean != null) {
            daoManager.deleteByUseridAndNoCheckWork(getJobID(), getUserid(), data);
        }
        daoManager.insertCheckWork(gotoCheck());
    }

    @NonNull
    private CheckWarehouse gotoCheckWarehouse() {
        CheckWarehouse checkWarehouse = new CheckWarehouse();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        checkWarehouse.setDate(sdf.format(new Date()));
        checkWarehouse.setUserid(getUserid());
        checkWarehouse.setWarehouseNo(getRoomId());
        Gson gson = new Gson();
        String route = gson.toJson(res);
        checkWarehouse.setResultJson(route);
        return checkWarehouse;
    }
    @NonNull
    private CheckWork gotoCheck() {
        CheckWork checkWarehouse = new CheckWork();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        checkWarehouse.setDate(sdf.format(new Date()));
        checkWarehouse.setUserid(getUserid());
        checkWarehouse.setWarehouseNo(getJobID());
        checkWarehouse.setType("3");
        Gson gson = new Gson();
        String route = gson.toJson(res);
        checkWarehouse.setResultJson(route);
        return checkWarehouse;
    }
    @NonNull
    private FixedCheckBean gotoFixed() {
        FixedCheckBean checkWarehouse = new FixedCheckBean();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        checkWarehouse.setDate(sdf.format(new Date()));
        checkWarehouse.setUserid(getUserid());
        checkWarehouse.setWarehouseNo(getRoomId());
        checkWarehouse.setType("1");
        Gson gson = new Gson();
        String route = gson.toJson(res);
        checkWarehouse.setResultJson(route);
        return checkWarehouse;
    }


    private void showDialog8(String num) {
        if (TextUtils.isEmpty(num)) num = "0";
        final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(getActivity()).builder()
                .setTitle("请输入")
                .setEditText(num).setEditType(8194);
        myAlertInputDialog.getPositiveButton().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(myAlertInputDialog.getResult())){
                    showMsg("请输入数值");
                    return;
                }
                double start=Double.valueOf(res.get(position).getStrValues()[0]);
                double end=Double.valueOf(res.get(position).getStrValues()[1]);
                double result=Double.valueOf(myAlertInputDialog.getResult());
                if(Double.compare(result,start)>=0&&Double.compare(result,end)<=0){
                    res.get(position).setTxtValue(myAlertInputDialog.getResult());
                    res.get(position).setShowText(myAlertInputDialog.getResult());
                    postMap(res.get(position));
                    sumAlt(position);
                    getCheckList(res);
                }else {
                    showMsg("输入超范围");
                }
                position = -1;

                myAlertInputDialog.dismiss();
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = -1;
                myAlertInputDialog.dismiss();
            }
        });
        myAlertInputDialog.show();

    }
    private void showScrollviewDialog(String content){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.dialog_scroll, null);
        TextView tvcontent = (TextView) v.findViewById(R.id.tv_content);
        tvcontent.setText(Html.fromHtml(content));
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);
        dialog.setCancelable(true);


    }



}
