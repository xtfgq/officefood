package com.xxx.ency.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.WindowManager;

import com.xxx.ency.config.Constants;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.util.AppActivityTaskManager;

import javax.inject.Inject;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Activity基类
 * Created by xiarh on 2017/9/21.
 */

public abstract class BaseActivity extends SupportActivity {

    protected Activity mContext;

    protected abstract int getLayoutId();

    protected abstract void initialize();

    SharePrefManager sharePrefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mContext = this;
        ButterKnife.bind(this);
        onViewCreated();
        sharePrefManager=new SharePrefManager(this);
        AppActivityTaskManager.getInstance().addActivity(this);

        setTitle("");
        initialize();
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    protected void onViewCreated() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppActivityTaskManager.getInstance().removeActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 回退
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(sharePrefManager.getLocalToken())){
            Constants.token=sharePrefManager.getLocalToken();

        }
    }
}
