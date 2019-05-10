package com.xxx.ency.view.move;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.dialog.myDialog.MyAlertDialog;
import com.xxx.ency.R;
import com.xxx.ency.base.BaseMVPActivity;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.MoveContract;
import com.xxx.ency.di.component.DaggerMoveActivityComponent;
import com.xxx.ency.di.module.MoveActivityModule;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.presenter.MovePresenter;
import com.xxx.ency.util.DateUtil;
import com.xxx.ency.view.login.LoginActivity;
import com.xxx.ency.widget.ContainsEmojiEditText;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MoveActivity extends BaseMVPActivity<MovePresenter> implements MoveContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Inject
    SharePrefManager sharePrefManager;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    String id;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_property)
    TextView tvProperty;
    @BindView(R.id.tv_chuliang)
    TextView tvChuliang;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.tv_crash)
    TextView tvCrash;
    @BindView(R.id.checkboxcrash)
    CheckBox checkcrash;
    @BindView(R.id.checknocrash)
    CheckBox checkboxnocrash;
    @BindView(R.id.btnlogin)
    Button btnlogin;
    @BindView(R.id.ed_crash)
    ContainsEmojiEditText edCrash;
    String tag = "";
    @BindView(R.id.tv_person)
    TextView tvPerson;
    @BindView(R.id.tag_1)
    LinearLayout tag1;
    @BindView(R.id.tag_2)
    LinearLayout tag2;

    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_has_crash)
    TextView tvHasCrash;
    @BindView(R.id.tag_3)
    LinearLayout tag3;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_move;
    }

    String fromTo;

    @Override
    protected void initialize() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("远程巡仓");
        id = getIntent().getStringExtra("id");

        tag = getIntent().getStringExtra("tag");
        fromTo = getIntent().getStringExtra("fromTo");
        if ("1".equals(tag)&&TextUtils.isEmpty(fromTo)) {
            tag1.setVisibility(View.VISIBLE);
            btnlogin.setVisibility(View.VISIBLE);
            toolbarTitle.setText("远程巡仓");
            mPresenter.move();
        }
        if ("2".equals(tag)&&TextUtils.isEmpty(fromTo)) {

            tag2.setVisibility(View.VISIBLE);
            tag3.setVisibility(View.VISIBLE);
            toolbarTitle.setText("现场巡仓");
            btnlogin.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(fromTo) && "2".equals(tag)) {
            tag2.setVisibility(View.VISIBLE);
            tag3.setVisibility(View.GONE);
            tvHasCrash.setVisibility(View.VISIBLE);
            tvHasCrash.setText("巡查区域:"+getIntent().getStringExtra("area"));
            toolbarTitle.setText("现场巡仓");
            edCrash.setVisibility(View.VISIBLE);
            edCrash.setBackground(null);
            edCrash.setText(getIntent().getStringExtra("des"));
            edCrash.setEnabled(false);
            btnlogin.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(fromTo) && "1".equals(tag)) {
            tag1.setVisibility(View.VISIBLE);
            toolbarTitle.setText("远程巡仓");
            btnlogin.setVisibility(View.GONE);
            mPresenter.move();
        }

        checkcrash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkboxnocrash.setChecked(false);
                } else {
                    checkboxnocrash.setChecked(true);
                }

            }
        });
        checkboxnocrash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkcrash.setChecked(false);
                    edCrash.setVisibility(View.GONE);
                } else {
                    checkcrash.setChecked(true);
                    edCrash.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    @Override
    protected void initInject() {
        DaggerMoveActivityComponent.builder().
                appComponent(EncyApplication.getAppComponent()).
                moveActivityModule(new MoveActivityModule()).build()
                .inject(this);
    }

    @Override
    public void clearData() {
        sharePrefManager.clearData();
        final MyAlertDialog myAlertDialog = new MyAlertDialog(MoveActivity.this).builder()
                .setTitle("提示")
                .setMsg("该账号在其他设备上登录，请重新登录");
        myAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoveActivity.this, LoginActivity.class);
                intent.putExtra("tag", "exit");
                startActivity(intent);


            }
        });
        myAlertDialog.show();
    }

    @Override
    public String getRoomId() {
        return id;
    }

    int normal;

    @Override
    public void showSuccuss(String str) {
        if (!TextUtils.isEmpty(str)) {


            try {
                JSONObject jsonObject = new JSONObject(str);
                tvNum.setText("储粮仓号：" + jsonObject.getString("name"));
                tvYear.setText("入库年限：" + jsonObject.getString("warehousingYear"));
                tvProperty.setText("储粮性质：" + jsonObject.getString("property"));
                tvChuliang.setText("科技储粮：" + jsonObject.getString("technologyGrainStorageTxt"));
                tvDesc.setText("储粮情况：" + jsonObject.getString("grainStorageSituationTxt"));
                if (!TextUtils.isEmpty(jsonObject.getString("userId"))) {
                    tvPerson.setText("保管员：" + EncyApplication.getInstance().userMap.get(jsonObject.getString("userId")));
                } else {
                    tvPerson.setText("保管员：");
                }
                if (!TextUtils.isEmpty(jsonObject.getString("createDate"))) {
                    tvTime.setText("最近入仓检查时间：" + DateUtil.StringPattern(jsonObject.getString("createDate"),
                            "yyyy-MM-dd", "yyyy年MM月dd日"));
                } else {
                    tvTime.setText("最近入仓检查时间：");
                }
                normal = Integer.valueOf(jsonObject.getString("isAbnormal"));
                if (normal == 0) {
                    tvCrash.setText("有无异常:无");
                } else {
                    tvCrash.setText("有无异常:" + jsonObject.getString("abnormalDesc"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public String getContent() {
        if ("1".equals(tag)) {
            return tvDesc.getText().toString();
        } else {
            return normal == 0 ? "" : edCrash.getText().toString();

        }
    }

    @Override
    public int getNormal() {
        return normal;
    }

    @Override
    public String getUserId() {
        return sharePrefManager.getUserId();
    }

    @Override
    public String getType() {
        if ("1".equals(tag)) {
            return "1";
        } else {
            return "2";

        }
    }

    @Override
    public void addSucuss() {
        EventBus.getDefault().post(100);
        showMsg("提交成功");
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnlogin)
    public void onViewClicked() {
        if ("1".equals(tag)) {
            mPresenter.postSave();
        } else {
            normal = checkboxnocrash.isChecked() ? 0 : 1;
            mPresenter.postSave();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(222);
    }
}
