package com.sinovatio.iesi.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinovatio.iesi.R;
import com.sinovatio.iesi.model.entity.Equipment_recycler_entity;
import com.sinovatio.iesi.model.entity.EquipmentsBean;

import java.util.List;

public class EquipDetail_Adapter extends BaseAdapter {
    Context context;
    List<EquipmentsBean> list;
    public EquipDetail_Adapter(Context context, List<EquipmentsBean> list, CheckBoxChangeed checkBoxChangeed){
        this.context=context;
        this.list=list;
        this.checkBoxChangeed=checkBoxChangeed;
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
        if (convertView==null){
            holder=new MyHolder();
            convertView=LayoutInflater.from(context).inflate(R.layout.adapter_equip_detail,null);
            holder.tv=convertView.findViewById(R.id.tv_txt);
            holder.cb=convertView.findViewById(R.id.cb_chosse);
            holder.top_lin=convertView.findViewById(R.id.top_lin);
            convertView.setTag(holder);
        }else {
            holder= (MyHolder) convertView.getTag();
        }
        holder.tv.setText(list.get(position).getEquipmentName());
//
        if(list.get(position).getIdChoice()==1){
            holder.cb.setClickable(false);
//            holder.cb.setChecked(true);
            holder.top_lin.setBackgroundColor(0xffeeeeee);
        }else {
            holder.cb.setClickable(true);
//            holder.cb.setChecked(false);
            holder.top_lin.setBackgroundColor(0xffffffff);
        }
//        if(list.get(position).getType()==0){
//            holder.cb.setChecked(false);
//        }else {
//            holder.cb.setChecked(true);
//        }
        holder.cb.setOnCheckedChangeListener(new MyChecked(position));
        return convertView;
    }

    private MyHolder holder;

    class MyHolder{
        TextView tv;
        LinearLayout top_lin;
        CheckBox cb;
    }

    private CheckBoxChangeed checkBoxChangeed;

    public interface CheckBoxChangeed {
        void checkBoxChange(int pos);
    }

    class MyChecked implements   CompoundButton.OnCheckedChangeListener{
        int pos=0;
        public MyChecked(int pos){
            this.pos=pos;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checkBoxChangeed.checkBoxChange(pos);
        }
    }
}
