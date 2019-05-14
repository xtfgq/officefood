package com.xxx.ency.view.SecurityCheck;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hb.dialog.dialog.LoadingDialog;
import com.xxx.ency.R;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.util.ButtonUtils;
import com.xxx.ency.view.servicelist.ServiceListActvity;


import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportActivity;

public class SecurityListActivity extends SupportActivity {
    @BindView(R.id.toolbar_rightitle)
    TextView toolbarRightitle;
    @BindView(R.id.work_content)
    FrameLayout workContent;
    private SecurityCheckListFragment workFragment;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    SharePrefManager sharePrefManager;
    String title;
    String step,jobId;
    String formTo;
    String currentStep;
    String roleId;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title=getIntent().getStringExtra("title");
        String type=getIntent().getStringExtra("type");
        String id=getIntent().getStringExtra("id");
        String brow=getIntent().getStringExtra("isBrow");
        step=getIntent().getStringExtra("step");
        jobId=getIntent().getStringExtra("jobId");
        formTo=getIntent().getStringExtra("fromTo");
        currentStep=getIntent().getStringExtra("currentStep");
        roleId=getIntent().getStringExtra("roleId");
        setTitle("");
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(title);
        sharePrefManager=new SharePrefManager(this);
        loadingDialog = new LoadingDialog(this);
        if(brow!=null){
            workFragment =SecurityCheckListFragment.newInstance(type,id,brow,step,jobId,formTo,currentStep,roleId);
        }else {
            if(TextUtils.isEmpty(step)) step="";
            workFragment = SecurityCheckListFragment.newInstance(type, id,"",step,jobId,formTo,currentStep,roleId);

        }
        if(sharePrefManager.getLocalMode()==1&&!TextUtils.isEmpty(step)&&!TextUtils.isEmpty(jobId)
                &&"2".equals(formTo)) {

            toolbarRightitle.setText("保存");
        }
        if(sharePrefManager.getLocalMode()==1&&!TextUtils.isEmpty(step)&&!TextUtils.isEmpty(jobId)
                &&"3".equals(formTo)) {
            toolbarRightitle.setVisibility(View.VISIBLE);
            toolbarRightitle.setText("保存");
        }

        if(sharePrefManager.getLocalMode()==1&&!TextUtils.isEmpty(step)
                &&"5".equals(formTo)) {
            toolbarRightitle.setVisibility(View.VISIBLE);
            toolbarRightitle.setText("保存");
        }
        if(sharePrefManager.getLocalMode()==1&&
                "6".equals(formTo)) {
            toolbarRightitle.setVisibility(View.VISIBLE);
            toolbarRightitle.setText("保存");
        }
        loadRootFragment(R.id.work_content, workFragment);
    }

    @OnClick(R.id.toolbar_rightitle)
    public void onViewClicked() {
        loadingDialog.setMessage("loading");
        loadingDialog.show();
        toolbarTitle.setEnabled(false);
        workFragment.postSave();

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
    public void setSaveGone(){
        toolbarRightitle.setVisibility(View.GONE);
    }
    public void setSaveVisule(){
        toolbarRightitle.setText("保存");
        toolbarRightitle.setVisibility(View.VISIBLE);
    }
    public void setEnable(){
        if(loadingDialog!=null&&loadingDialog.isShowing())
        loadingDialog.dismiss();
        toolbarRightitle.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(222);
    }
}
