package com.xxx.ency.config;

import android.app.Application;
import android.content.Context;

import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;


import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.xxx.ency.BuildConfig;
import com.xxx.ency.di.component.AppComponent;
import com.xxx.ency.di.component.DaggerAppComponent;
import com.xxx.ency.di.module.ApplicationModule;
import com.xxx.ency.di.module.HttpModule;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.util.AppApplicationUtil;
import com.xxx.ency.util.LogUtil;
import com.xxx.ency.view.work.WorkTypeData;

import java.util.HashMap;
import java.util.Map;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

/**
 * Created by xiarh on 2017/9/20.
 */

public class EncyApplication<attachBaseContext> extends MultiDexApplication {

    private static EncyApplication instance;

    public static AppComponent appComponent;

    public static synchronized EncyApplication getInstance() {
        return instance;
    }
    public static String Latitude,Longitude;
    public Map<String,String> userMap=new HashMap<>();
    public Map<String,String> roomMap =new HashMap<>();
    public Map<String,String> areaMap=new HashMap<>();
    public Map<Integer,WorkTypeData> workMap=new HashMap<>();
    public  String name[] = {"李连杰","赵普","赵丽颖","胡景涛","刘潘坤","张三"};
    public  String scanCode[] = {"α","β","γ","δ"};

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appComponent = DaggerAppComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .httpModule(new HttpModule())
                .build();
        SharePrefManager sharePrefManager = appComponent.getSharePrefManager();
        boolean nightMode = sharePrefManager.getNightMode();
        AppCompatDelegate.setDefaultNightMode(nightMode ? AppCompatDelegate.MODE_NIGHT_YES :
                AppCompatDelegate.MODE_NIGHT_NO);
        sharePrefManager.setLocalProvincialTrafficPatterns(sharePrefManager.getProvincialTrafficPattern());
        // 初始化Bugly
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppVersion(String.valueOf(AppApplicationUtil.getVersionCode(getApplicationContext())));
        CrashReport.initCrashReport(getApplicationContext(), Constants.BUGLY_APP_ID, false); // debug版本设置为true，正式发布设置为false
        configTinker();
        // 初始化Fragmentation
        Fragmentation.builder()
                // 设置 栈视图 模式为 悬浮球模式   SHAKE: 摇一摇唤出  默认NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.BUBBLE)
                // 开发环境：true时，遇到异常："Can not perform this action after onSaveInstanceState!"时，抛出，并Crash;
                // 生产环境：false时，不抛出，不会Crash，会捕获，可以在handleException()里监听到
                .debug(false)
                // 实际场景建议.debug(BuildConfig.DEBUG)
                // 生产环境时，捕获上述异常（避免crash），会捕获
                // 建议在回调处上传下面异常到崩溃监控服务器
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        CrashReport.postCatchedException(e);  // bugly会将这个Exception上报
                    }
                })
                .install();

        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtil.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
        // 异常处理类
        if (!BuildConfig.DEBUG) {
            EncyCrashHandler.getInstance().setCrashHanler(this);
        }

    }



    public static AppComponent getAppComponent() {
        return appComponent;
    }

    private void configTinker() {

        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁，默认为true
        Beta.canAutoDownloadPatch = true;
        // 设置是否自动合成补丁，默认为true
        Beta.canAutoPatch = true;
        // 设置是否提示用户重启，默认为false
        Beta.canNotifyUserRestart = false;
        // 补丁回调接口
        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFile) {

            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {

            }

            @Override
            public void onDownloadSuccess(String msg) {

            }

            @Override
            public void onDownloadFailure(String msg) {

            }

            @Override
            public void onApplySuccess(String msg) {

            }

            @Override
            public void onApplyFailure(String msg) {

            }

            @Override
            public void onPatchRollback() {

            }
        };

        // 多渠道需求塞入
        // String channel = WalleChannelReader.getChannel(getApplication());
        // Bugly.setAppChannel(getApplication(), channel);
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        Bugly.init(this, Constants.BUGLY_APP_ID, true);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 安装tinker
        // 此接口仅用于反射Application方式接入。
        Beta.installTinker();
    }

}