package com.xxx.ency.view.splash;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;


import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.cache.CacheManager;
import com.xxx.ency.R;
import com.xxx.ency.config.Constants;
import com.xxx.ency.view.main.MainActivity;



import me.yokeyword.fragmentation.SupportActivity;

/**
 * 启动页
 * Created by xiarh on 2017/12/27.
 */

public class SplashActivity extends SupportActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initXgConfig();
        // 判断是否从推送通知栏打开的
        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        if (click != null) {
            //从推送通知栏打开-Service打开Activity会重新执行Laucher流程
            //查看是不是全新打开的面板
            if (isTaskRoot()) {
                return;
            }
            //如果有面板存在则关闭当前的面板
            finish();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,
                        MainActivity.class));
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, 3000);
    }
    /**
     * 腾讯信鸽初始化配置
     */
    private void initXgConfig() {
        // 开启logcat输出，方便debug，发布时请关闭

        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(),
        // XGIOperateCallback)带callback版本
        // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext

        XGPushConfig.enableOtherPush(getApplicationContext(), true);
        XGPushConfig.enableDebug(this,true);
        XGPushConfig.setHuaweiDebug(true);
        XGPushManager.registerPush(getApplicationContext(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                Log.w(com.tencent.android.tpush.common.Constants.LogTag, "注册信鸽推送成功token:" + data);
                // Toast.makeText(AtyWelcome.this, "注册信鸽推送成功token:"+data,
                // Toast.LENGTH_LONG).show();
                Constants.XINGE=data.toString();
                CacheManager.getRegisterInfo(getApplicationContext());

            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.w(com.tencent.android.tpush.common.Constants.LogTag, "注册信鸽推送失败token:" + data
                        + ", errCode:" + errCode + ",msg:" + msg);
                // Toast.makeText(AtyWelcome.this, "注册信鸽推送失败token:" + data +
                // ", errCode:" + errCode + ",msg:" + msg,
                // Toast.LENGTH_LONG).show();
            }
        });
        // 2.36（不包括）之前的版本需要调用以下2行代码
        // Intent service = new Intent(getApplicationContext(),
        // XGPushService.class);
        // getApplicationContext().startService(service);
    }
}
