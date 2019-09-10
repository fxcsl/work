package com.sinovatio.iesi.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.sinovatio.iesi.BaseActivity;
import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.Myapplication;
import com.sinovatio.iesi.R;
import com.sinovatio.iesi.tools.SharedPreferencesHelper;
import com.sinovatio.iesi.view.adapter.RecyclerViewAdapter;
import com.sinovatio.iesi.contract.TaskContract;
import com.sinovatio.iesi.model.adb.UserInfo;
import com.sinovatio.iesi.model.entity.TaskBean;
import com.sinovatio.iesi.presenter.TaskPresenter;
import com.sinovatio.iesi.tools.ToastUtil;
import com.sinovatio.iesi.view.adapter.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TaskActivity extends BaseActivity<TaskPresenter> implements TaskContract.View {
    @BindView(R.id.et_taskfilter)
    EditText etTaskfilter;
    @BindView(R.id.rv_task)
    RecyclerView rvTask;
    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.tv_nullTask)
    TextView tvNullTask;

    private TaskContract.Presenter mPresenter;
    private RecyclerViewAdapter adapter;
    private List<TaskBean> list = new ArrayList<>();

    String type="0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new TaskPresenter(this);
        titleTv.setText("我的任务");

        String account = LitePal.findFirst(UserInfo.class).getAccount();
        Integer missionState = 1;
        Log.i("httpss",account);
        mPresenter.getTaskList(account, missionState);
        type=getIntent().getStringExtra("type");
        //设置为垂直的样式  ---硬件信息内容的recyclerView
        rvTask.setLayoutManager(new LinearLayoutManager(this));
        //设置适配器
        rvTask.setAdapter(adapter = new RecyclerViewAdapter(this, list));
        etTaskfilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                if (s.length() > 0) {
//                    searchClear.setVisibility(View.VISIBLE);
//                } else {
//                    searchClear.setVisibility(View.INVISIBLE);
//                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_task;
    }

    @Override
    public void onSuccess(BaseArrayBean<Object> bean) {
        String code = bean.getCode();
        if ("200".equals(code)) {
            List listTask = bean.getResult();
            if(listTask != null && listTask.size()>0){
                tvNullTask.setVisibility(View.GONE);
                list.clear();
                for(int i = 0; i < listTask.size(); i++) {
                    LinkedTreeMap<String,Object> tmTask = (LinkedTreeMap<String,Object>)bean.getResult().get(i);
                    TaskBean taskBean = new TaskBean();
                    taskBean.setMissionId((String) tmTask.get("missionId"));
                    taskBean.setMissionName((String) tmTask.get("missionName"));
                    taskBean.setDescription((String) tmTask.get("description"));
                    taskBean.setCreatedTime((Long.parseLong((String) tmTask.get("createdTime"))));
//                    taskBean.setIsCaptain((String) tmTask.get("isCaptain"));
                    list.add(taskBean);
                }
            }else{
              tvNullTask.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
        } else {//服务端返回登陆错误
            Toast.makeText(this, bean.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        Toast.makeText(this, "任务查询失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        dialog.show();
    }

    @Override
    public void hideLoading() {
        dialog.dismiss();
    }

    long exitTime;
    /**
     * 按两次返回
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            keyDown();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.title_back)
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                keyDown();
                break;
        }
    }

    public void keyDown(){
        if(type.equals("1")) {
            Intent intent =new Intent(context,LoginActivity.class);
            intent.putExtra("status","2");
            startActivity(intent);
        }
        finish();
    }
}
