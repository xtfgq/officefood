package com.xxx.ency.view.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.xxx.ency.R;
import com.xxx.ency.base.BaseMVPActivity;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.TaskActivityContract;
import com.xxx.ency.di.component.DaggerTaskManagerActivityComponent;
import com.xxx.ency.di.module.TaskManagerActivityModule;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.presenter.TaskActivityPresenter;
import com.xxx.ency.view.login.LoginActivity;
import com.xxx.ency.view.map.UpAdressActivity;
import com.xxx.ency.view.send.SendActivity;
import com.xxx.ency.view.work.SortModel;
import com.xxx.ency.widget.ContainsEmojiEditText;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TaskManagerActivity extends BaseMVPActivity<TaskActivityPresenter> implements TaskActivityContract.View {
    @Inject
    SharePrefManager sharePrefManager;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_rightitle)
    TextView toolbarRightitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_person)
    TextView tvPerson;
    @BindView(R.id.tv_title)
    ContainsEmojiEditText tvTitle;
    @BindView(R.id.tv_address_start)
    TextView tvAddressStart;
    @BindView(R.id.tv_value_start)
    TextView tvValueStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.tv_select_end)
    TextView tvSelectEnd;
    @BindView(R.id.rl_end)
    RelativeLayout rlEnd;
    @BindView(R.id.ed_crash)
    ContainsEmojiEditText edCrash;
    @BindView(R.id.btnlogin)
    Button btnlogin;
    String startDate,endDate;
    TimePickerView pvTime, pvTime2;
    @Override
    protected void initInject() {
        DaggerTaskManagerActivityComponent.builder().
                appComponent(EncyApplication.getAppComponent()).
                taskManagerActivityModule(new TaskManagerActivityModule())
                .build().inject(this);

    }
    List<SortModel> lstBean;
    StringBuilder sb1 = new StringBuilder();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_task_manager;
    }

    @Override
    protected void initialize() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("派发任务");
        Intent intentGet = getIntent();
        lstBean = (List<SortModel>) intentGet.getSerializableExtra("lstBean");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < lstBean.size(); i++) {
            if (i > 0) {
                sb.append(",");
                sb1.append(",");
            }
            sb.append(lstBean.get(i).getName());
            sb1.append(lstBean.get(i).getUserid());
        }
        pvTime = new TimePickerBuilder(TaskManagerActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvValueStart.setText(getTime(date));
                startDate=getTime(date);
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).
                setCancelColor(getResources().getColor(R.color.colorPrimary)).
                setSubmitColor(getResources().getColor(R.color.colorPrimary)).build();
        pvTime2 = new TimePickerBuilder(TaskManagerActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvSelectEnd.setText(getTime(date));
                endDate=getTime(date);

            }
        }).setType(new boolean[]{true, true, true, false, false, false}).
                setCancelColor(getResources().getColor(R.color.colorPrimary)).
                setSubmitColor(getResources().getColor(R.color.colorPrimary)).
                build();
        tvPerson.setText(sb.toString());
    }

    @Override
    public String getUserId() {
        return sharePrefManager.getUserId();
    }

    @Override
    public String getContent() {
        return edCrash.getText().toString();
    }
    private String getTime(Date date) {//可根据需要自行截取数据显示

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    @Override
    public String getSendId() {
        return sb1.toString();
    }

    @Override
    public String getStartDate() {
        return startDate;
    }

    @Override
    public String getEndDate() {
        return endDate;
    }


    @Override
    public String getMessageTitle() {
        return tvTitle.getText().toString().trim();
    }

    @Override
    public void Succuss() {
        showMsg("派发成功");
        EventBus.getDefault().post(100);
        finish();
    }

    @Override
    public void clearData() {
        sharePrefManager.clearData();
        final MyAlertDialog myAlertDialog = new MyAlertDialog(TaskManagerActivity.this).builder()
                .setTitle("提示")
                .setMsg("该账号在其他设备上登录，请重新登录");
        myAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskManagerActivity.this, LoginActivity.class);
                intent.putExtra("tag", "exit");
                startActivity(intent);

            }
        });
        myAlertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnlogin, R.id.rl_start, R.id.rl_end})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_start:
                if (null != pvTime) {
                    hintKeyBoard();
                    pvTime.show();

                }
                break;
            case R.id.rl_end:
                if (null != pvTime2) {
                    hintKeyBoard();
                    pvTime2.show();
                }
                break;
            case R.id.btnlogin:
                if(TextUtils.isEmpty(tvTitle.getText().toString())){
                    showMsg("请填写标题");
                    return;
                }
                if(TextUtils.isEmpty(edCrash.getText().toString())){
                    showMsg("请填写任务内容");
                    return;
                }
                mPresenter.postTask();
                break;
                default:
                    break;
        }
    }
    public void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
