package com.sinovatio.iesi.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinovatio.iesi.BaseActivity;
import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.R;
import com.sinovatio.iesi.contract.TargetInfoQueryContract;
import com.sinovatio.iesi.model.adb.UserInfo;
import com.sinovatio.iesi.model.entity.TargetBean;
import com.sinovatio.iesi.presenter.TargetInfoQueryPresenter;
import com.sinovatio.iesi.view.adapter.TargetInfoQuery_Adapter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TargetInfoQueryActivity extends BaseActivity<TargetInfoQueryPresenter> implements TargetInfoQueryContract.View,TargetInfoQuery_Adapter.BindTargetClick {


    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.et_targetinfofilter)
    EditText etTargetinfofilter;
    @BindView(R.id.rv_target_info)
    RecyclerView rvTargetInfo;
    @BindView(R.id.tv_nullTarget)
    TextView tvNullTarget;

    private TargetInfoQueryContract.Presenter mPresenter;
    private TargetInfoQuery_Adapter adapter;
    private List<TargetBean> targetList = new ArrayList<>();

    UserInfo userInfo;
    String missionId = "";//任务id
    String account = "";
    String queryType = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new TargetInfoQueryPresenter(this);
        titleTv.setText("目标信息查询");
        ButterKnife.bind(this);

        userInfo = LitePal.findFirst(UserInfo.class);//获取用户信息
        if(userInfo!=null) {
            SharedPreferences sh = context.getSharedPreferences("taskId", 0);//获取任务信息
            missionId = sh.getString("MissionId", "0");
            account = userInfo.getAccount();
            mPresenter = new TargetInfoQueryPresenter(this);
            mPresenter.getTargetUser(account,missionId,queryType);//获取设备种类列表
            //设置为垂直的样式  ---目标信息内容的recyclerView
            rvTargetInfo.setLayoutManager(new LinearLayoutManager(this));
            //设置适配器
            rvTargetInfo.setAdapter(adapter = new TargetInfoQuery_Adapter(this, targetList,this));
            //添加自定义的分割线
            DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
            rvTargetInfo.addItemDecoration(divider);

            etTargetinfofilter.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_target_info_query;
    }

    private String targetId;
    private String name;
    private String identityCode;
    private String refFlag;
    private String bdType;

    @Override
    public void bindTargetClick(int position,List<TargetBean> filterlist) {
        targetId = filterlist.get(position).getTargetId();
        name = filterlist.get(position).getName();
        identityCode = filterlist.get(position).getIdentityCode();
        refFlag = filterlist.get(position).getRefFlag();
        if(refFlag.equals("0")){
            bdType = "1";
        }else{
            bdType = "0";
        }
        mPresenter.refTargetUser(account,missionId,targetId,name,identityCode,bdType);
    }

    @Override
    public void onSuccess(List<TargetBean> list) {
        targetList.clear();
        targetList.addAll(list);
        if(targetList != null && targetList.size()>0){
            tvNullTarget.setVisibility(View.GONE);
            }
        else{
            tvNullTarget.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onbdTypeSuccess(BaseArrayBean<Object> bean) {
        Toast.makeText(this, "绑定/解绑成功", Toast.LENGTH_SHORT).show();
        mPresenter.getTargetUser(account,missionId,queryType);
    }

    @Override
    public void onError(Throwable throwable) {
        Toast.makeText(this, "查询/绑定失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        dialog.show();
    }

    @Override
    public void hideLoading() {
        dialog.dismiss();
    }

    @OnClick(R.id.title_back)
    public void onClick(View v) {
        finish();
    }
}
