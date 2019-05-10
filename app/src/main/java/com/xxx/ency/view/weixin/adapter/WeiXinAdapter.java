package com.xxx.ency.view.weixin.adapter;

import android.text.TextUtils;
import android.widget.ImageView;


import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxx.ency.R;
import com.xxx.ency.model.bean.WeiXinBean;
import com.xxx.ency.util.AppNetWorkUtil;
import com.xxx.ency.util.ImageLoader;
import com.xxx.ency.util.WeatherUtil;

import java.util.List;

/**
 * Created by xiarh on 2017/11/8.
 */

public class WeiXinAdapter extends BaseMultiItemQuickAdapter<WeiXinBean.NewslistBean, BaseViewHolder> {

    private boolean isPTP = false;

    public void setPTP(boolean ptp) {
        this.isPTP = ptp;
    }

    public WeiXinAdapter(List<WeiXinBean.NewslistBean> data) {
        super(data);
        addItemType(WeiXinBean.NewslistBean.HEAD, R.layout.weixin_header);
        addItemType(WeiXinBean.NewslistBean.INFO, R.layout.item_weixin);

    }

    @Override
    protected void convert(BaseViewHolder helper, WeiXinBean.NewslistBean item) {
        switch (helper.getItemViewType()) {
            case WeiXinBean.NewslistBean.HEAD:
                helper.setText(R.id.txt_city,item.getLocation());
                helper.setText(R.id.txt_weather,item.getCond_txt());
                helper.setText(R.id.txt_temperature,item.getTmp()+"°C");
                helper.setText(R.id.tv_time,item.getCtime());
                helper.setText(R.id.tv_air,"pm2.5:"+item.getPm25()+"    "+pm25Result(Integer.valueOf(item.getPm25())));
                if(!TextUtils.isEmpty(item.getWind_dir())) {
                    helper.setText(R.id.tv_wind, item.getWind_dir() + item.getWind_sc() + "级");
                }
//                ImageView imageView = helper.getView(R.id.img_weather);
//                ImageLoader.loadAllAsBitmap(mContext, WeatherUtil.getImageUrl(
//                       item.getCond_code()), imageView);
//                ImageLoader.loadAll(mContext,item.getPicUrl(),(ImageView) helper.getView(R.id.img_weather_bg));
                break;
            case WeiXinBean.NewslistBean.INFO:
                helper.setText(R.id.txt_weixin_title, item.getTitle());
//                helper.setText(R.id.txt_weixin_author, item.getDescription());
                helper.setText(R.id.txt_weixin_date, item.getCtime());
                if (isPTP && AppNetWorkUtil.getNetworkType(mContext) == AppNetWorkUtil.TYPE_MOBILE) {
                    ImageLoader.loadDefault(mContext,(ImageView) helper.getView(R.id.img_weixin));
                } else {
                    ImageLoader.loadAll(mContext,item.getPicUrl(),(ImageView) helper.getView(R.id.img_weixin));
                }
                break;
        }

    }
    /**
     优：0~50

     良：50~100

     轻度污染：100~150

     中度污染：150~200

     重度污染：200~300

     严重污染：大于300及以上
     */
    public String pm25Result(int pm25){
        String res="";
        if(pm25<=100){
            res="优秀";
        }else if(pm25>100&&pm25<=150){
            res="轻度";
        }else if(pm25>150&&pm25<=200){
            res="中度";
        }else if(pm25>250&&pm25<=300){
            res="重度";
        }else {
            res="严重";
        }
        return res;


    }
}
