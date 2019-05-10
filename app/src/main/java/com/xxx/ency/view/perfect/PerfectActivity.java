package com.xxx.ency.view.perfect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.xxx.ency.R;
import com.xxx.ency.base.BaseMVPActivity;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.PerfectContract;
import com.xxx.ency.di.component.DaggerPerfectActivityComponent;
import com.xxx.ency.di.module.PerfectActivityModule;
import com.xxx.ency.model.bean.LoginBean;
import com.xxx.ency.model.prefs.SharePrefManager;
import com.xxx.ency.presenter.PrefectPresenter;
import com.xxx.ency.util.FileUtil;
import com.xxx.ency.util.LQRPhotoSelectUtils;
import com.xxx.ency.view.login.LoginActivity;
import com.xxx.ency.view.work.AddWorkActivity;
import com.xxx.ency.widget.CircleImageView;
import com.xxx.ency.widget.ContainsEmojiEditText;
import com.xxx.ency.widget.PicPopupWindows;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import kr.co.namee.permissiongen.PermissionGen;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class PerfectActivity extends BaseMVPActivity<PrefectPresenter> implements
        PerfectContract.View {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rlseclectplace)
    RelativeLayout rlseclectplace;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_sex_value)
    ContainsEmojiEditText tvSexValue;
    @BindView(R.id.rlsex)
    RelativeLayout rlsex;
    @BindView(R.id.btnlogin)
    Button btnlogin;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;

    private PicPopupWindows picPop;
    @Inject
    SharePrefManager sharePrefManager;
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private int index = -1;
    String imgContent;

    @Override
    protected void initInject() {
        DaggerPerfectActivityComponent.builder().
                appComponent(EncyApplication.getAppComponent())
                .perfectActivityModule(new PerfectActivityModule())
                .build().inject(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_perfect;
    }

    @Override
    protected void initialize() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("账号资料");
        mPresenter.getUser();
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(PerfectActivity.this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
//                upLoadFile(outputFile);
                Luban.with(PerfectActivity.this)
                        .load(outputFile)
                        .ignoreBy(100)
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                                // TODO 压缩开始前调用，可以在方法内启动 loading UI
                            }

                            @Override
                            public void onSuccess(File file) {
                                // TODO 压缩成功后调用，返回压缩后的图片文件
                                try {
                                    //加载图片
                                    Glide.with(PerfectActivity.this).load(file).into(ivHead);
                                    imgContent = FileUtil.encodeBase64File(file);

                                    mPresenter.upLoad();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                // TODO 当压缩过程出现问题时调用
                            }
                        }).launch();

            }
        }, true);
    }

    @Override
    public String getUserid() {
        return sharePrefManager.getUserId();
    }

    @Override
    public String getBase64() {
        return imgContent;
    }

    @Override
    public String getRealName() {
        return tvSexValue.getText().toString();
    }

    @Override
    public void showLoginBean(LoginBean lb) {
        if (!TextUtils.isEmpty(lb.getPortrait())) {
            byte[] decodedString = Base64.decode(lb.getPortrait(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivHead.setImageBitmap(decodedByte);
        }
        tvSexValue.setText(lb.getUsername());
        tvSexValue.setSelection(lb.getUsername().length());
    }

    @Override
    public void clearData() {
        sharePrefManager.clearData();
        final MyAlertDialog myAlertDialog = new MyAlertDialog(PerfectActivity.this).builder()
                .setTitle("提示")
                .setMsg("该账号在其他设备上登录，请重新登录");
        myAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PerfectActivity.this,LoginActivity.class);
                intent.putExtra("tag","exit");
                startActivity(intent);

            }
        });
        myAlertDialog.show();
    }

    @Override
    public void sucuss() {
       showMsg("更新成功");
        new Handler().postDelayed(new Runnable(){
            public void run() {
               finish();
            }
        }, 2000);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rlseclectplace, R.id.rlsex, R.id.btnlogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlseclectplace:
                picPop = new PicPopupWindows(PerfectActivity.this, ivHead, "从相册中选取", "拍照",
                        new PicPopupWindows.PipSectect() {
                            @Override
                            public void OnTake() {
                                index = 0;
                                if (Build.VERSION.SDK_INT >= 23) {
                                    if (checkPermission(Manifest.permission.CAMERA) &&
                                            checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                            ) {
                                        mLqrPhotoSelectUtils.takePhoto();
                                    } else {
                                        PermissionGen.with(PerfectActivity.this)
                                                .addRequestCode(100)
                                                .permissions(
                                                        Manifest.permission.CAMERA,
                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                        Manifest.permission.WRITE_SETTINGS
                                                )
                                                .request();
                                    }
                                } else {
                                    mLqrPhotoSelectUtils.takePhoto();
                                }

                            }

                            @Override
                            public void OnSelect() {
                                index = 1;
                                if (Build.VERSION.SDK_INT >= 23) {
                                    if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                            ) {
                                        mLqrPhotoSelectUtils.selectPhoto();
                                    } else {
                                        PermissionGen.with(PerfectActivity.this)
                                                .addRequestCode(100)
                                                .permissions(

                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                        Manifest.permission.WRITE_SETTINGS
                                                )
                                                .request();
                                    }
                                } else {
                                    mLqrPhotoSelectUtils.selectPhoto();
                                }

                            }

                            @Override
                            public void OnCancle() {
                                index = -1;
                            }
                        });
                break;

            case R.id.rlsex:
                break;
            case R.id.btnlogin:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String permissions[], @android.support.annotation.NonNull int[] grantResults) {
        if (index == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    checkPermission(Manifest.permission.CAMERA) &&
                    checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ) {

                mLqrPhotoSelectUtils.takePhoto();
            }
        } else if (index == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ) {

                mLqrPhotoSelectUtils.selectPhoto();
            }
        }
    }

    @OnClick(R.id.btnlogin)
    public void onViewClicked() {

        if(TextUtils.isEmpty(tvSexValue.getText().toString())){
            showMsg("请填写姓名");
            return;
        }
        mPresenter.upLoad();
    }

}
