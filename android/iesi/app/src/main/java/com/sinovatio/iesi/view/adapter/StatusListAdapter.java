package com.sinovatio.iesi.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sinovatio.iesi.R;
import com.sinovatio.iesi.model.entity.StatusBean;
import com.sinovatio.iesi.tools.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class StatusListAdapter extends BaseAdapter {

    public Context context;
    public List<StatusBean> list=new ArrayList<>();
    public StatusListAdapter(Context context,List<StatusBean> list,CheckBoxChangeed checkBoxChangeed){
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
        if(convertView==null){
            holder=new ViewHolder();
            convertView=LayoutInflater.from(context).inflate(R.layout.adapter_equip_detail,null);
            holder.cb=convertView.findViewById(R.id.cb_chosse);
            holder.tv=convertView.findViewById(R.id.tv_txt);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder) convertView.getTag();
        }
        holder.cb.setOnCheckedChangeListener(null);
        if(list.get(position).getMeans()!=("-1")){
            holder.tv.setTextColor(0xff43454F);
            holder.cb.setChecked(false);
        }else {
            holder.tv.setTextColor(0xff108EE9);
            holder.cb.setChecked(true);
        }
        holder.tv.setText(list.get(position).getDicValue());
        holder.cb.setOnCheckedChangeListener(new MyChecked(position));
        return convertView;
    }

    ViewHolder holder;
    class ViewHolder{
        TextView tv;
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
