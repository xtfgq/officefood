package com.xxx.ency.view.servicelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xxx.ency.R;
import com.xxx.ency.view.work.AreaActivity;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportActivity;

public class ServiceListActvity extends SupportActivity {
    @BindView(R.id.toolbar_rightitle)
    TextView toolbarRightitle;
    @BindView(R.id.work_content)
    FrameLayout workContent;
    private ServiceListFragment serviceListFragment;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        title=getIntent().getStringExtra("title");
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(title);
        if(title.contains("安全")||title.contains("巡仓")){
            toolbarRightitle.setVisibility(View.VISIBLE);
            toolbarRightitle.setText("+");
            toolbarRightitle.setTextSize(32);
        }
        serviceListFragment = ServiceListFragment.newInstance(title);
        loadRootFragment(R.id.work_content, serviceListFragment);
        EventBus.getDefault().register(this);
    }

    @OnClick(R.id.toolbar_rightitle)
    public void onViewClicked() {
        if(title.contains("安全")){
            startActivity(new Intent(ServiceListActvity.this, AreaActivity.class));
        }else if(title.contains("巡仓")){
            Intent intent=new Intent(ServiceListActvity.this, AreaActivity.class);
            intent.putExtra("tag","move");
            startActivity(intent);
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
    @Subscribe
    public void getEventBus(Integer num) {
        if (num != null&&num==100) {
            serviceListFragment.onRefresh();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(222);
        EventBus.getDefault().unregister(this);
    }
}
