package com.xxx.ency.view.work;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.xxx.ency.R;
import com.xxx.ency.base.BaseMVPActivity;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.AreaContract;
import com.xxx.ency.di.component.DaggerAreaModuleComponent;
import com.xxx.ency.di.module.AreaWorkModule;
import com.xxx.ency.model.bean.SpaceBean;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.presenter.AreaPresenter;
import com.xxx.ency.view.login.LoginActivity;
import com.xxx.ency.view.move.MoveActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AreaActivity extends BaseMVPActivity<AreaPresenter> implements
        AreaContract.View {
    @BindView(R.id.toolbar_rightitle)
    TextView toolbarRightitle;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Inject
    SharePrefManager sharePrefManager;
    String areaId;
    @BindView(R.id.btn_post)
    Button btnPost;
    List<String> typeList = new ArrayList<>();
    List<String> crashList = new ArrayList<>();
    @BindView(R.id.tv_place_no)
    TextView tvPlaceNo;
    @BindView(R.id.tv_crash_type_value)
    TextView tvCrashTypeValue;
    @BindView(R.id.tv_type_crash)
    TextView tvCrashType;
    String content = "";

    @BindView(R.id.rlseclectplace)
    RelativeLayout rlseclectplace;
    @BindView(R.id.rlcrash)
    RelativeLayout rlcrash;
    @BindView(R.id.rlcrash_type)
    RelativeLayout rlcrashType;
    @BindView(R.id.tv_place)
    TextView tvPlace;
    @BindView(R.id.tv_out_tmp_value)
    TextView tvOutTmpValue;
    @BindView(R.id.tv_out_tmp)
    TextView tvOutTmp;
    String yichang = "";
    String tag = "";



    @Override
    protected int getLayoutId() {
        return R.layout.activity_area_manager;
    }

    private List<SpaceBean> mList = new ArrayList<>();

    @Override
    protected void initialize() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        tag = getIntent().getStringExtra("tag");
        if (TextUtils.isEmpty(tag)) {
            typeList.add("防汛安全");
            typeList.add("用电安全");
            typeList.add("防火安全");
            typeList.add("防爆安全");
            typeList.add("基础设施");
            typeList.add("设备安全");
            typeList.add("危险化学药剂");
            typeList.add("安全保卫");
            typeList.add("作业安全");
            typeList.add("其他");
            crashList.add("本区无安全隐患");
            crashList.add("隐患类型");
            toolbarTitle.setVisibility(View.VISIBLE);
            toolbarTitle.setText("区域情况");
            areaId = getIntent().getStringExtra("areaId");
            if (TextUtils.isEmpty(areaId)) {
                rlseclectplace.setVisibility(View.VISIBLE);
            } else {
                rlseclectplace.setVisibility(View.GONE);
            }
            initTypeSeclect();
            initSeclectCrash();
            mPresenter.getAreaList();
            rlcrash.setVisibility(View.VISIBLE);
            rlcrashType.setVisibility(View.GONE);
        } else {
            crashList.add("远程巡仓");
            crashList.add("现场巡仓");
            rlseclectplace.setVisibility(View.VISIBLE);
            toolbarTitle.setVisibility(View.VISIBLE);
            toolbarTitle.setText("移动巡仓");
            tvOutTmp.setText("检查类型");
            tvOutTmpValue.setText("请选择检查类型");
            tvPlace.setText("选择仓号");
            tvPlaceNo.setText("请选择仓号");
            mPresenter.getData();
            initSecletCheck();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private OptionsPickerView pvCustomOptionsPalce;
    private OptionsPickerView pvCustomOptionsCrash;

    private void initSeclect(final List<String> areas) {
        pvCustomOptionsPalce = new OptionsPickerBuilder(AreaActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = areas.get(options1);
                areaId = mList.get(options1).getNo();

                tvPlaceNo.setText(tx);
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
        pvCustomOptionsPalce.setPicker(areas);
    }

    boolean isCrashType = false;

    private void initSeclectCrash() {
        pvCustomOptionsCrash = new OptionsPickerBuilder(AreaActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = crashList.get(options1);
                tvOutTmpValue.setText(tx);
                isHasCrash = 1;
                if (crashList.size() - 1 == options1) {
                    isCrashType = true;

                    rlcrashType.setVisibility(View.VISIBLE);
                } else {
                    isCrashType = false;

                    rlcrashType.setVisibility(View.GONE);
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
                                pvCustomOptionsCrash.returnData();
                                pvCustomOptionsCrash.dismiss();
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptionsCrash.dismiss();
                            }
                        });

                    }
                })
                .isDialog(false)
                .setOutSideCancelable(false)
                .build();
        pvCustomOptionsCrash.setPicker(crashList);
    }

    private void showDialog() {
        final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(AreaActivity.this).builder()
                .setTitle("请输入异常情况")
                .setEditText("");
        myAlertInputDialog.getContentEditText().setText(content);
        myAlertInputDialog.getPositiveButton().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = myAlertInputDialog.getResult();

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

    private OptionsPickerView pvCustomOptionsType;
    int index = -1;

    private void initSecletCheck() {
        pvCustomOptionsCrash = new OptionsPickerBuilder(AreaActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = crashList.get(options1);
                tvOutTmpValue.setText(tx);
                index = options1;

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
                                pvCustomOptionsCrash.returnData();
                                pvCustomOptionsCrash.dismiss();
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptionsCrash.dismiss();
                                index = -1;
                            }
                        });

                    }
                })
                .isDialog(false)
                .setOutSideCancelable(false)
                .build();
        pvCustomOptionsCrash.setPicker(crashList);
    }

    private void initTypeSeclect() {
        pvCustomOptionsType = new OptionsPickerBuilder(AreaActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置

                String tx = typeList.get(options1);
                tvCrashTypeValue.setText(tx);
                typeCrash = options1 + 1;
                yichang = tx;
                showDialog();

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
                                pvCustomOptionsType.returnData();
                                pvCustomOptionsType.dismiss();
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptionsType.dismiss();
                            }
                        });

                    }
                })
                .isDialog(false)
                .setOutSideCancelable(false)
                .build();
        pvCustomOptionsType.setPicker(typeList);
    }

    @Override
    protected void initInject() {
        DaggerAreaModuleComponent.builder()
                .appComponent(EncyApplication.getAppComponent())
                .areaWorkModule(new AreaWorkModule()).build()
                .inject(this);

    }


    @Override
    public void showSucuss() {
        showMsg("成功");
        EventBus.getDefault().post(100);
        finish();
    }

    @Override
    public void failGetHotData() {

    }

    @Override
    public String getUserid() {
        return sharePrefManager.getUserId();
    }

    @Override
    public String getAreaId() {
        return areaId;
    }

    @Override
    public String getLatitude() {
        return EncyApplication.Latitude;
    }

    @Override
    public String getLongitude() {
        return EncyApplication.Longitude;
    }

    int isHasCrash = 0;
    int typeCrash = -1;


    @Override
    public String getDes() {
        return content;

    }

    @Override
    public void clearData() {
        sharePrefManager.clearData();
        final MyAlertDialog myAlertDialog = new MyAlertDialog(AreaActivity.this).builder()
                .setTitle("提示")
                .setMsg("该账号在其他设备上登录，请重新登录");
        myAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AreaActivity.this, LoginActivity.class);
                        intent.putExtra("tag", "exit");
                        startActivity(intent);

                    }
                });
        myAlertDialog.show();

    }

    @Override
    public int crashType() {
        return typeCrash;
    }

    @Override
    public int isHasCrash() {
        return isHasCrash;
    }

    @Override
    public void showSpaceBean(List<SpaceBean> list) {
        if (list != null && list.size() > 0) {
            mList.addAll(list);
            List<String> str = new ArrayList<>();
            for (SpaceBean bean : list) {
                str.add(bean.getName());
            }
            initSeclect(str);
        }

    }

    @Override
    public void showArea(List<SpaceBean> list) {
        if (list != null && list.size() > 0) {
            mList.addAll(list);
            List<String> str = new ArrayList<>();
            for (SpaceBean bean : list) {
                str.add(bean.getName());
            }
            initSeclect(str);
        } else {
            showMsg("暂无管理区域");
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.rlseclectplace, R.id.rlcrash, R.id.rlcrash_type, R.id.btn_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlseclectplace:

                if (null != pvCustomOptionsPalce) {
                    pvCustomOptionsPalce.show();
                }
                break;
            case R.id.rlcrash:
                if (null != pvCustomOptionsCrash) {
                    pvCustomOptionsCrash.show();
                }
                break;
            case R.id.rlcrash_type:
                if (null != pvCustomOptionsType) {
                    pvCustomOptionsType.show();
                }
                break;
            case R.id.btn_post:
                if (TextUtils.isEmpty(tag)) {
                    if (tvPlaceNo.getText().toString().contains("选择")) {
                        showMsg("请选择区域");
                        return;
                    }
                    if (tvOutTmpValue.getText().toString().contains("有无")) {
                        showMsg("请选择有无异常");
                        return;
                    }
                    if (isCrashType && tvCrashTypeValue.getText().toString().contains("请选择隐患类型")) {
                        showMsg("请选择隐患类型");
                        return;
                    }
                    if(isCrashType&&TextUtils.isEmpty(content)){
                        showMsg("请输入异常描述");
                        return;
                    }

                    EventBus.getDefault().post(666);
                    mPresenter.postArea();
                } else {
                    if (tvPlaceNo.getText().toString().contains("选择")) {
                        showMsg("请选择仓号");
                        return;
                    }
                    if (tvOutTmpValue.getText().toString().contains("选择")) {
                        showMsg("请选择检查类型");
                        return;
                    }
                    if (index == 0) {
                        //远程
                        Intent i = new Intent(AreaActivity.this, MoveActivity.class);
                        i.putExtra("id", areaId);
                        i.putExtra("tag", "1");
                        startActivity(i);
                    } else {
                        //现场
                        Intent i = new Intent(AreaActivity.this,MoveActivity.class);
                        i.putExtra("id", areaId);
                        i.putExtra("tag", "2");
                        startActivity(i);
                    }
                    finish();
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(222);
    }
}
