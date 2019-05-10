package com.xxx.ency.view.login;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xxx.ency.R;
import com.xxx.ency.base.BaseMVPActivity;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.ChangePasswordContract;
import com.xxx.ency.di.component.DaggerChangePasswordActivityComponent;
import com.xxx.ency.di.module.ChangePaaswordActivityModule;
import com.xxx.ency.model.bean.LoginBean;
import com.xxx.ency.model.bean.Sms;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.presenter.ChangePasswordPresenter;
import com.xxx.ency.util.PhoneUtils;
import com.xxx.ency.widget.ContainsEmojiEditText;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChangePasswrodActivity extends BaseMVPActivity<ChangePasswordPresenter> implements
        ChangePasswordContract.View {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_getCode)
    TextView tvGetCode;
    @BindView(R.id.btn_update)
    Button btnUpdate;
    @BindView(R.id.edit_phone)
    EditText editPhone;
    @BindView(R.id.edt_sms)
    ContainsEmojiEditText edtSms;
    @BindView(R.id.edt_new_Pass_again)
    ContainsEmojiEditText edPass;
    @Inject
    SharePrefManager sharePrefManager;
    private TimeCount time;

    @Override
    protected void initInject() {
        DaggerChangePasswordActivityComponent.builder().
                appComponent(EncyApplication.getAppComponent()).
              changePaaswordActivityModule(new ChangePaaswordActivityModule())
                .build().inject(this);

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
    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void initialize() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("重置密码");
        time = new TimeCount(60000, 1000);
    }



    @Override
    public void sendSmsBean() {
        time.start();
    }

    @Override
    public void changSuccuss() {
        finish();
    }

    @Override
    public String getPhone() {
        return editPhone.getText().toString();
    }

    @Override
    public String getCode() {
        return edtSms.getText().toString();
    }

    @Override
    public String getPwd() {
        return edPass.getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_getCode, R.id.btn_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_getCode:
                if (!PhoneUtils.isMobileNO(editPhone.getText().toString().trim())) {
                   showTop("手机格式不正确");
                  return;
                }
                mPresenter.sendSms();
                break;
            case R.id.btn_update:
                if (!PhoneUtils.isMobileNO(editPhone.getText().toString().trim())) {
                    showTop("手机格式不正确");
                    return;
                }
                if (TextUtils.isEmpty(edtSms.getText().toString().trim())) {
                    showTop("请输入短信验证码");
                    return;
                }
                if (TextUtils.isEmpty(edPass.getText().toString().trim())) {
                    showTop("请输入密码");
                    return;
                }
                mPresenter.changePwd();
                break;
        }
    }
    /**
     * 做关闭逻辑处理
     */
    private void goBack() {

        finish();
    }
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvGetCode.setClickable(false);
            tvGetCode.setText("("+millisUntilFinished / 1000 +") 秒后可重新发送");
        }

        @Override
        public void onFinish() {
            tvGetCode.setText("重新获取验证码");
            tvGetCode.setClickable(true);
            tvGetCode.setBackgroundColor(Color.parseColor("#4EB84A"));

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(222);
        if(time!=null){
            time.cancel();
            time=null;
        }
    }
}