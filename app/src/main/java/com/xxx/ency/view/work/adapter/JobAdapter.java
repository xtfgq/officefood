package com.xxx.ency.view.work.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxx.ency.R;
import com.xxx.ency.config.EncyApplication;
import com.xxx.ency.model.bean.JobBean;
import com.xxx.ency.model.prefs.SharePrefManager;


import java.util.List;

public class JobAdapter extends BaseQuickAdapter<JobBean, BaseViewHolder>  {

    private Context mcontext;
    private List<JobBean> list;
    private String title;
    private SharePrefManager sharePrefManager;

    public JobAdapter(Context context, List<JobBean> data, String title) {
            super(R.layout.item_work);
        mcontext=context;
        list=data;
        this.title=title;
        sharePrefManager=new SharePrefManager(mcontext);
    }
    public JobAdapter(Context context,int layoutResId, List data,String title) {
        super(layoutResId, data);
        mcontext=context;
        list=data;
        this.title=title;
        sharePrefManager=new SharePrefManager(mcontext);
    }

    @Override
    protected void convert(BaseViewHolder helper, JobBean item) {
        if(title.contains("消息")){
            int status=item.getStatus();
            if(status==0) {
                helper.itemView.findViewById(R.id.iv_icon).setVisibility(View.VISIBLE);
            }else {
                helper.itemView.findViewById(R.id.iv_icon).setVisibility(View.INVISIBLE);
            }
            if("0".equals(item.getType())) {
                Spanned strB= Html.fromHtml( "<b><tt>发送人：</tt></b>" +
                   item.getSendUserId()+"<br/>"+"<b><tt>标题：</tt></b>"+
                   item.getTitle());
                Spanned strC=Html.fromHtml("<b><tt>发送时间：</tt></b>" + item.getData());
                helper.setText(R.id.tv_work_title,strB);
                helper.setText(R.id.tv_person, strC);
            }else{
                Spanned strA= Html.fromHtml("<b><tt>发送给：</tt></b>" +
                        item.getReceiveUserId()+"<br/>"+"<b><tt>标题：</tt></b>"+
                        item.getTitle());
                Spanned strD=Html.fromHtml("<b><tt>发送时间：</tt></b>" + item.getData());
                helper.setText(R.id.tv_work_title, strA);
                helper.setText(R.id.tv_person, strD);
            }
        }
        else if (title.contains("作业")) {
            Spanned strB= Html.fromHtml( "<b><tt>仓号：</tt></b>" +
                    item.getGrainStoreRoomName()+"<br/>"+"<b><tt>作业名称：</tt></b>"+
                    item.getTitle());
                helper.setText(R.id.tv_work_title, strB);

            if (sharePrefManager.getLocalMode() == 5) {
                if(!TextUtils.isEmpty(item.getRefuseCause())) {
                    Spanned strc= Html.fromHtml( "<b><tt>申请人：</tt></b>" +
                            item.getCreateUserRealName()+"<br/>"+"<b><tt>驳回原因：</tt></b>"+
                            item.getRefuseCause());

                    helper.setText(R.id.tv_person, strc);
                }else {
                    Spanned strD= Html.fromHtml( "<b><tt>申请人：</tt></b>" +
                            item.getCreateUserRealName());
                    helper.setText(R.id.tv_person, strD);
                }
            } else {
                Spanned strA= Html.fromHtml( "<b><tt>批准人：</tt></b>" +
                        item.getAuditorUserRealName());
                helper.setText(R.id.tv_person, strA);
            }

        }else if(title.contains("安全")){
            int isAbnormal=Integer.valueOf(item.getIsAbnormal());
            if(isAbnormal==0) {
                Spanned strB= Html.fromHtml( "<b><tt>区域：</tt></b>" +
                        item.getAreaName()+"<br/>"+"<b><tt>检查情况：</tt></b>"+
                       "安全无异常");
                helper.setText(R.id.tv_work_title, strB);
            }else {

                if(TextUtils.isEmpty(item.getDes())) {
                    Spanned strC= Html.fromHtml( "<b><tt>区域：</tt></b>" +
                            item.getAreaName()+"<br/>"+"<b><tt>检查情况：</tt></b>"+
                            item.getAbnormalTypeName());
                    helper.setText(R.id.tv_work_title, strC);
                }else {
                    Spanned strD= Html.fromHtml( "<b><tt>区域：</tt></b>" +
                            item.getAreaName()+"<br/>"+"<b><tt>检查情况：</tt></b>"+
                            item.getAbnormalTypeName()+item.getDes());
                    helper.setText(R.id.tv_work_title, strD);
                }
            }
            Spanned strE= Html.fromHtml( "<b><tt>检查时间：</tt></b>" +
                    item.getStrCreateDate() );
                helper.setText(R.id.tv_person, strE );

        }else if(title.contains("委托")){
            String type=item.getIsAbnormal();
            if("1".equals(type)) {
                Spanned strA= Html.fromHtml( "<b><tt>名称：</tt></b>" +
                        item.getName()+"<br/>"+"<b><tt>类型：</tt></b>"+
                        item.getType()+"<br/>"+"<b><tt>委托人：</tt></b>"+ item.getCreateUserRealName());
                helper.setText(R.id.tv_work_title,strA);
            }else {
                Spanned strB= Html.fromHtml( "<b><tt>名称：</tt></b>" +
                        item.getName()+"<br/>"+"<b><tt>类型：</tt></b>"+
                        item.getType()+"<br/>"+"<b><tt>受托人：</tt></b>"+item.getAuditorUserRealName());
                helper.setText(R.id.tv_work_title,strB);
            }

                helper.setText(R.id.tv_person, item.getDes() );
        }else if(title.contains("送检")){

            if(item.getsDeali()==0) {
                Spanned strB= Html.fromHtml( "<b><tt>仓号：</tt></b>" +
                        item.getTitle()+"<br/>"+"<b><tt>送检人：</tt></b>"+
                        EncyApplication.getInstance().userMap.get(item.getCreateUserId()));
                helper.setText(R.id.tv_work_title, strB);
                Spanned strC= Html.fromHtml( "<b><tt>送检时间：</tt></b>" +
                        item.getData());
                helper.setText(R.id.tv_person, strC);
            }else {
                String name="";
                int x=(int)(Math.random()*5);
                if(TextUtils.isEmpty( EncyApplication.getInstance().userMap
                        .get(item.getCensorshipUserId()).toString())){
                    name=EncyApplication.getInstance().name[x];
                }else {
                    name=EncyApplication.getInstance().userMap
                            .get(item.getCensorshipUserId()).toString();
                }
                Spanned strB= Html.fromHtml( "<b><tt>仓号：</tt></b>" +
                        item.getTitle()+"<br/>"+"<b><tt>送检人：</tt></b>"+
                       name);
                helper.setText(R.id.tv_work_title, strB);
                Spanned strC= Html.fromHtml( "<b><tt>送检时间：</tt></b>" +
                        item.getData()+"<br/>"+"<b><tt>水分检测结果：</tt></b>"+
                        item.getResult());
                helper.setText(R.id.tv_person, strC);
            }
        }else if(title.contains("维修")){
            if(item.getsDeali()==0) {
                Spanned strA= Html.fromHtml( "<b><tt>内容：</tt></b>" +
                        item.getTitle()+"<br/>"+"<b><tt>报修人：</tt></b>"+
                        EncyApplication.getInstance().userMap
                                .get(item.getCreateUserId()));
                helper.setText(R.id.tv_work_title, strA);
                Spanned strB= Html.fromHtml( "<b><tt>报修时间：</tt></b>" +
                        item.getData());
                helper.setText(R.id.tv_person, strB);
            }else {
                String name="";
                int x=(int)(Math.random()*5);
                if(TextUtils.isEmpty( EncyApplication.getInstance().userMap
                        .get(item.getRepairUserId()).toString())){
                    name=EncyApplication.getInstance().name[x];
                }else {
                    name=EncyApplication.getInstance().userMap
                            .get(item.getRepairUserId()).toString();
                }
                Spanned strC= Html.fromHtml( "<b><tt>内容：</tt></b>" +
                        item.getTitle()+"<br/>"+"<b><tt>维修人：</tt></b>"+
                       name);
                helper.setText(R.id.tv_work_title, strC);
                Spanned strD= Html.fromHtml( "<b><tt>维修时间：</tt></b>" +
                        item.getRepairTime());
                helper.setText(R.id.tv_person, strD);
            }
        }else if(title.contains("巡仓")){
            int isAbnormal=Integer.valueOf(item.getIsAbnormal());
            int type=Integer.valueOf(item.getType());
            if(isAbnormal==0) {
                Spanned strA= Html.fromHtml( "<b><tt>巡查区域：</tt></b>" +
                        item.getAreaName()+"<br/>"+"<b><tt>巡查类型：</tt></b>"+
                        getTye(type)+"<br/>"+"<b><tt>检查情况：</tt></b>"+"粮情无异常");
                helper.setText(R.id.tv_work_title, strA);
            }else {
                Spanned strB= Html.fromHtml( "<b><tt>巡查区域：</tt></b>" +
                        item.getAreaName()+"<br/>"+"<b><tt>巡查类型：</tt></b>"+
                        getTye(type)+"<br/>"+"<b><tt>检查情况：</tt></b>"+item.getDes());
                helper.setText(R.id.tv_work_title, strB);
            }
            Spanned strC= Html.fromHtml( "<b><tt>巡查时间：</tt></b>" +
                    item.getData());
            helper.setText(R.id.tv_person,  strC);
        }
        else if(title.contains("日志")){
            Spanned strA= Html.fromHtml( "<b><tt>仓号：</tt></b>" +
                    item.getTitle());
            Spanned strB= Html.fromHtml( "<b><tt>检查时间：</tt></b>" +
                    item.getData());
            helper.setText(R.id.tv_work_title, strA);
            helper.setText(R.id.tv_person, strB);
        }else if(title.contains("任务")){
            int type=Integer.valueOf(item.getIsAbnormal());
            if(type==1){
                Spanned strA= Html.fromHtml( "<b><tt>接收人：</tt></b>" +
                        EncyApplication.getInstance().userMap.get(item.getAuditorUserId())+"<br/>"+
                        "<b><tt>内容：</tt></b>"+item.getContent());
                helper.setText(R.id.tv_work_title, strA);
            }else {
                Spanned strB= Html.fromHtml( "<b><tt>派发人：</tt></b>" +
                        EncyApplication.getInstance().userMap.get(item.getCreateUserId())+"<br/>"+
                        "<b><tt>内容：</tt></b>"+item.getContent());
                helper.setText(R.id.tv_work_title, strB);
            }
            helper.itemView.findViewById(R.id.iv_icon).setVisibility(View.VISIBLE);
            int status=item.getStatus();
            if(status==1) {
                ((ImageView) helper.itemView.findViewById(R.id.iv_icon)).setImageResource(R.mipmap.tbd);
            }else if(status==2){
                ((ImageView) helper.itemView.findViewById(R.id.iv_icon)).setImageResource(R.mipmap.finish);
            }
            else {
                ((ImageView) helper.itemView.findViewById(R.id.iv_icon)).setImageResource(R.mipmap.limit);

            }
            Spanned strC= Html.fromHtml( "<b><tt>任务时间：</tt></b>" +
                    item.getDes());

            helper.setText(R.id.tv_person, strC);
        }
    }
    private String  getTye(int type){
        String res="";
        switch (type) {
            case 1:
                res="移动巡仓";
                break;
            case 2:
                res="现场巡仓";
                break;
        }
        return res;
    }

}
