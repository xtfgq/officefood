package com.xxx.ency.view.login;


import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hb.dialog.myDialog.MyAlertDialog;
import com.xxx.ency.R;
import com.xxx.ency.base.BaseMVPActivity;
import com.xxx.ency.config.Constants;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.LoginContract;
import com.xxx.ency.di.component.DaggerLoginActivityComponent;
import com.xxx.ency.di.module.LoginActivityModule;
import com.xxx.ency.model.bean.LoginBean;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.presenter.LoginPresenter;

import com.xxx.ency.util.AppActivityTaskManager;
import com.xxx.ency.view.main.MainActivity;
import com.xxx.ency.view.splash.SplashActivity;
import com.xxx.ency.widget.ContainsEmojiEditText;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseMVPActivity<LoginPresenter> implements
        LoginContract.View {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.btnlogin)
    Button btnlogin;
    @BindView(R.id.editPhone)
    ContainsEmojiEditText editPhone;
    @Inject
    SharePrefManager sharePrefManager;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.editPass)
    EditText editPass;
    String tag="";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initialize() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("登  录");
        tag=getIntent().getStringExtra("tag");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                goBack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 做关闭逻辑处理
     */
    private void goBack() {
        if(TextUtils.isEmpty(tag)){
            finish();
        }else {
            MyAlertDialog myAlertDialog = new MyAlertDialog(LoginActivity.this).builder()
                .setTitle("提示")
                .setMsg("确定要退出应用吗")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppActivityTaskManager.getInstance().removeAllActivity();
                        //创建ACTION_MAIN
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //启动ACTION_MAIN，直接回到桌面
                        startActivity(intent);
                        android.os.Process.killProcess(android.os.Process.myPid());


                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            myAlertDialog.show();



        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnlogin)
    public void onViewClicked() {
        goLogin();
    }

    private void goLogin() {
        if (TextUtils.isEmpty(editPhone.getText().toString().trim())) {
            showTop("账号不能为空");

            return;
        }

        mPresenter.login(sharePrefManager.getDeviceId());

    }


    @Override
    protected void initInject() {
        DaggerLoginActivityComponent.
                builder().
                appComponent(EncyApplication.getAppComponent())
                .loginActivityModule(new LoginActivityModule()).build()
                .inject(this);

    }

    @Override
    public void showLoginBean(LoginBean loginBean) {
        if (loginBean != null) {
            sharePrefManager.setLocalMode(loginBean.getModel());
            sharePrefManager.setLoginToken(loginBean.getToken());
            sharePrefManager.setUser(loginBean.getUsername());
            sharePrefManager.setUseId(loginBean.getUserid());
            sharePrefManager.setLoginToken(loginBean.getToken());
            Constants.token=sharePrefManager.getLocalToken();
            mPresenter.getRoomData();

        }

    }

    @Override
    public String getUserName() {
        return editPhone.getText().toString().trim();
    }

    @Override
    public String getPwd() {
        return editPass.getText().toString().trim();
    }

    @Override
    public String getUserid() {
        return sharePrefManager.getUserId();
    }

    @Override
    public void showMsg(String msg) {
        MyAlertDialog myAlertDialog = new MyAlertDialog(LoginActivity.this).builder()
                .setTitle("提示")
                .setMsg(msg)
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
        myAlertDialog.show();
    }

    @Override
    public void showSucussFinsh() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(222);
    }
}
