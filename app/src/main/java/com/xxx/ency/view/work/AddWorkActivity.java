package com.xxx.ency.view.work;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.xxx.ency.R;
import com.xxx.ency.base.BaseMVPActivity;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.AddWorkActivityContract;
import com.xxx.ency.di.component.DaggerAddWorkActivityComponent;
import com.xxx.ency.di.module.AddWorkActivityModule;
import com.xxx.ency.model.bean.SpaceBean;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.presenter.AddWorkPresenter;
import com.xxx.ency.view.SecurityCheck.SecurityListActivity;
import com.xxx.ency.view.login.LoginActivity;
import com.xxx.ency.view.main.MainActivity;
import com.xxx.ency.view.main.WorkType;
import com.xxx.ency.view.map.UpAdressActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加通风作业申请
 */
public class AddWorkActivity extends BaseMVPActivity<AddWorkPresenter> implements
        AddWorkActivityContract.View {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rlseclectplace)
    RelativeLayout rlseclectplace;
    @BindView(R.id.rloutroom)
    RelativeLayout rloutroom;
    @BindView(R.id.tv_out_tmp_value)
    TextView tvOutTmpValue;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @Inject
    SharePrefManager sharePrefManager;
    String id;
    @BindView(R.id.tv_place_no)
    TextView tvPlaceNo;
    private List<SpaceBean> mList = new ArrayList<>();
    List<String> tmpList = new ArrayList<>();
    private OptionsPickerView pvCustomOptionsPalce, pvCustomOptionsTemp;

    @Override
    protected void initInject() {
        DaggerAddWorkActivityComponent.builder()
                .appComponent(EncyApplication.getAppComponent())
                .addWorkActivityModule(new AddWorkActivityModule()).build()
                .inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_work;
    }
    String title;
    String auditor_user_id;

    @Override
    public void clearData() {
        sharePrefManager.clearData();
        final MyAlertDialog myAlertDialog = new MyAlertDialog(AddWorkActivity.this).builder()
                .setTitle("提示")
                .setMsg("该账号在其他设备上登录，请重新登录");
        myAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddWorkActivity.this,LoginActivity.class);
                intent.putExtra("tag","exit");
                startActivity(intent);


            }
        });
        myAlertDialog.show();
    }
    @Override
    protected void initialize() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        toolbarTitle.setVisibility(View.VISIBLE);
        title=getIntent().getStringExtra("title");
        id=getIntent().getStringExtra("id");
        auditor_user_id=getIntent().getStringExtra("roleId");
        if(null!=getIntent().getStringExtra("type"))
        type=Integer.valueOf(getIntent().getStringExtra("type"));
        if(title.contains("作业")) {
            WorkTypeData workTypeData= (WorkTypeData) EncyApplication.getInstance().workMap.
                    get(type);
            toolbarTitle.setText(workTypeData.getName()+"申请");

            rloutroom.setVisibility(View.GONE);
            mPresenter.getData();
        }else if(title.contains("送检")){
            toolbarTitle.setText("扦样送检");
            rloutroom.setVisibility(View.GONE);
            mPresenter.getData();
        }else {
            toolbarTitle.setText("仓储检查");
            initScan();
            rlseclectplace.setVisibility(View.GONE);
            rloutroom.setVisibility(View.VISIBLE);

        }



    }


    @Override
    public void showSpaceBean(List<SpaceBean> results) {
        if (results != null && results.size() > 0) {
            mList.addAll(results);
            List<String> str = new ArrayList<>();
            for (SpaceBean bean : results) {
                str.add(bean.getName());
            }
            initPlaces(str);
        }

    }
    @OnClick({R.id.rlseclectplace, R.id.rloutroom,
            R.id.btnnext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlseclectplace:
                if (pvCustomOptionsPalce != null) {
                    pvCustomOptionsPalce.show();
                }

                break;

            case R.id.rloutroom:
                if (pvCustomOptionsTemp != null) {
                    pvCustomOptionsTemp.show();
                }
                break;

            case R.id.btnnext:
                if(title.contains("作业")) {
                    if (tvPlaceNo.getText().toString().contains("请选择")) {
                        showTop("请选择仓号");
                        return;
                    }
                    Intent intent1=new Intent(AddWorkActivity.this,SecurityListActivity.class);
                    WorkTypeData workTypeData= (WorkTypeData) EncyApplication.getInstance().workMap.
                            get(type);
                    intent1.putExtra("title",workTypeData.getName()+"申请");
                    intent1.putExtra("step","1");
                    intent1.putExtra("id",id);
                    intent1.putExtra("type",type+"");
                    intent1.putExtra("roleId",auditor_user_id);
//                    intent1.putExtra("jobId",jobId);
                    intent1.putExtra("fromTo","5");
                    startActivity(intent1);
                    finish();


                }else if(title.contains("送检")){
                    if (tvPlaceNo.getText().toString().contains("请选择")) {
                        showTop("请选择仓号");
                        return;
                    }
                    mPresenter.postCheck();
                }else {
                    Intent intent1=new Intent(AddWorkActivity.this,SecurityListActivity.class);
                    if (type == 1) {
                        intent1.putExtra("title","定项检查");
                        intent1.putExtra("type","1");


                    } else if (type == 2) {
                        intent1.putExtra("title","入仓检查");
                        intent1.putExtra("type","2");

                    }


                    startActivity(intent1);
                }


                break;

        }
    }
    @Override
    public void sucuss(String jobId) {

        Intent intent1=new Intent(AddWorkActivity.this,SecurityListActivity.class);
        WorkTypeData workTypeData= (WorkTypeData) EncyApplication.getInstance().workMap.
                get(type);
        intent1.putExtra("title",workTypeData.getName()+"申请");
        intent1.putExtra("step","1");
        intent1.putExtra("id",id);
        intent1.putExtra("type",type+"");
        intent1.putExtra("jobId",jobId);
        intent1.putExtra("fromTo","5");
        startActivity(intent1);
        finish();

    }

    @Override
    public void sucuss() {
        EventBus.getDefault().post(100);
        showMsg("成功");
        new Handler().postDelayed(new Runnable(){
            public void run() {
                finish();
            }
        }, 1000);

    }

    @Override
    public String getUserid() {
        return sharePrefManager.getUserId();
    }

    @Override
    public String getRoomId() {
        return id;
    }

    @Override
    public int getType() {
        return type;
    }

    private void initPlaces(final List<String> str) {

        pvCustomOptionsPalce = new OptionsPickerBuilder(AddWorkActivity.this
        ,new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置

                String tx = str.get(options1);
                tvPlaceNo.setText(tx);
                id = mList.get(options1).getNo();


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
                    }
                });

            }
        })
                .isDialog(false)
                .setOutSideCancelable(false)
                .build();
        pvCustomOptionsPalce.setPicker(str);//添加数据
    }
    private void initTemp() {


        tmpList.add("机械通风作业");

        pvCustomOptionsTemp = new OptionsPickerBuilder(AddWorkActivity.this ,new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = tmpList.get(options1);
                tvOutTmpValue.setText(tx);


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
                                pvCustomOptionsTemp.returnData();
                                pvCustomOptionsTemp.dismiss();
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptionsTemp.dismiss();
                            }
                        });

                    }
                })

                .setSelectOptions(36)
                .setOutSideCancelable(false)
                .build();
        pvCustomOptionsTemp.setPicker(tmpList);//添加数据
    }
    int type;
    private void initScan() {


//        tmpList.add("定项检查");
        tmpList.add("入仓检查");

        pvCustomOptionsTemp = new OptionsPickerBuilder(AddWorkActivity.this ,new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = tmpList.get(options1);
                tvOutTmpValue.setText(tx);
                type=options1+1;

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
                                pvCustomOptionsTemp.returnData();
                                pvCustomOptionsTemp.dismiss();
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptionsTemp.dismiss();
                            }
                        });

                    }
                })

                .setSelectOptions(36)
                .setOutSideCancelable(false)
                .build();
        pvCustomOptionsTemp.setPicker(tmpList);//添加数据
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(222);
    }
}
