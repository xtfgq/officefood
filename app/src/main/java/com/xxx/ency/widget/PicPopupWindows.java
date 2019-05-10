package com.xxx.ency.widget;


import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.xxx.ency.R;
import com.xxx.ency.base.BaseActivity;


/**
 * 相册类
 * Created by Administrator on 2016/4/7.
 */
public class PicPopupWindows extends PopupWindow {

    private Context mContext; // 上下文参数

    public interface PipSectect {

        void OnTake();

        void OnSelect();
        void OnCancle();
    }


    public PicPopupWindows(BaseActivity mContext, View parent, String selectPhoto,
                           String selectCapture, final PipSectect listener) {
        super(mContext);
        this.mContext = mContext;
        View view = View
                .inflate(mContext, R.layout.item_popubwindows, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.fade_ins));
        LinearLayout ll_popup = (LinearLayout) view
                .findViewById(R.id.ll_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.push_bottom_in_2));

        setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);

        Button bt1 = (Button) view
                .findViewById(R.id.item_popupwindows_camera);
        bt1.setText(selectCapture);
        Button bt2 = (Button) view
                .findViewById(R.id.item_popupwindows_Photo);
        bt2.setText(selectPhoto);
        Button bt3 = (Button) view
                .findViewById(R.id.item_popupwindows_cancel);

        bt1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.OnTake();
                dismiss();
            }
        });
        bt2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnSelect();
                dismiss();
            }
        });
        bt3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}
