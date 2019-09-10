package com.sinovatio.iesi.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinovatio.iesi.R;
import com.sinovatio.iesi.model.entity.TargetBean;
import com.sinovatio.iesi.tools.DialogUtil;

import java.util.ArrayList;
import java.util.List;

public class TargetInfoQuery_Adapter extends RecyclerView.Adapter<TargetInfoQuery_Adapter.MyViewHolder> implements Filterable {
    private Activity mContext;
    private List<TargetBean> list;
    private List<TargetBean> filterList;

    //构造器
    public TargetInfoQuery_Adapter(Activity activity, List<TargetBean> list,BindTargetClick bindTargetClick) {
        this.mContext = activity;
        this.list = list;
        this.filterList = list;
        this.bindTargetClick = bindTargetClick;
    }

    //加载布局
    @Override
    public TargetInfoQuery_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TargetInfoQuery_Adapter.MyViewHolder holder = new TargetInfoQuery_Adapter.MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.target_base, parent, false));
        return holder;
    }

    //为布局加载数据
    @Override
    public void onBindViewHolder(TargetInfoQuery_Adapter.MyViewHolder holder, int position) {
        holder.target_name.setText(filterList.get(position).getName());
        holder.target_phonenumber.setText(filterList.get(position).getIdentityCode());
        holder.target_bind.setOnClickListener(new TargetInfoQuery_Adapter.MyTargetClick(position,filterList));
        if(filterList.get(position).getRefFlag().equals("1")) {
            holder.target_bind.setImageResource(R.mipmap.bind);
        }else{
            holder.target_bind.setImageResource(R.mipmap.unbind);
        }
    }

    //总共多少个项
    @Override
    public int getItemCount() {
        return filterList.size();
    }
    //初始化布局信息
    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView target_photo;
        TextView target_name;
        TextView target_phonenumber;
        ImageView target_bind;

        public MyViewHolder(View itemView) {
            super(itemView);
            target_photo = (ImageView) itemView.findViewById(R.id.target_photo);
            target_name = (TextView) itemView.findViewById(R.id.target_name);
            target_phonenumber = (TextView) itemView.findViewById(R.id.target_phonenumber);
            target_bind = (ImageView) itemView.findViewById(R.id.target_bind);
        }
    }

    private BindTargetClick bindTargetClick;
    public interface BindTargetClick{
        void bindTargetClick(int position,List<TargetBean> filterlist);
    }

    class MyTargetClick implements View.OnClickListener{

        int position;
        List<TargetBean> filterlist;
        public MyTargetClick(int position,List<TargetBean> filterlist){
            this.position = position;
            this.filterlist = filterlist;
        }

        @Override
        public void onClick(View v) {
            if(filterlist.get(position).getRefFlag().equals("0")) {
                DialogUtil.showAlertDialog(mContext, "绑定", "确认绑定目标人：" + filterList.get(position).getName() + "？",
                        "取消", "确定", true, new DialogUtil.AlertDialogBtnClickListener() {
                            @Override
                            public void clickPositive() {
                                //positive
                                bindTargetClick.bindTargetClick(position, filterlist);
                            }

                            @Override
                            public void clickNegative() {
                                //negative
                            }
                        });
            }else{
                DialogUtil.showAlertDialog(mContext, "解绑", "确认解绑目标人：" + filterList.get(position).getName() + "？",
                        "取消", "确定", true, new DialogUtil.AlertDialogBtnClickListener() {
                            @Override
                            public void clickPositive() {
                                //positive
                                bindTargetClick.bindTargetClick(position, filterlist);
                            }

                            @Override
                            public void clickNegative() {
                                //negative
                            }
                        });
            }
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty() || charString == "") {
                    //没有过滤的内容，则使用源数据
                    filterList = list;
                } else {
                    List<TargetBean> mfilterList = new ArrayList<>();
                    for (TargetBean targetBean : list) {
                        //这里根据需求，添加匹配规则
                        if (targetBean.name.contains(charString)|| targetBean.identityCode.contains(charString)) {
                            mfilterList.add(targetBean);
                        }
                    }
                    filterList = mfilterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterList = (ArrayList<TargetBean>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
