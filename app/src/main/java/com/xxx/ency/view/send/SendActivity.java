package com.xxx.ency.view.send;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hb.dialog.myDialog.MyAlertDialog;
import com.xxx.ency.R;
import com.xxx.ency.base.BaseMVPActivity;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.SendActivityContract;
import com.xxx.ency.di.component.DaggerSendActivityComponent;
import com.xxx.ency.di.module.SendActivityModule;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.presenter.SendActivityPresenter;
import com.xxx.ency.view.login.LoginActivity;
import com.xxx.ency.view.work.AreaActivity;
import com.xxx.ency.view.work.SortModel;
import com.xxx.ency.widget.ContainsEmojiEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SendActivity extends BaseMVPActivity<SendActivityPresenter> implements SendActivityContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.btnlogin)
    Button btnlogin;
    @BindView(R.id.ed_crash)
    ContainsEmojiEditText edCrash;
    @Inject
    SharePrefManager sharePrefManager;
    @BindView(R.id.tv_person)
    TextView tvPerson;
    @BindView(R.id.tv_title)
    ContainsEmojiEditText tvTitle;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_send;
    }

    List<SortModel> lstBean;
    StringBuilder sb1 = new StringBuilder();

    @Override
    protected void initialize() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("发送消息");
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
        tvPerson.setText(sb.toString());

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnlogin)
    public void onViewClicked() {
        if(TextUtils.isEmpty(tvTitle.getText().toString())){
            showMsg("请填写标题");
            return;
        }
        if(TextUtils.isEmpty(edCrash.getText().toString())){
            showMsg("请填写内容");

            return;
        }
       mPresenter.postMessage();

    }


    @Override
    protected void initInject() {
        DaggerSendActivityComponent.builder().appComponent(EncyApplication.getAppComponent())
                .sendActivityModule(new SendActivityModule()).build()
                .inject(this);
    }

    @Override
    public String getUserId() {
        return sharePrefManager.getUserId();
    }

    @Override
    public String getContent() {
        return edCrash.getText().toString();
    }

    @Override
    public String getSendUserId() {
        return sb1.toString();
    }

    @Override
    public String getMessageTitle() {
        return tvTitle.getText().toString();
    }

    @Override
    public void Succuss() {
        showMsg("发送成功");
        EventBus.getDefault().post(100);
        new Handler().postDelayed(new Runnable(){
            public void run() {
                finish();
            }
        }, 2000);

    }

    @Override
    public void clearData() {
        sharePrefManager.clearData();
        final MyAlertDialog myAlertDialog = new MyAlertDialog(SendActivity.this).builder()
                .setTitle("提示")
                .setMsg("该账号在其他设备上登录，请重新登录");
        myAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendActivity.this, LoginActivity.class);
                intent.putExtra("tag", "exit");
                startActivity(intent);

            }
        });
        myAlertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(222);
    }
}
