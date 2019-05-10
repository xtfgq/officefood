package com.xxx.ency.view.SecurityCheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.xxx.ency.R;
import com.xxx.ency.base.BaseMVPFragment;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.SecurityCheckContract;
import com.xxx.ency.di.component.DaggerSecurityCheckFragmentComponent;
import com.xxx.ency.di.module.SecurityCheckFragmentModule;
import com.xxx.ency.model.bean.SpaceBean;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.presenter.SecurityCheckPresenter;
import com.xxx.ency.util.ButtonUtils;
import com.xxx.ency.view.main.MainActivity;
import com.xxx.ency.view.work.GankFragment;
import com.xxx.ency.view.work.WorkTypeData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SecurityCheckFragment extends BaseMVPFragment<SecurityCheckPresenter> implements
        SecurityCheckContract.View {
    @BindView(R.id.rlseclectplace)
    RelativeLayout rlseclectplace;
    @BindView(R.id.rloutroom)
    RelativeLayout rloutroom;
    @BindView(R.id.rlouthygiene)
    RelativeLayout rlouthygiene;
    Unbinder unbinder;
    @BindView(R.id.tv_place_no)
    TextView tvPlaceNo;
    @BindView(R.id.tv_out_tmp_value)
    TextView tvOutTmpValue;
    @BindView(R.id.tv_out_tmp)
    TextView tvOutTmp;
    @BindView(R.id.tv_out_humidity_value)
    TextView tvOutHumidityValue;
    @BindView(R.id.tv_out_hygiene_value)
    TextView tvOutHygieneValue;
    List<String> shiList = new ArrayList<>();
    List<String> tmpList = new ArrayList<>();
    List<String> buliuList = new ArrayList<>();
    @BindView(R.id.checkboxfixed)
    CheckBox checkboxfixed;
    @BindView(R.id.ll_fixed)
    LinearLayout llFixed;
    @BindView(R.id.ll_inroom)
    LinearLayout llInroom;

    @BindView(R.id.checkboxinroom)
    CheckBox checkboxinroom;
    @Inject
    SharePrefManager sharePrefManager;
    private OptionsPickerView pvCustomOptionsPalce, pvCustomOptionsTemp,
            pvCustomOptionsHumidity, pvCustomOptionsIsYes;
    private List<SpaceBean> mList=new ArrayList<>();

    @Override
    protected void initInject() {
        DaggerSecurityCheckFragmentComponent.builder()
                .appComponent(EncyApplication.getAppComponent())
                .securityCheckFragmentModule(new SecurityCheckFragmentModule())
                .build()
                .inject(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_securitycheck;
    }
    String type;

    @Override
    protected void initialize() {
        Bundle args = getArguments();
        if (args != null) {
            type = args.getString("type");

        }
        mPresenter.getData();
        if("3".equals(type)) {
            tvOutTmp.setText("作业类型");
            tvOutTmpValue.setText("请选择作业类型");
            rlseclectplace.setVisibility(View.GONE);
        }
        initTemp();
        initShi();
        initBuliu();
        checkboxfixed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkboxinroom.setChecked(false);
                }else {
                    checkboxinroom.setChecked(true);
                }

            }
        });
        checkboxinroom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkboxfixed.setChecked(false);
                }else {
                    checkboxfixed.setChecked(true);
                }

            }
        });
    }

    @Override
    public void showSpaceBean(List<SpaceBean> results) {
        if(results!=null&&results.size()>0) {
            mList.addAll(results);
            List<String> str = new ArrayList<>();
            for (SpaceBean bean : results) {
                str.add(bean.getName());
            }
            initPlaces(str);
        }

    }

    @Override
    public void failGetData() {

    }

    @Override
    public String getUserid() {
        return sharePrefManager.getUserId();
    }

    @Override
    public void postSuccuss() {

    }

    @Override
    public void clearData() {
        ((MainActivity)getActivity()).clearData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rlseclectplace, R.id.rloutroom, R.id.rloutroomhumidity, R.id.rlouthygiene,
            R.id.btnnext,R.id.ll_fixed,R.id.ll_inroom})
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
            case R.id.rloutroomhumidity:
                if (pvCustomOptionsHumidity != null) {
                    pvCustomOptionsHumidity.show();
                }
                break;
            case R.id.rlouthygiene:
                if (pvCustomOptionsIsYes != null) {
                    pvCustomOptionsIsYes.show();
                }
                break;
            case R.id.btnnext:
                if (!ButtonUtils.isFastDoubleClick(R.id.btnnext)) {
                    //写你相关操作即可
                    if("1".equals(type)) {
                        if (tvPlaceNo.getText().toString().contains("请选择")) {
                            showTop("请选择仓号");
                            return;
                        }
                        if (tvOutTmpValue.getText().toString().contains("请选择")) {
                            showTop("请选择检查类型");
                            return;
                        }

                        ((MainActivity) getActivity()).showSecurityList();
                    }else if("2".equals(type)){
                        if (tvPlaceNo.getText().toString().contains("请选择")) {
                            showTop("请选择仓号");
                            return;
                        }
                        if (tvOutTmpValue.getText().toString().contains("请选择")) {
                            showTop("请选择检查类型");
                            return;
                        }
                        ((MainActivity) getActivity()).showSecurityBrowList();

                    }else {
                        if (tvOutTmpValue.getText().toString().contains("请选择")) {
                            showTop("请选择作业类型");
                            return;
                        }
                        ((MainActivity) getActivity()).showWork(tvOutTmpValue.getText().toString(),
                                getTypeWork(tvOutTmpValue.getText().toString())+"");
                    }

                }

                break;

        }
    }


    private void initPlaces(final List<String> str) {

        pvCustomOptionsPalce = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置

                String tx = str.get(options1);
                tvPlaceNo.setText(tx);
                ((MainActivity)getActivity()).setId(mList.get(options1).getNo());
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


            if ("3".equals(type)) {
                Iterator entries = EncyApplication.getInstance().workMap.entrySet().iterator();
                while(entries.hasNext()){
                    Map.Entry entry = (Map.Entry)entries.next();
                    WorkTypeData workTypeData=(WorkTypeData)entry.getValue();
                    tmpList.add(workTypeData.getName());
                }


            }else {
                tmpList.add("定项检查");
                tmpList.add("入仓检查");
            }



        pvCustomOptionsTemp = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = tmpList.get(options1);
                tvOutTmpValue.setText(tx);
                if ("3".equals(type)) {
                    ((MainActivity) getActivity()).setCheckType(getTypeWork(tx));
                }else {
                    ((MainActivity) getActivity()).setCheckType(options1 + 1);
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
                .isDialog(true)
                .setSelectOptions(36)
                .setOutSideCancelable(false)
                .build();
        pvCustomOptionsTemp.setPicker(tmpList);//添加数据
    }
    private int getTypeWork(String name){
        int code=0;
        Iterator entries = EncyApplication.getInstance().workMap.entrySet().iterator();
        while(entries.hasNext()){
            Map.Entry entry = (Map.Entry)entries.next();
            WorkTypeData workTypeData=(WorkTypeData)entry.getValue();
            if(name.equals(workTypeData.getName())) {
                code=Integer.valueOf(workTypeData.getCode());

            }
        }

        return code;

    }

    private void initShi() {

        for (int i = 0; i < 100; i++) {
            shiList.add(i + "");
        }
        pvCustomOptionsHumidity = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = shiList.get(options1);
                tvOutHumidityValue.setText(tx);
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
                                pvCustomOptionsHumidity.returnData();
                                pvCustomOptionsHumidity.dismiss();
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptionsHumidity.dismiss();
                            }
                        });

                    }
                })
                .isDialog(false)
                .setSelectOptions(50)
                .setOutSideCancelable(false)
                .build();
        pvCustomOptionsHumidity.setPicker(shiList);
    }

    private void initBuliu() {
        buliuList.add("是");
        buliuList.add("否");
        pvCustomOptionsIsYes = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = buliuList.get(options1);
                tvOutHygieneValue.setText(tx);
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
                                pvCustomOptionsIsYes.returnData();
                                pvCustomOptionsIsYes.dismiss();
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptionsIsYes.dismiss();
                            }
                        });

                    }
                })
                .isDialog(false)
                .setOutSideCancelable(false)
                .build();
        pvCustomOptionsIsYes.setPicker(buliuList);
    }
    public static SecurityCheckFragment newInstance(String type) {
        SecurityCheckFragment gankFragment = new SecurityCheckFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);

        gankFragment.setArguments(bundle);
        return gankFragment;
    }

}
