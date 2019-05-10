package com.xxx.ency.view.work;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.xxx.ency.R;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.view.main.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportActivity;

public class WorkActivity extends SupportActivity {
    @BindView(R.id.toolbar_rightitle)
    TextView toolbarRightitle;
    @BindView(R.id.work_content)
    FrameLayout workContent;
    private GankMainFragment workFragment;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Inject
    SharePrefManager sharePrefManager;
    String title;
    String type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = getIntent().getStringExtra("title");
        String brow=getIntent().getStringExtra("isBrow");
        type=getIntent().getStringExtra("type");
        String id=getIntent().getStringExtra("id");
        setTitle("");
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(title);
        sharePrefManager=EncyApplication.getAppComponent().getSharePrefManager();
        if(TextUtils.isEmpty(brow)) {
            if(title.contains("消息")) {
                toolbarRightitle.setVisibility(View.VISIBLE);
                toolbarRightitle.setText("+");
                toolbarRightitle.setTextSize(32);
            }else {

                    if (title.contains("作业")&&sharePrefManager.getLocalMode() == 1
                            || title.contains("扦样送检")&&sharePrefManager.getLocalMode() == 1) {
                        toolbarRightitle.setVisibility(View.VISIBLE);
                        toolbarRightitle.setText("+");
                        toolbarRightitle.setTextSize(32);
                    }else if(title.contains("委托")){

                        toolbarRightitle.setVisibility(View.VISIBLE);
                        toolbarRightitle.setText("+");
                        toolbarRightitle.setTextSize(32);
                    }else if(title.contains("任务")){
                        if(sharePrefManager.getLocalMode()>=4) {
                            toolbarRightitle.setVisibility(View.VISIBLE);
                            toolbarRightitle.setText("+");
                            toolbarRightitle.setTextSize(32);
                        }
                }

            }
            workFragment =GankMainFragment.newInstance(title,type,"");
        }else {
            workFragment =GankMainFragment.newInstance(title,type,id);
        }
        loadRootFragment(R.id.work_content, workFragment);
        EventBus.getDefault().register(this);
    }

    @OnClick(R.id.toolbar_rightitle)
    public void onViewClicked() {
        if(title.contains("委托")) {
            Intent intent2 = new Intent(WorkActivity.this, WeituoActivity.class);
            intent2.putExtra("title", "添加委托");
            startActivity(intent2);
        }else  if(title.contains("作业")){
            Intent intent2 = new Intent(WorkActivity.this, WeituoActivity.class);
            intent2.putExtra("title", "作业申请");
            intent2.putExtra("roleId", "5");
            intent2.putExtra("type", type);
            startActivity(intent2);
        }else if(title.contains("扦样")){
            Intent intent2 = new Intent(WorkActivity.this, WeituoActivity.class);
            intent2.putExtra("title", "扦样送检");
            intent2.putExtra("roleId", "2");
            startActivity(intent2);
        }
        else  if(title.contains("任务")){
            Intent intent2 = new Intent(WorkActivity.this, WeituoActivity.class);
            intent2.putExtra("title", "任务提醒");
            startActivity(intent2);
        }else {
            Intent intent2 = new Intent(WorkActivity.this, WeituoActivity.class);
            intent2.putExtra("title", "收件人");
            startActivity(intent2);
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(222);
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void getEventBus(Integer num) {
        if (num != null&&num==100) {
            workFragment.refurhFragment();
        }
    }

}
