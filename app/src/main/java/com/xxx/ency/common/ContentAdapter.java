package com.xxx.ency.common;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxx.ency.R;

import java.util.List;


import q.rorbin.badgeview.QBadgeView;

public class ContentAdapter extends ArrayAdapter {
    private Context context;
    private List<ContentModel> list;
    private int selectPostion;
    private int resourceId;
    public void setNum(int num) {
        this.num = num;
    }

    private int num=0;

    public int getSelectPostion() {
        return selectPostion;
    }

    public void setSelectPostion(int selectPostion) {
        this.selectPostion = selectPostion;
    }

    public ContentAdapter(Context context, List<ContentModel> list) {
        super(context,R.layout.content_item,list);
        this.resourceId = R.layout.content_item;
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        View view = View.inflate(context, R.layout.content_item,
//                null);
        ContentModel contentModel = (ContentModel)getItem(position);
        LinearLayout contentModelItem = new LinearLayout(getContext());
        String inflater = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
        vi.inflate(resourceId, contentModelItem, true);

        ImageView item_imageview=contentModelItem.findViewById(R.id.item_imageview);
        if(selectPostion-1==position){
            item_imageview.setImageResource(R.drawable.msg_green);
        }else {
            item_imageview.setImageResource(R.drawable.msg_normal);
        }

        TextView item_textview=contentModelItem.findViewById(R.id.item_textview);
        item_textview.setText(contentModel.getText());
        if(contentModel.getText().contains("消息")){
            new QBadgeView(context).bindTarget(item_textview).
                    setBadgeTextSize(8,true).setBadgeNumber(num);
        }
        return contentModelItem;
    }

}
