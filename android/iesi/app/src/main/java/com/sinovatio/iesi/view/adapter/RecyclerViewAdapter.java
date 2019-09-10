package com.sinovatio.iesi.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinovatio.iesi.R;
import com.sinovatio.iesi.model.entity.TaskBean;
import com.sinovatio.iesi.tools.SharedPreferencesHelper;
import com.sinovatio.iesi.view.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:一个垂直的基本的RecycleView的适配器
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements Filterable {

    private Activity mContext;
    private List<TaskBean> list;
    private List<TaskBean> filterList;
    private SharedPreferencesHelper sharedPreferences;
    private String currentMissionId = "";

    //自定义点击事件和长按事件

    //构造器
    public RecyclerViewAdapter(Activity activity, List<TaskBean> list) {
        this.mContext = activity;
        this.list = list;
        this.filterList = list;
        sharedPreferences=new SharedPreferencesHelper(mContext,"taskId");
        if( null!=sharedPreferences.getString("MissionId")){
            currentMissionId = sharedPreferences.getString("MissionId");
        }
    }

    //加载布局
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.task_base, parent, false));
        return holder;
    }

    //为布局加载数据
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        if(currentMissionId == null) {
//            if (sharedPreferences.getString("MissionId").equals(filterList.get(position).getMissionId())) {
////            holder.task_Linear.setFocusable(true);
////            holder.task_Linear.requestFocus();
////            holder.task_Linear.setFocusableInTouchMode(true);
////            holder.task_Linear.requestFocusFromTouch();
//                holder.task_Linear.setBackgroundResource(R.mipmap.linear_task_selected);
//            } else {
//                holder.task_Linear.setBackgroundResource(R.mipmap.linear_task_unselected);
//            }
//        }else{
//        }
        if (filterList.get(position).getMissionId().equals(currentMissionId)) {
            holder.task_Linear.setBackgroundResource(R.mipmap.linear_task_selected);
        }else {
            holder.task_Linear.setBackgroundResource(R.mipmap.linear_task_unselected);
        }
        Long time=filterList.get(position).getCreatedTime();
        if(time.toString().length()==10)
            time=time*1000;
        String date = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(time));
        holder.task_title.setText(filterList.get(position).getMissionName());
        holder.task_time.setText(date);
        holder.task_description.setText(filterList.get(position).getDescription());
        holder.task_Linear.setOnClickListener(new MyTaskClick(position, holder));
    }

    //设置点击事件

    //总共多少个项
    @Override
    public int getItemCount() {
        return filterList.size();
    }
    //初始化布局信息
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView task_title;
        TextView task_time;
        TextView task_description;
        LinearLayout task_Linear;
        public MyViewHolder(View itemView) {
            super(itemView);
            task_title = (TextView) itemView.findViewById(R.id.task_title);
            task_time = (TextView) itemView.findViewById(R.id.task_time);
            task_description = (TextView) itemView.findViewById(R.id.task_description);
            task_Linear = (LinearLayout) itemView.findViewById(R.id.task_linear);
        }
    }

    //添加

    //删除


    class MyTaskClick implements View.OnClickListener{

        int position;
        MyViewHolder holder;
        public MyTaskClick(int position,MyViewHolder holder){
            this.position = position;
            this.holder = holder;
        }

        @Override
        public void onClick(View v) {
//            if(holder.task_Linear.hasFocus()) {
//                sharedPreferences.set("MissionId", filterList.get(position).getMissionId());
//                Intent intent =new Intent(mContext, MainActivity.class);
//                mContext.startActivity(intent);
//                mContext.finish();
//            }
            if(currentMissionId == filterList.get(position).getMissionId()) {
                sharedPreferences.set("MissionId", filterList.get(position).getMissionId());
                sharedPreferences.set("MissionName",filterList.get(position).getMissionName());
                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
                mContext.finish();
            } else {
                currentMissionId = filterList.get(position).getMissionId();
                notifyDataSetChanged();
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
                    List<TaskBean> mfilterList = new ArrayList<>();
                    for (TaskBean taskBean : list) {
                        //这里根据需求，添加匹配规则
                        if (taskBean.missionName.contains(charString)|| taskBean.description.contains(charString)||
                                new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(taskBean.createdTime*1000)).contains(charString)) {
                            mfilterList.add(taskBean);
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
                filterList = (ArrayList<TaskBean>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
