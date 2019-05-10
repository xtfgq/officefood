package com.xxx.ency.view.message;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.dialog.myDialog.MyAlertDialog;
import com.xxx.ency.R;
import com.xxx.ency.base.BaseMVPActivity;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.MessageContract;
import com.xxx.ency.di.component.DaggerMessageActivityComponent;
import com.xxx.ency.di.module.MessageActivityModule;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.presenter.MessagePresenter;
import com.xxx.ency.view.login.LoginActivity;
import com.xxx.ency.widget.ContainsEmojiEditText;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MessageActivity extends BaseMVPActivity<MessagePresenter> implements
        MessageContract.View {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.ll_task)
    LinearLayout llTask;
    @BindView(R.id.btnpost)
    Button btnpost;
    @BindView(R.id.toolbar_rightitle)
    TextView toolbarRightitle;
    @BindView(R.id.ed_demo)
    ContainsEmojiEditText edDemo;

    private String title, id;
    @Inject
    SharePrefManager sharePrefManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    String type, fromTo;
    int status;

    @Override
    protected void initialize() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("详情");
        title = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        fromTo = getIntent().getStringExtra("fromTo");
        status = getIntent().getIntExtra("status", -1);
        tvContent.setText(title);
        if ("0".equals(type)) {
            mPresenter.readMessage();
        }
        if (!TextUtils.isEmpty(fromTo) && "2".equals(type) && status == 1) {

            llTask.setVisibility(View.VISIBLE);
            btnpost.setVisibility(View.VISIBLE);
            edDemo.setText("已完成"+title);
        }
    }

    @Override
    public void clearData() {
        sharePrefManager.clearData();
        final MyAlertDialog myAlertDialog = new MyAlertDialog(MessageActivity.this).builder()
                .setTitle("提示")
                .setMsg("该账号在其他设备上登录，请重新登录");
        myAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this, LoginActivity.class);
                intent.putExtra("tag", "exit");
                startActivity(intent);


            }
        });
        myAlertDialog.show();
    }

    @Override
    public String getContent() {
        return edDemo.getText().toString();
    }

    @Override
    public void goFinish() {
        showMsg("成功");
        EventBus.getDefault().post(100);
        new Handler().postDelayed(new Runnable(){
            public void run() {
                finish();
            }
        }, 2000);

    }


    @Override
    protected void initInject() {
        DaggerMessageActivityComponent.builder().
                appComponent(EncyApplication.getAppComponent())
                .messageActivityModule(new MessageActivityModule()).build().
                inject(this);

    }

    @Override
    public void showSuccuss() {
        EventBus.getDefault().post(100);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(222);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnpost)
    public void onViewClicked() {
        if(TextUtils.isEmpty(edDemo.getText().toString())){
            showMsg("请填写任务完成情况");
            return;
        }
        mPresenter.finishTask();

    }
}
