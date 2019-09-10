package com.sinovatio.iesi.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinovatio.iesi.R;
import com.sinovatio.iesi.http.HttpConfig;
import com.sinovatio.iesi.model.entity.ProfileBean;
import com.sinovatio.iesi.tools.GlideUtil;
import com.sinovatio.iesi.view.TargetArchivesInfoActivity;

import java.util.ArrayList;
import java.util.List;

public class TargetProfileQuery_Adapter extends RecyclerView.Adapter<TargetProfileQuery_Adapter.MyViewHolder> implements Filterable {
    private Activity mContext;
    private List<ProfileBean> list;
    private List<ProfileBean> filterList;

    //构造器
    public TargetProfileQuery_Adapter(Activity activity, List<ProfileBean> list,TargetDetailClick targetDetailClick) {
        this.mContext = activity;
        this.list = list;
        this.filterList = list;
        this.targetDetailClick = targetDetailClick;
    }

    //加载布局
    @Override
    public TargetProfileQuery_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TargetProfileQuery_Adapter.MyViewHolder holder = new TargetProfileQuery_Adapter.MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.target_profile_base, parent, false));
        return holder;
    }

    //为布局加载数据
    @Override
    public void onBindViewHolder(TargetProfileQuery_Adapter.MyViewHolder holder, int position) {
        GlideUtil.viewHttpPic_activity(mContext, HttpConfig.BASE_URL+filterList.get(position).getHeadPic(),holder.target_photo,GlideUtil.options);
        holder.target_name.setText(filterList.get(position).getName());
        holder.target_case.setText(filterList.get(position).getCaseCode());
        holder.target_linear.setOnClickListener(new TargetProfileQuery_Adapter.MyTargetClick(position,filterList));
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
        TextView target_case;
        LinearLayout target_linear;

        public MyViewHolder(View itemView) {
            super(itemView);
            target_photo = (ImageView) itemView.findViewById(R.id.target_photo);
            target_name = (TextView) itemView.findViewById(R.id.target_name);
            target_case = (TextView) itemView.findViewById(R.id.target_case);
            target_linear = (LinearLayout) itemView.findViewById(R.id.target_linear);
        }
    }

    private TargetProfileQuery_Adapter.TargetDetailClick targetDetailClick;
    public interface TargetDetailClick{
        void targetDetailClick(int position,List<ProfileBean> filterlist);
    }

    class MyTargetClick implements View.OnClickListener{

        int position;
        List<ProfileBean> filterlist;
        public MyTargetClick(int position,List<ProfileBean> filterlist){
            this.position = position;
            this.filterlist = filterlist;
        }

        @Override
        public void onClick(View v) {
            Intent intent =new Intent(mContext, TargetArchivesInfoActivity.class);
            intent.putExtra("id",filterlist.get(position).id);
            mContext.startActivity(intent);
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
                    List<ProfileBean> mfilterList = new ArrayList<>();
                    for (ProfileBean profileBean : list) {
                        //这里根据需求，添加匹配规则
                        if (profileBean.name.contains(charString)|| profileBean.caseCode.contains(charString)) {
                            mfilterList.add(profileBean);
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
                filterList = (ArrayList<ProfileBean>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
