package com.xxx.ency.view.work;



import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;

import com.hb.dialog.myDialog.MyAlertDialog;
import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.xxx.ency.R;
import com.xxx.ency.base.BaseMVPActivity;
import com.xxx.ency.config.EncyApplication;

import com.xxx.ency.contract.WeituoContract;
import com.xxx.ency.di.component.DaggerWeituoActivityModuleComponent;
import com.xxx.ency.di.module.WeituoActivityModule;
import com.xxx.ency.model.bean.LoginBean;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.presenter.WeituoPresenter;
import com.xxx.ency.view.login.LoginActivity;
import com.xxx.ency.view.main.MainActivity;
import com.xxx.ency.view.map.UpAdressActivity;
import com.xxx.ency.view.send.SendActivity;
import com.xxx.ency.view.task.TaskManagerActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class WeituoActivity extends BaseMVPActivity<WeituoPresenter> implements WeituoContract.View{
    private RecyclerView mRecyclerView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    LinearLayoutManager manager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    private List<SortModel> SourceDateList;
    @Inject
    SharePrefManager sharePrefManager;
    String title,roleId;
    @BindView(R.id.toolbar_rightitle)
    TextView toolbarRightitle;
    String type;
    /**
     * 对应多种登记表的
     */
    int multipleType;
    String id;

    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private OptionsPickerView pvCustomOptionsPalce;

    @Override
    protected void initInject() {
        DaggerWeituoActivityModuleComponent.builder()
                .appComponent(EncyApplication.getAppComponent())
                .weituoActivityModule(new WeituoActivityModule()).build()
                .inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_weituo_manager;
    }

    @Override
    protected void initialize() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        toolbarTitle.setVisibility(View.VISIBLE);
        title=getIntent().getStringExtra("title");
        type=getIntent().getStringExtra("type");
        roleId=getIntent().getStringExtra("roleId");
        id=getIntent().getStringExtra("id");
        multipleType=getIntent().getIntExtra("multipleType",-1);
        if(TextUtils.isEmpty(title)) title="用户列表";
        toolbarTitle.setText(title);
        pinyinComparator = new PinyinComparator();
        sideBar = (SideBar) findViewById(R.id.sideBar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        //设置右侧SideBar触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }

            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        if(title.contains("委托")) {
            List<String> str = new ArrayList<>();
            str.add("区域巡查委托");
            str.add("仓储保管委托");
            initPlaces(str);
        }else if(title.contains("收件人")){
            toolbarRightitle.setVisibility(View.VISIBLE);
            toolbarRightitle.setText("内容");
        }else if(title.contains("任务")){
            toolbarRightitle.setVisibility(View.VISIBLE);
            toolbarRightitle.setText("内容");
        }
        if(title.contains("任务")){
            mPresenter.postAdd();
        }else {
            mPresenter.getUser();
        }

    }

    @Override
    public String getUserId() {
        return sharePrefManager.getUserId();
    }

    @Override
    public String getRoleId() {
        return roleId;
    }


    @Override
    public void showScuss() {

    }
    private void initPlaces(final List<String> str) {

        pvCustomOptionsPalce = new OptionsPickerBuilder(WeituoActivity.this
                ,new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置

                Intent intent1 = new Intent(WeituoActivity.this, UpAdressActivity.class);
                intent1.putExtra("touserid", SourceDateList.get(pos).getUserid());
                if((options1+1)==1){
                    intent1.putExtra("title", "区域巡查委托");
                }else {
                    intent1.putExtra("title", "仓库保管委托");
                }
                intent1.putExtra("tag", "weituo");
                startActivity(intent1);
                finish();

            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);

                        TextView tvCancel = (TextView) v.findViewById(R.id.tv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptionsPalce.returnData();
                                pvCustomOptionsPalce.dismiss();
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptionsPalce.dismiss();
                            }
                        });

                    }
                })
                .isDialog(false)
                .setOutSideCancelable(false)
                .build();
        pvCustomOptionsPalce.setPicker(str);//添加数据
    }
    int pos=-1;


    @Override
    public void failGetData() {

    }
    @Override
    public void clearData() {
        sharePrefManager.clearData();
        final MyAlertDialog myAlertDialog = new MyAlertDialog(WeituoActivity.this).builder()
                .setTitle("提示")
                .setMsg("该账号在其他设备上登录，请重新登录");
        myAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WeituoActivity.this,LoginActivity.class);
                intent.putExtra("tag","exit");
                startActivity(intent);

            }
        });
        myAlertDialog.show();
    }

    @Override
    public void showUser(List<LoginBean> list) {
        SourceDateList = filledData(list);

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        //RecyclerView社置manager
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        adapter = new SortAdapter(this, SourceDateList,title);
        mRecyclerView.setAdapter(adapter);

        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                        ) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateList(filterDateList);
    }
    private List<SortModel> filledData(List<LoginBean> list) {
        List<SortModel> mSortList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(list.get(i).getUsername());
            sortModel.setUserid(list.get(i).getUserid());
            sortModel.setSelect(false);
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(list.get(i).getUsername().trim());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setLetters(sortString.toUpperCase());
            } else {
                sortModel.setLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }
    public void showAdd(int postion){
        if(null != pvCustomOptionsPalce){
            pvCustomOptionsPalce.show();
            pos=postion;
        }
    }
    public void showUser(int postion){
        if(title.contains("作业")){
            if(multipleType!=-1){
               mPresenter.postMultipleType(SourceDateList.get(postion).getUserid(),multipleType+"",id);
            }else {
                Intent intent1 = new Intent(WeituoActivity.this, AddWorkActivity.class);
                intent1.putExtra("roleId", SourceDateList.get(postion).getUserid());
                intent1.putExtra("title", title);
                intent1.putExtra("type", type);

                startActivity(intent1);
                finish();
            }
        }
    }

    public void sendCheck(int postion){
        Intent intent1=new Intent(WeituoActivity.this,AddWorkActivity.class);

        intent1.putExtra("title",title);
        startActivity(intent1);
        finish();
    }
    public void showMessage(int postion) {
        final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(WeituoActivity.this).builder()
                .setTitle("请输入发送内容")
                .setEditText("");

        myAlertInputDialog.getPositiveButton().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              myAlertInputDialog.getResult();
              myAlertInputDialog.dismiss();
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myAlertInputDialog.dismiss();
            }
        });
        myAlertInputDialog.show();
    }

    @OnClick(R.id.toolbar_rightitle)
    public void onViewClicked() {

        List<SortModel> list=adapter.getmData();
        List<SortModel> list2=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(list.get(i).isSelect())
                list2.add(list.get(i));
        }
        if(title.contains("收件")) {
            Intent intentPut = new Intent(WeituoActivity.this, SendActivity.class);
            intentPut.putExtra("lstBean", (Serializable) list2);
            startActivity(intentPut);
            finish();
        }else {
            Intent intentPut = new Intent(WeituoActivity.this, TaskManagerActivity.class);
            intentPut.putExtra("lstBean", (Serializable) list2);
            startActivity(intentPut);
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(222);
    }
}
