package com.xxx.ency.view.SecurityCheck;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xxx.ency.R;
import com.xxx.ency.model.bean.CheckListBean;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private List<CheckListBean> datas;

    public MyAdapter(Context context, List<CheckListBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tv_title.setText(datas.get(position).getChildTitle());
        holder.tv_value.setText(datas.get(position).getChildHints());

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_value;


        public ViewHolder(View view) {
            super(view);


            tv_title = (TextView) view.findViewById(R.id.tv_child);
            tv_value = (TextView) view.findViewById(R.id.tv_value);

        }
    }
}
