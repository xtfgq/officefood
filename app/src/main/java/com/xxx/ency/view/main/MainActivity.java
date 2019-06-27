package com.xxx.ency.view.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.squareup.haha.perflib.Main;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;
import com.tencent.android.tpush.service.cache.CacheManager;
import com.xxx.ency.R;
import com.xxx.ency.base.BaseMVPActivity;
import com.xxx.ency.common.ContentAdapter;
import com.xxx.ency.common.ContentModel;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.MainContract;
import com.xxx.ency.di.component.DaggerMainActivityComponent;
import com.xxx.ency.di.module.MainActivityModule;
import com.xxx.ency.model.bean.CheckTypeForm;
import com.xxx.ency.model.bean.LoginBean;
import com.xxx.ency.model.bean.UpdateBean;
import com.xxx.ency.model.bean.WeatherBean;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.presenter.MainPresenter;
import com.xxx.ency.util.AppExitUtil;
import com.xxx.ency.util.LogUtil;
import com.xxx.ency.util.SystemUtil;
import com.xxx.ency.util.WeatherUtil;
import com.xxx.ency.view.SecurityCheck.SecurityCheckFragment;
import com.xxx.ency.view.SecurityCheck.SecurityCheckListFragment;
import com.xxx.ency.view.SecurityCheck.SecurityListActivity;
import com.xxx.ency.view.about.AboutActivity;
import com.xxx.ency.view.eyepetizer.EyepetizerFragment;
import com.xxx.ency.view.like.LikeFragment;
import com.xxx.ency.view.login.LoginActivity;
import com.xxx.ency.view.map.UpAdressActivity;
import com.xxx.ency.view.one.OneFragment;
import com.xxx.ency.view.perfect.PerfectActivity;
import com.xxx.ency.view.scan.ScanCodeActivity;
import com.xxx.ency.view.servicelist.ServiceListActvity;
import com.xxx.ency.view.setting.SettingActivity;
import com.xxx.ency.view.weixin.WeiXinFragment;
import com.xxx.ency.view.work.AreaActivity;
import com.xxx.ency.view.work.GankMainFragment;
import com.xxx.ency.view.work.WorkActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.design.widget.SwipeDismissBehavior.STATE_SETTLING;
import static android.support.v4.widget.DrawerLayout.STATE_IDLE;

/**
 * 主页
 */
public class MainActivity extends BaseMVPActivity<MainPresenter> implements
         MainContract.View, AMapLocationListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawerlayout)
    DrawerLayout mDrawerLayout;

    @Inject
    SharePrefManager sharePrefManager;
    @BindView(R.id.toolbar_rightitle)
    TextView toolbarRightitle;

    private View mHeaderView;

    private TextView mTxtName;

    private TextView mTxtType;
    private ImageView ivScan;


    private WeatherBean.HeWeather6Bean.NowBean mWeatherBean;

    private static final int PERMISSION_CODE = 1000;

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;

    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;

    // 权限获取提示框
    private MaterialDialog dialog;

    private WeiXinFragment weiXinFragment;
    private OneFragment oneFragment;
    private LikeFragment likeFragment;
    private EyepetizerFragment eyepetizerFragment;
    private GankMainFragment gankFragment;
    private SecurityCheckFragment securityCheckFragment1;
    private SecurityCheckFragment securityCheckFragment2;
    private SecurityCheckFragment securityCheckFragment3;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 仓库id

     */
    private String id;

    /**
     * 1,表示定项检查
     * 2，表示入仓检查
     *
     * @return
     */
    int checkType;

    public int getCheckType() {
        return checkType;
    }

    public void setCheckType(int checkType) {
        this.checkType = checkType;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initInject() {
        DaggerMainActivityComponent
                .builder()
                .appComponent(EncyApplication.getAppComponent())
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(sharePrefManager.getUserId())) {
            mTxtName.setText(sharePrefManager.getUserName());
            mPresenter.getMsgNum();
            mPresenter.getPersonUser();
            mPresenter.getWorkType();

        }
    }
    private ListView mListView;
    private ContentAdapter adapter;
    private List<ContentModel> mList;
    private String getStringType(int model){
        String res="";
        switch (model) {
            case 0:
                res="管理员";
                break;
            case 1:
                res="保管员";
                break;
            case 2:
                res= "检验员";
                break;
            case 3:
                res= "维修员";
                break;
            case 4:
                res= "总经理";
                break;
            case 5:
                res= "保管科长";
                break;

            case 7:
                res="仓储科副科长";
            case 8:
                res="副总经理";
            default:
                break;
        }
        return res;
    }
    String[] menu0;
    String[] menu1;
    String[] menu2;
    String[] menu3;
    String[] menu4;
    String[] menu5;
    String[] menu7;
    String[] menu8;
    private ImageView ivHead;
    @Override
    protected void initialize() {
        setSupportActionBar(mToolbar);
        setTitle("储粮科技");
//        XGPushClickedResult message = XGPushManager.onActivityStarted(this);
//        if (message != null) {
//            //拿到数据自行处理
//            if (isTaskRoot()) {
//                return;
//            }
//            finish();
//        }
//        String mess=this.getIntent().getStringExtra("jump_type");
//        if(!TextUtils.isEmpty(mess)) {
//            Intent intent3 = new Intent();
//            intent3.setClass(MainActivity.this, WorkActivity.class);
//            intent3.putExtra("title", "消息");
//            startActivity(intent3);
//        }
        menu0=getResources().getStringArray(R.array.menu_0);
        menu1=getResources().getStringArray(R.array.menu_1);
        menu2=getResources().getStringArray(R.array.menu_2);
        menu3=getResources().getStringArray(R.array.menu_3);
        menu4=getResources().getStringArray(R.array.menu_4);
        menu5=getResources().getStringArray(R.array.menu_5);
        menu7=getResources().getStringArray(R.array.menu_7);
        menu8=getResources().getStringArray(R.array.menu_8);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                /**
                 * 抽屉滑动时，调用此方法
                 * */

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                /**
                 * 抽屉被完全展开时，调用此方法
                 * */

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                /**
                 * 抽屉被完全关闭时，调用此方法
                 * */

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                /**
                 * 抽屉状态改变时，调用此方法
                 * */
                if (TextUtils.isEmpty(sharePrefManager.getLocalToken()) &&
                        newState == STATE_SETTLING) {
                    goLogin();
                }else if(newState == STATE_IDLE&&!TextUtils.isEmpty(sharePrefManager.getLocalToken())){
                    mPresenter.getUser();
                    mPresenter.getAreaData();
                    mPresenter.getRoomData();
                    setMenu(sharePrefManager.getLocalMode());
                    mTxtName.setText(sharePrefManager.getUserName());
                    mTxtType.setText(getStringType(sharePrefManager.getLocalMode()));

                }

            }

        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        WeatherUtil.init(mContext);

        mHeaderView = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.nav_header, null);
        mTxtName = mHeaderView.findViewById(R.id.tv_name);
        mTxtType = mHeaderView.findViewById(R.id.tv_type);
        ivHead=mHeaderView.findViewById(R.id.iv_head);
        ivHead.setClickable(true);
        ivScan = (ImageView) mHeaderView.findViewById(R.id.iv_scan);
        mListView = (ListView) findViewById(R.id.left_listview);
        mListView.addHeaderView(mHeaderView);
        mList=new ArrayList<>();
        mList.add(new ContentModel("储粮科技"));

        adapter = new ContentAdapter(this,mList);
        mListView.setAdapter(adapter);
        mPresenter.checkPermissions();
        initDialog();

        ivScan.setClickable(true);
        ivScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPresenter.checkPermissionsScan();

            }
        });
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,PerfectActivity.class));

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                if(postion>0) {
                    adapter.setSelectPostion(postion);
                    adapter.notifyDataSetChanged();
                    goActivity(mList.get(postion - 1).getText());
                    mDrawerLayout.closeDrawers();
                }

            }
        });
        initXgConfig();
        initFragment();
        EventBus.getDefault().register(this);
        mPresenter.getWeather(sharePrefManager.getLocation());
    }
    private void goActivity(String target){
//        clearData();
        if("储粮科技".equals(target)){
            setTitle("储粮科技");
            showHideFragment(weiXinFragment);
        }else if("作业管理".equals(target)){
            setTitle("作业管理");
            showHideFragment(securityCheckFragment3);
        }else if("我的消息".equals(target)){
            Intent intent3=new Intent(MainActivity.this,WorkActivity.class);
            intent3.putExtra("title","消息");
            startActivity(intent3);
        }else if("仓储检查".equals(target)){
            setTitle("仓储检查");
            showHideFragment(securityCheckFragment1);
        }else if("扦样送检".equals(target)){
            Intent intent2=new Intent(MainActivity.this,WorkActivity.class);
            intent2.putExtra("title","扦样送检");
            startActivity(intent2);
        }else if("维修管理".equals(target)){
            Intent intent2=new Intent(MainActivity.this,WorkActivity.class);
            intent2.putExtra("title","维修管理");
            startActivity(intent2);
        }else if("设置".equals(target)){
            startActivity(new Intent(mContext, SettingActivity.class));
        }else if("关于".equals(target)){
            startActivity(new Intent(mContext, AboutActivity.class));
        }else if("工作日志".equals(target)){
            setTitle("工作日志");
            showHideFragment(securityCheckFragment2);
        }else if("区域管理".equals(target)||"仓库管理".equals(target)){
            Intent intent2=new Intent(MainActivity.this,UpAdressActivity.class);
            intent2.putExtra("title",target);
            startActivity(intent2);
        }else if("委托管理".equals(target)){
            Intent intent2=new Intent(MainActivity.this,WorkActivity.class);
            intent2.putExtra("title",target);
            startActivity(intent2);
        }else if("安全管理".equals(target)){
            Intent intent=new Intent(MainActivity.this,AreaActivity.class);
            intent.putExtra("title",target);
            startActivity(intent);
        }else if("移动巡仓".equals(target)){
            Intent intent=new Intent(MainActivity.this,ServiceListActvity.class);
            intent.putExtra("title",target);
            startActivity(intent);
        }else if("任务提醒".equals(target)){
            Intent intent=new Intent(MainActivity.this,WorkActivity.class);
            intent.putExtra("title",target);
            startActivity(intent);
        }
    }

    /**
     * （1）储粮科技（通知通告改为此，通知信息调整在“我的消息”中，该模块不登录仍可浏览）
     * （2）我的消息
     * （3）仓储检查（检查与作业改为此）
     * （4）作业管理
     * （5）扦样送检（送检管理改为此）
     * （6）维修管理（维修管理改为此）
     * （7）区域管理
     * @param worktype
     */
    private void setMenu(int worktype){

        switch (worktype){
            case 0:
                mList.clear();
                addMenu(menu0);
                break;
            case 1:
                mList.clear();
                addMenu(menu1);
                break;
            case 2:
                mList.clear();
            addMenu(menu2);
                break;
            case 3:
                mList.clear();
             addMenu(menu3);
                break;
            case 4:
                mList.clear();
              addMenu(menu4);
                break;
            case 5:
                mList.clear();
                addMenu(menu5);
                break;
            case 7:
                mList.clear();
                addMenu(menu7);
                break;
            case 8:
                mList.clear();
                addMenu(menu7);
                break;
                default:
                    break;

        }
        adapter.setNum(msgNum);
        adapter.setSelectPostion(1);
        adapter.notifyDataSetChanged();

    }
    private void addMenu(String[] array){
        for(int i=0;i<array.length;i++){
            mList.add(new ContentModel(array[i]));
        }

    }

    /**
     * 定位初始化
     */
    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(mContext);
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);

        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔 单位毫秒
        mLocationOption.setInterval(60* 1000 *30);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //启动定位
        mLocationClient.startLocation();
    }

    private void initDialog() {
        dialog = new MaterialDialog.Builder(mContext)
                .title(R.string.permission_application)
                .content(R.string.permission_application_content)
                .cancelable(false)
                .positiveText(R.string.setting)
                .positiveColorRes(R.color.colorPositive)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivityForResult(intent, PERMISSION_CODE);
                    }
                })
                .negativeText(R.string.no)
                .negativeColorRes(R.color.colorNegative)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // 不给权限就直接退出
                        AppExitUtil.exitAPP(mContext);
                    }
                })
                .build();
    }


    private void initFragment() {
        weiXinFragment = new WeiXinFragment();
        oneFragment = new OneFragment();
        likeFragment = new LikeFragment();
        eyepetizerFragment = new EyepetizerFragment();
        gankFragment = new GankMainFragment();
        securityCheckFragment1 = SecurityCheckFragment.newInstance("1");
        securityCheckFragment2 = SecurityCheckFragment.newInstance("2");
        securityCheckFragment3 = SecurityCheckFragment.newInstance("3");

        loadMultipleRootFragment(R.id.main_content, 0, weiXinFragment,
                oneFragment, gankFragment, eyepetizerFragment, likeFragment,
                securityCheckFragment1,securityCheckFragment2,securityCheckFragment3);
    }

    /**
     * 检查更新提示框
     */
    @Override
    public void showUpdateDialog(final UpdateBean updateBean) {
        if (null != updateBean) {
            new MaterialDialog.Builder(mContext)
                    .title(R.string.app_update)
                    .content("最新版本：" + updateBean.getVersionShort() + "\n"
                            + "版本大小：" + SystemUtil.getFormatSize(updateBean.getBinary().getFsize()) + "\n"
                            + "更新内容：" + updateBean.getChangelog())
                    .negativeText(R.string.no)
                    .negativeColorRes(R.color.colorNegative)
                    .positiveText(R.string.update)
                    .positiveColorRes(R.color.colorPositive)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            showMsg(getResources().getString(R.string.start_update));
                            Intent intent = new Intent(mContext, UpdateService.class);
                            intent.putExtra("downloadurl", updateBean.getInstall_url());
                            startService(intent);
                        }
                    })
                    .show();
        }
    }

    /**
     * 获取天气信息成功
     *
     * @param weatherBean
     */
    @Override
    public void showWeather(WeatherBean.HeWeather6Bean.NowBean weatherBean) {
        if (weatherBean != null) {
            mWeatherBean = weatherBean;
            weiXinFragment.getData();
        }

    }

    @Override
    public WeatherBean.HeWeather6Bean.NowBean getWeathter() {
        return mWeatherBean;
    }

    /**
     * 权限未获取，显示提示框
     */
    @Override
    public void showPermissionDialog() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 获得权限成功，进行之后的所有操作
     */
    @Override
    public void getPermissionSuccess() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        sharePrefManager.setDeviceId(tm.getDeviceId());
        mPresenter.checkUpdate();
        initLocation();
    }

    @Override
    public void changeDayOrNight(boolean changed) {
        if (changed) {
            finish();
        }
    }

    @Override
    public void getPermissionScanSuccess() {
        startActivity(new Intent(MainActivity.this,ScanCodeActivity.class));
    }


    @Override
    public void clearData() {

        sharePrefManager.clearData();
        final MyAlertDialog myAlertDialog = new MyAlertDialog(MainActivity.this).builder()
                .setTitle("提示")
                .setMsg("该账号在其他设备上登录，请重新登录");
        myAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
//                        intent.putExtra("tag","exit");
//                        intent.putExtra("fromTo","home");
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        mPresenter.setFalse();


                    }
                });
        myAlertDialog.show();
    }
    int msgNum=0;

    @Override
    public void showMsgNum(int num) {
        msgNum=num;
    }



    @Override
    public String getUserid() {
        return sharePrefManager.getUserId();
    }

    @Override
    public String roomId() {
        return id;
    }

    @Override
    public String getlatitude() {

        return EncyApplication.Latitude;
    }

    @Override
    public String getlongitude() {
        return EncyApplication.Longitude;
    }

    @Override
    public void showLocaiton() {

            isCheck = false;
            Intent intent1 = new Intent(MainActivity.this, SecurityListActivity.class);
            if (checkType == 1) {
                intent1.putExtra("title", "定项检查");
                intent1.putExtra("type", "1");
                intent1.putExtra("id", id);
                intent1.putExtra("fromTo", "6");
                startActivity(intent1);
            } else if (checkType == 2) {

                MyAlertDialog myAlertDialog = new MyAlertDialog(MainActivity.this).builder()
                        .setTitle("提示")
                        .setMsg("请对准二维码进行扫描")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this,ScanCodeActivity.class));
                            }
                        });
                myAlertDialog.show();

            } else {
                intent1.putExtra("title", "通风作业");
                intent1.putExtra("type", "3");
                intent1.putExtra("id", id);
                intent1.putExtra("fromTo", "6");
                startActivity(intent1);
            }



    }

    @Override
    public void showLoginBean(LoginBean lb) {
        if (!TextUtils.isEmpty(lb.getPortrait())) {
            byte[] decodedString = Base64.decode(lb.getPortrait(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivHead.setImageBitmap(decodedByte);
        }else {
            ivHead.setImageResource(R.mipmap.icon_user_tx1);
        }
        sharePrefManager.setUser(lb.getUsername());
        mTxtName.setText(lb.getUsername());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_CODE) {
            mPresenter.checkPermissions();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                mDrawerLayout.openDrawer(Gravity.LEFT);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }



    boolean isCheck=false;
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                if(!isCheck) {
                    if(TextUtils.isEmpty(aMapLocation.getCity())){
                        mPresenter.getWeather(sharePrefManager.getLocation());
                    }else {
                        sharePrefManager.setLocation(aMapLocation.getCity());
                        mPresenter.getWeather(aMapLocation.getCity());
                    }
                }else {
                        mPresenter.checkLocaiton();
                }
                EncyApplication.Latitude=aMapLocation.getLatitude()+"";
                EncyApplication.Longitude=aMapLocation.getLongitude()+"";
                //showMsg(Latitude+"::::"+Longitude);
            }
            //定位失败，通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息
            else {
                showMsg("请打开位置权限");
                LogUtil.e("AmapError",
                        "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" +
                                aMapLocation.getErrorInfo());
            }
        }
    }



    @Override
    public void onBackPressedSupport() {

        AppExitUtil.exitApp(this, mToolbar);
    }

    public void showSecurityList() {

        if (checkType == 1) {
            Intent intent1 = new Intent(MainActivity.this, SecurityListActivity.class);
            intent1.putExtra("title", "定项检查");
            intent1.putExtra("type", "1");
            intent1.putExtra("id", id);
            intent1.putExtra("fromTo", "6");
            startActivity(intent1);
        }  else {
            isCheck=true;
            mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }


    }
    public void showSecurityBrowList() {
        Intent intent1=new Intent(MainActivity.this,WorkActivity.class);
        if (checkType == 1) {
            intent1.putExtra("type","1");
        } else if (checkType == 2) {
            intent1.putExtra("type","2");
        }else {
            intent1.putExtra("type",checkType+"");

        }
        if(sharePrefManager.getLocalMode()==1||sharePrefManager.getLocalMode()==0) {
            intent1.putExtra("title","工作日志");

        }
        else {
            intent1.putExtra("title","移动巡仓");

        }
        intent1.putExtra("id",id);
        intent1.putExtra("isBrow","1");
        startActivity(intent1);

    }
    public void showWork(String title,String type) {

        Intent intent=new Intent(MainActivity.this,WorkActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("type",type);
        startActivity(intent);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 销毁定位
         * 如果AMapLocationClient是在当前Activity实例化的，
         * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
         */
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }

        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void getEventBus(Integer num) {
        if (num != null&&num==666) {
            try {

                mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
                mLocationClient.stopLocation();
                mLocationClient.startLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (num != null&&num==222) {
            mPresenter.getMsgNum();
            try {
                setTitle("储粮科技");
                showHideFragment(weiXinFragment);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void goLogin() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        mDrawerLayout.closeDrawers();
    }
    private void initXgConfig() {
        // 开启logcat输出，方便debug，发布时请关闭

        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(),
        // XGIOperateCallback)带callback版本
        // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext
        XGPushConfig.enableOtherPush(getApplicationContext(), true);
        XGPushConfig.enableDebug(getApplicationContext(),true);
        XGPushConfig.setHuaweiDebug(true);

        XGPushManager.registerPush(getApplicationContext(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                Log.w(com.tencent.android.tpush.common.Constants.LogTag, "注册信鸽推送成功token:" + data);
                // Toast.makeText(AtyWelcome.this, "注册信鸽推送成功token:"+data,
                // Toast.LENGTH_LONG).show();
                com.xxx.ency.config.Constants.XINGE=data.toString();
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

    }




}