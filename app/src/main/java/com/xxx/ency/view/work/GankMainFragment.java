package com.xxx.ency.view.work;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.xxx.ency.R;
import com.xxx.ency.base.BaseFragment;
import com.xxx.ency.view.work.adapter.TitleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xiarh on 2017/11/27.
 */

public class GankMainFragment extends BaseFragment {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager_gank)
    ViewPager viewPager;
    String postTye="";
    String typeStr="";
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> types = new ArrayList<>();
    private TitleAdapter titleAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gank;
    }
    String id="";
    @Override
    protected void initialize() {
        Bundle args = getArguments();
        if (args != null) {
            postTye = args.getString("title");
            typeStr = args.getString("type");
            id=args.getString("id");
        }
        if(postTye.contains("作业")) {
//            types.add("待申请");
            types.add("待批准");
            types.add("已批准");
            types.add("已完成");
            types.add("被驳回");
//            fragments.add(GankFragment.newInstance("0",postTye,typeStr,""));
//            fragments.add(GankFragment.newInstance("1",postTye,typeStr,""));
            fragments.add(GankFragment.newInstance("1",postTye,typeStr,""));
            fragments.add(GankFragment.newInstance("2",postTye,typeStr,""));
            fragments.add(GankFragment.newInstance("3",postTye,typeStr,""));
            fragments.add(GankFragment.newInstance("4",postTye,typeStr,""));
        }else if(postTye.contains("送检")) {
            types.add("未检验");
            types.add("已检验");
            fragments.add(GankFragment.newInstance("0",postTye,typeStr,""));
            fragments.add(GankFragment.newInstance("1",postTye,typeStr,""));
        }else if(postTye.contains("维修")){
            types.add("未维修");
            types.add("已维修");
            fragments.add(GankFragment.newInstance("0",postTye,typeStr,""));
            fragments.add(GankFragment.newInstance("1",postTye,typeStr,""));
        }else if(postTye.contains("消息")){


            types.add("已收消息");
            types.add("已发消息");
            fragments.add(GankFragment.newInstance("0",postTye,typeStr,""));
            fragments.add(GankFragment.newInstance("1",postTye,typeStr,""));
        }else if(postTye.contains("日志")){

            if("1".equals(typeStr)){
                types.add("定项检查");
            }else if("2".equals(typeStr)){
                types.add("入仓检查");
            }
            fragments.add(GankFragment.newInstance(typeStr,postTye,typeStr,id));

        }else if(postTye.contains("委托")){

            types.add("我的委托");
            types.add("我被委托");
            fragments.add(GankFragment.newInstance("2",postTye,typeStr,""));
            fragments.add(GankFragment.newInstance("1",postTye,typeStr,""));
        }else if(postTye.contains("任务")){
            types.add("派发");
            types.add("接收");
            fragments.add(GankFragment.newInstance("1",postTye,typeStr,""));
            fragments.add(GankFragment.newInstance("2",postTye,typeStr,""));
        }

        titleAdapter = new TitleAdapter(getChildFragmentManager(), fragments, types);
        if(titleAdapter!=null) {
            viewPager.setAdapter(titleAdapter);
            tablayout.setTabMode(TabLayout.MODE_FIXED);
            tablayout.setupWithViewPager(viewPager);
        }
    }

    public static GankMainFragment newInstance(String title,String type,String id) {
        GankMainFragment gankFragment = new GankMainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("type", type);
        bundle.putString("id", id);
        gankFragment.setArguments(bundle);
        return gankFragment;
    }
    public void refurhFragment(){
        for(int i=0;i<fragments.size();i++) {
            ((GankFragment) fragments.get(i)).onRefresh();
        }
    }
}
