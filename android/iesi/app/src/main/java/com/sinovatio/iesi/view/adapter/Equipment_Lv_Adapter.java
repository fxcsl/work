package com.sinovatio.iesi.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinovatio.iesi.R;
import com.sinovatio.iesi.constant.Constants;
import com.sinovatio.iesi.model.entity.Equipment_Lv_entity;

import java.util.List;

/**
 * 携带装备种类列表适配器
 */
public class Equipment_Lv_Adapter extends BaseAdapter {

    Context context;
    List<Equipment_Lv_entity> list;
    public Equipment_Lv_Adapter( Context context,List<Equipment_Lv_entity> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            holder=new MyHolder();
            convertView=LayoutInflater.from(context).inflate(R.layout.adapter_equip_list,null);
            holder.iv_icon=convertView.findViewById(R.id.iv_icon);
            holder.tv=convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        }else {
            holder=(MyHolder) convertView.getTag();
        }
        int pos=Integer.parseInt(list.get(position).getDicValue().substring(list.get(position).getDicValue().length()-1));
        holder.iv_icon.setImageResource(Constants.icon_name[pos-1]);
        holder.tv.setText(list.get(position).getDicPorperty());
        return convertView;
    }

    private MyHolder holder;
    class MyHolder{
        ImageView iv_icon;
        TextView tv;
    }
}
