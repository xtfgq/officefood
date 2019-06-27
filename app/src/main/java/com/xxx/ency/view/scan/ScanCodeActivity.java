package com.xxx.ency.view.scan;


import android.Manifest;

import android.content.Intent;

import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hb.dialog.myDialog.MyAlertDialog;
import com.xxx.ency.R;


import com.xxx.ency.base.BaseMVPActivity;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.contract.ScanCodeContract;
import com.xxx.ency.di.component.DaggerScanCodeActivityComponent;
import com.xxx.ency.di.module.ScanCodeActivityModule;
import com.xxx.ency.presenter.ScanCodePresenter;
import com.xxx.ency.view.SecurityCheck.SecurityListActivity;
import com.xxx.ency.view.login.LoginActivity;
import com.xxx.ency.view.send.SendActivity;
import com.xxx.ency.view.work.AreaActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;


public class ScanCodeActivity extends BaseMVPActivity<ScanCodePresenter> implements QRCodeView.Delegate, ScanCodeContract.View {
    private static final int REQUEST_CODE_CAMERA = 999;
    private static final String TAG = ScanCodeActivity.class.getSimpleName();

    private QRCodeView mQRCodeView;
    private Map<String, String> codeMap;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan_code;
    }

    @Override
    protected void initialize() {
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
        codeMap = new HashMap<>();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        mQRCodeView.startSpot();

    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();


    }

    @Override
    protected void initInject() {
        DaggerScanCodeActivityComponent.builder().
                appComponent(EncyApplication.getAppComponent())
                .scanCodeActivityModule(new ScanCodeActivityModule()).build()
                .inject(this);

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().post(222);
        mQRCodeView.onDestroy();
        super.onDestroy();

    }


    String id;
    String type;

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        mQRCodeView.stopSpot();

        if (!TextUtils.isEmpty(result)) {
            if (result.contains("id")) {
                mQRCodeView.stopCamera();
//                mQRCodeView.onDestroy();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    id = jsonObject.getString("id");
                    type = jsonObject.getString("type");
                    if ("1".equals(type)) {
                        if (isRoom(id)) {
                            EventBus.getDefault().post(666);
                            mPresenter.checkCode();

                        } else {
                            Toast.makeText(this, "你所扫的二维码不是你的管辖区域", Toast.LENGTH_SHORT).show();

                        }
                    } else if ("2".equals(type)) {
                        if (isArea(id)) {
                            EventBus.getDefault().post(666);
                            mPresenter.checkCode();

                        } else {
                            Toast.makeText(this, "你所扫的二维码不是你的管辖区域", Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (isCode(result)) {
                codeMap.put(result, "code");
                if (codeMap.size() < 4) {
                    scanCodeDialog();
                } else {
                    Intent intent1 = new Intent(ScanCodeActivity.this,
                            SecurityListActivity.class);
                    intent1.putExtra("title", "入仓检查");
                    intent1.putExtra("type", "2");
                    intent1.putExtra("id", id);
                    intent1.putExtra("fromTo", "6");
                    startActivity(intent1);
                    finish();
                }

            }

        } else {
            Toast.makeText(this, "链接无效,请重新扫描", Toast.LENGTH_SHORT).show();
            mQRCodeView.startSpot();
        }


    }

    /**
     * 扫描仓库内部二维码
     */

    private void scanCodeDialog() {
        final MyAlertDialog myAlertDialog = new MyAlertDialog(ScanCodeActivity.this).builder()
                .setTitle("提示")
                .setMsg("请继续扫描仓库内部的二维码");
        myAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mQRCodeView.startCamera();
                mQRCodeView.startSpot();
            }
        });
        myAlertDialog.show();
    }


    private boolean isArea(String id) {
        boolean isHas = false;
        if (EncyApplication.getInstance().areaMap.containsKey(id))
            isHas = true;
        return isHas;
    }

    private boolean isRoom(String id) {
        boolean isHas = false;
        if (EncyApplication.getInstance().roomMap.containsKey(id))
            isHas = true;
        return isHas;
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "无相机权限,打开相机出错");
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            mQRCodeView.startCamera();
            mQRCodeView.startSpot();
        }
    }


    @Override
    public String getRoomId() {
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
    public String getType() {
        return type;
    }

    @Override
    public void showLocaiton() {
        if ("1".equals(type)) {
            scanCodeDialog();
        } else {
            Intent intent1 = new Intent(ScanCodeActivity.this,
                    AreaActivity.class);
            intent1.putExtra("areaId", id);
            startActivity(intent1);
            finish();
        }

    }

    @Override
    public void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public boolean isCode(String code) {
        boolean res = false;
        for (int i = 0; i < EncyApplication.getInstance().scanCode.length; i++) {
            if (EncyApplication.getInstance().scanCode[i].equals(code)) {
                res = true;
            }
        }
        return res;
    }

}
