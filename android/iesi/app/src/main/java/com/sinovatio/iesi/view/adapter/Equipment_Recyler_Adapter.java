package com.sinovatio.iesi.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinovatio.iesi.R;
import com.sinovatio.iesi.constant.Constants;
import com.sinovatio.iesi.contract.MainContract;
import com.sinovatio.iesi.model.entity.Equipment_recycler_entity;

import java.util.List;

/**
 * 已经携带装备列表适配器
 */

public class Equipment_Recyler_Adapter extends RecyclerView.Adapter<Equipment_Recyler_Adapter.ViewHolder>{

    Context context;
    List<Equipment_recycler_entity> list;
    public Equipment_Recyler_Adapter(Context context,List<Equipment_recycler_entity> list,DeteleImageClick deteleImageClick){
        this.context=context;
        this.list=list;
        this.deteleImageClick=deteleImageClick;
    }

    @NonNull
    @Override
    public Equipment_Recyler_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(context).inflate(R.layout.adapter_equip_recyclerview,viewGroup,false);
        final ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    //这个方法主要用于适配渲染数据到View中
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.tv_txt.setText(list.get(i).getEqumentName());
        viewHolder.tv_ptxt.setText(list.get(i).getDicPorperty());
        int pos=Integer.parseInt(list.get(i).getEqumentType().substring(list.get(i).getEqumentType().length()-1));
        viewHolder.iv_picon.setImageResource(Constants.icon_name[pos-1]);
        if(list.get(i).getType()==0){
            viewHolder.im_detelt.setVisibility(View.GONE);
        }else {
            viewHolder.im_detelt.setVisibility(View.VISIBLE);
        }
        viewHolder.im_detelt.setOnClickListener(new MyImageClick(list.get(i).getLastEquipmentId(),list.get(i).getIsNew()));
    }

    //BaseAdapter的getCount方法了，即总共有多少个条目
    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView im_detelt;
        ImageView iv_picon;
        TextView tv_ptxt;
        TextView tv_txt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            im_detelt=itemView.findViewById(R.id.im_detele);
            iv_picon=itemView.findViewById(R.id.iv_picon);
            tv_ptxt=itemView.findViewById(R.id.tv_ptxt);
            tv_txt=itemView.findViewById(R.id.tv_txt);
        }
    }

    private DeteleImageClick deteleImageClick;
    public interface DeteleImageClick{
        void deteleImageClick(String id,int isNew);
    }

    class MyImageClick implements View.OnClickListener{

        String id;
        int isNew=0;
        public MyImageClick(String id,int isNew){
            this.id=id;
            this.isNew=isNew;
        }

        @Override
        public void onClick(View v) {
            deteleImageClick.deteleImageClick(id,isNew);
        }
    }
}
