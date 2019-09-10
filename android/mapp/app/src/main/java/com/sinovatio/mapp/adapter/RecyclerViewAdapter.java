package com.sinovatio.mapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sinovatio.mapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:一个垂直的基本的RecycleView的适配器
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> mTitle;
    private List<String> mText;

    //自定义点击事件和长按事件

    //构造器
    public RecyclerViewAdapter(Context mContext, List<String> mTitle,List<String> mText) {
        this.mContext = mContext;
        this.mTitle = mTitle;
        this.mText = mText;
    }

    public RecyclerViewAdapter(Context mContext, Map<String,String> data) {
        this.mContext = mContext;
        List<String> mTitle = new ArrayList<>();
        List<String> mText = new ArrayList<>();
            for (Map.Entry<String,String> entry : data.entrySet()) {
               String key = entry.getKey() ;
               String value = entry.getValue();
                mTitle.add(key);
                mText.add(value);
            }

        this.mTitle = mTitle;
        this.mText = mText;
    }


    //加载布局
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_base, parent, false));
        return holder;
    }
    //为布局加载数据
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_title.setText(mTitle.get(position));
        holder.tv_text.setText(mText.get(position));

    }
    //设置点击事件


    //总共多少个项
    @Override
    public int getItemCount() {
        return mTitle.size();
    }
    //初始化布局信息
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_text;
        TextView tv_title;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }

    //添加

    //删除

}
