package com.xxx.ency.view.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.jaygoo.selector.MultiSelectPopWindow;
import com.xxx.ency.R;
import com.xxx.ency.base.BaseMVPActivity;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.UpAdressContract;
import com.xxx.ency.di.component.DaggerUpAdreessActivityComponent;
import com.xxx.ency.di.module.UpAdressActivityModule;
import com.xxx.ency.model.bean.SpaceBean;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.presenter.UpAdressPresenter;
import com.xxx.ency.view.login.LoginActivity;
import com.xxx.ency.view.main.MainActivity;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpAdressActivity extends BaseMVPActivity<UpAdressPresenter> implements
        UpAdressContract.View {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_value)
    TextView tvValue;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.toolbar_rightitle)
    TextView toolbarRightitle;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @Inject
    SharePrefManager sharePrefManager;
    @BindView(R.id.tv_value_cang)
    TextView tvValueCang;
    @BindView(R.id.rl_address_cang)
    RelativeLayout rlAddressCang;
    @BindView(R.id.tv_value_quyu)
    TextView tvValueQuyu;
    @BindView(R.id.rl_address_quyu)
    RelativeLayout rlAddressQuyu;
    @BindView(R.id.tv_address_quyu)
    TextView tvAddressQuyu;
    @BindView(R.id.tv_address_cang)
    TextView tvAddressCang;
    @BindView(R.id.tv_value_start)
    TextView tvValueStart;
    @BindView(R.id.rl_start)
    RelativeLayout rlStart;
    @BindView(R.id.tv_select_end)
    TextView tvSelectEnd;
    @BindView(R.id.rl_end)
    RelativeLayout rlEnd;

    @BindView(R.id.tv_address_start)
    TextView tvAddressStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.mBottom)
    View mBottom;
    private List<SpaceBean> mList = new ArrayList<>();
    private List<SpaceBean> mListArea = new ArrayList<>();

    @Override
    protected void initInject() {
        DaggerUpAdreessActivityComponent.builder()
                .appComponent(EncyApplication.getAppComponent())
                .upAdressActivityModule(new UpAdressActivityModule())
                .build().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.actitity_upaddrss;
    }

    String title;
    String tag;
    String toUsrid;

    TimePickerView pvTime, pvTime2;

    @Override
    protected void initialize() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        toolbarTitle.setVisibility(View.VISIBLE);
        title = getIntent().getStringExtra("title");
        toolbarTitle.setText(title);
        toolbarRightitle.setVisibility(View.VISIBLE);
        toolbarRightitle.setText("确定");
        tag = getIntent().getStringExtra("tag");
        toUsrid = getIntent().getStringExtra("touserid");
        if (TextUtils.isEmpty(getIntent().getStringExtra("tag"))) {
            rlAddress.setVisibility(View.VISIBLE);
            if (title.contains("区域")) {
                rlAddressQuyu.setVisibility(View.VISIBLE);
                mPresenter.getAreaData();
            } else {
                rlAddressCang.setVisibility(View.VISIBLE);
                mPresenter.getData();
            }
        } else {
            rlAddress.setVisibility(View.GONE);
            rlStart.setVisibility(View.VISIBLE);
            rlEnd.setVisibility(View.VISIBLE);
            if (title.contains("区域")) {
                rlAddressQuyu.setVisibility(View.VISIBLE);
                mPresenter.getAreaData();
            } else {
                rlAddressCang.setVisibility(View.VISIBLE);
                mPresenter.getData();
            }
            pvTime = new TimePickerBuilder(UpAdressActivity.this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    tvValueStart.setText(getTime(date));

                }
            }).setType(new boolean[]{true, true, true, false, false, false}).
                    setCancelColor(getResources().getColor(R.color.colorPrimary)).
                    setSubmitColor(getResources().getColor(R.color.colorPrimary)).build();
            pvTime2 = new TimePickerBuilder(UpAdressActivity.this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    tvSelectEnd.setText(getTime(date));

                }
            }).setType(new boolean[]{true, true, true, false, false, false}).
                   setCancelColor(getResources().getColor(R.color.colorPrimary)).
                    setSubmitColor(getResources().getColor(R.color.colorPrimary)).
                    build();

        }
    }

    @Override
    public void showAdress(String address) {
        tvValue.setText(address);
    }

    @Override
    public void postLationComplete(String msg) {
        if (TextUtils.isEmpty(tag)) {
            showMsg("上传成功");
            finish();
        } else {
            showMsg("添加任务成功");
            finish();
        }
    }

    @Override
    public String getUserid() {
        return sharePrefManager.getUserId();
    }

    @Override
    public void showSpaceBean(List<SpaceBean> results) {
        if (results != null && results.size() > 0) {
            mList.addAll(results);

            for (SpaceBean bean : results) {
                str.add(bean.getName());
            }
            initPlaces(str);
        }
    }


        @Override
        public void clearData() {
            sharePrefManager.clearData();
            final MyAlertDialog myAlertDialog = new MyAlertDialog(UpAdressActivity.this).builder()
                    .setTitle("提示")
                    .setMsg("该账号在其他设备上登录，请重新登录");
            myAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(UpAdressActivity.this,LoginActivity.class);
                    intent.putExtra("tag","exit");
                    startActivity(intent);


                }
            });
            myAlertDialog.show();
        }


    @Override
    public void showArea(List<SpaceBean> list) {
        if (list != null && list.size() > 0) {
            mListArea.addAll(list);

            for (SpaceBean bean : list) {
                str.add(bean.getName());
            }
            initAreas(str);

        }
    }

    @Override
    public String getRoomId() {
        return roomId;
    }

    @Override
    public String getAreaId() {
        return areaid;
    }

    @Override
    public void showWeituo() {

    }

    @Override
    public String getToUserid() {
        return toUsrid;
    }

    @Override
    public String type() {
        if (title.contains("区域")) {
            return "1";
        } else {
            return "2";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    LatLonPoint latLngPoint;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 1:
                if (data != null) {
                    latLngPoint = data.getParcelableExtra("Location");
                    String address = data.getStringExtra("Address");
                    mPresenter.getAdress(address);
                }
                break;
            default:
                break;
        }
    }

    private ArrayList<Integer> nList = new ArrayList<>();


    @OnClick({R.id.toolbar_rightitle, R.id.rl_address, R.id.rl_address_cang, R.id.rl_address_quyu,
            R.id.rl_start, R.id.rl_end})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_rightitle:
                if (TextUtils.isEmpty(tag)) {
                    if (null != latLngPoint)
                        mPresenter.postLation(latLngPoint, title);
                } else {
                    if(nList.size()>0) {
                      for(int i=0;i<nList.size();i++) {
                            if(title.contains("区域"))
                            mPresenter.postAddWeituo(tvValueStart.getText().toString(),
                                    tvSelectEnd.getText().toString(), mListArea.get(nList.get(i)).getNo());
                            else
                                mPresenter.postAddWeituo(tvValueStart.getText().toString(),
                                        tvSelectEnd.getText().toString(), mList.get(nList.get(i)).getNo());
                        }
                    }else {
                        showMsg("请选择委托列表");
                    }
                }
                break;
            case R.id.rl_address:
                Intent intent1 = new Intent();
                intent1.setClass(UpAdressActivity.this, AttendanceViewMap.class);
                startActivityForResult(intent1, 1);
                break;
            case R.id.rl_address_cang:

                if (!TextUtils.isEmpty(tag)) {

                    new MultiSelectPopWindow.Builder(UpAdressActivity.this)
                            .setNameArray(str)
                            .setConfirmListener(new MultiSelectPopWindow.OnConfirmClickListener() {
                                @Override
                                public void onClick(ArrayList<Integer> indexList, ArrayList<String>
                                        selectedList) {
                                    nList.clear();
                                    nList.addAll(indexList);
                                    StringBuilder sb = new StringBuilder();
                                    for (int i = 0; i < selectedList.size(); i++) {
                                        if (i > 0) {
                                            sb.append(",");

                                        }
                                        sb.append(selectedList.get(i));

                                    }
                                    tvValueCang.setText(sb.toString());
                                }
                            })
                            .setCancel("取消")
                            .setConfirm("完成")
                            .setTitle("委托列表")

                            .setCancelTextColor(getResources().getColor(R.color.colorPrimary))
                            .setConfirmTextColor(getResources().getColor(R.color.colorPrimary))
                            .build()


                            .show(findViewById(R.id.mBottom));
                }else {
                    if (pvCustomOptionsPalce != null) {
                        pvCustomOptionsPalce.show();
                    }
                }

                break;
            case R.id.rl_address_quyu:

                if (!TextUtils.isEmpty(tag)) {

                    new MultiSelectPopWindow.Builder(UpAdressActivity.this)
                            .setNameArray(str)
                            .setConfirmListener(new MultiSelectPopWindow.OnConfirmClickListener() {
                                @Override
                                public void onClick(ArrayList<Integer> indexList, ArrayList<String>
                                        selectedList) {
                                    nList.clear();
                                    nList.addAll(indexList);
                                    StringBuilder sb = new StringBuilder();
                                    for (int i = 0; i < selectedList.size(); i++) {
                                        if (i > 0) {
                                            sb.append(",");

                                        }
                                        sb.append(selectedList.get(i));

                                    }
                                    tvValueQuyu.setText(sb.toString());

                                }
                            })
                            .setCancel("取消")
                            .setConfirm("完成")
                            .setTitle("委托列表")

                            .setCancelTextColor(getResources().getColor(R.color.colorPrimary))
                            .setConfirmTextColor(getResources().getColor(R.color.colorPrimary))
                            .build()


                            .show(findViewById(R.id.mBottom));
                }else {
                    if (pvCustomOptionsArea != null && TextUtils.isEmpty(tag)) {
                        pvCustomOptionsArea.show();
                    }
                }


                break;
            case R.id.rl_start:
                if (null != pvTime) {
                    pvTime.show();
                }
                break;
            case R.id.rl_end:
                if (null != pvTime2) {
                    pvTime2.show();
                }
                break;
        }
    }

    private OptionsPickerView pvCustomOptionsPalce, pvCustomOptionsArea;

    /**
     * 仓库编号
     */
    String roomId;

    private void initPlaces(final List<String> str) {

        pvCustomOptionsPalce = new OptionsPickerBuilder(UpAdressActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置

                String tx = str.get(options1);
                tvValueCang.setText(tx);
                roomId = mList.get(options1).getNo();
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

    private String getTime(Date date) {//可根据需要自行截取数据显示

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


    String areaid;
    private ArrayList<String> str = new ArrayList<>();

    private void initAreas(final List<String> str) {

        pvCustomOptionsArea = new OptionsPickerBuilder(UpAdressActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置

                String tx = str.get(options1);
                tvValueQuyu.setText(tx);
                areaid = mListArea.get(options1).getNo();
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
                                pvCustomOptionsArea.returnData();
                                pvCustomOptionsArea.dismiss();
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptionsArea.dismiss();
                            }
                        });

                    }
                })
                .isDialog(false)
                .setOutSideCancelable(false)
                .build();
        pvCustomOptionsArea.setPicker(str);//添加数据
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(222);
    }
}
