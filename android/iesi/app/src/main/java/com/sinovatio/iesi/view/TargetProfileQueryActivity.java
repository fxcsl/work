package com.sinovatio.iesi.view;

import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.sinovatio.iesi.R;
import com.sinovatio.iesi.contract.TargetProfileQueryContract;
import com.sinovatio.iesi.model.adb.UserInfo;
import com.sinovatio.iesi.model.entity.ProfileBean;
import com.sinovatio.iesi.presenter.TargetProfileQueryPresenter;
import com.sinovatio.iesi.view.adapter.TargetProfileQuery_Adapter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TargetProfileQueryActivity extends BaseActivity<TargetProfileQueryPresenter> implements TargetProfileQueryContract.View, TargetProfileQuery_Adapter.TargetDetailClick  {
    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.et_targetprofilefilter)
    EditText etTargetprofilefilter;
    @BindView(R.id.rv_target_profile)
    RecyclerView rvTargetProfile;
    @BindView(R.id.tv_nullTarget)
    TextView tvNullTarget;

    private TargetProfileQueryContract.Presenter mPresenter;
    private TargetProfileQuery_Adapter adapter;
    private List<ProfileBean> targetList = new ArrayList<>();

    UserInfo userInfo;
    String missionId = "";//任务id
    String account = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new TargetProfileQueryPresenter(this);
        titleTv.setText("目标档案查询");
        ButterKnife.bind(this);

        userInfo = LitePal.findFirst(UserInfo.class);//获取用户信息
        if(userInfo!=null) {
            SharedPreferences sh = context.getSharedPreferences("taskId", 0);//获取任务信息
            missionId = sh.getString("MissionId", "0");
            account = userInfo.getAccount();
            mPresenter = new TargetProfileQueryPresenter(this);
            mPresenter.getTargetList(account,missionId);//获取目标档案列表
            //设置为垂直的样式  ---目标信息内容的recyclerView
            rvTargetProfile.setLayoutManager(new LinearLayoutManager(this));
            //设置适配器
            rvTargetProfile.setAdapter(adapter = new TargetProfileQuery_Adapter(this, targetList,this));
            //添加自定义的分割线
            DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
            rvTargetProfile.addItemDecoration(divider);

            etTargetprofilefilter.addTextChangedListener(new TextWatcher() {
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
        return R.layout.activity_target_profile_query;
    }

    @Override
    public void onSuccess(List<ProfileBean> list) {
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
    public void onError(Throwable throwable) {
        Toast.makeText(this, "档案信息查询失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        dialog.show();
    }

    @Override
    public void hideLoading() {
        dialog.dismiss();
    }

    @Override
    public void targetDetailClick(int position, List<ProfileBean> filterlist) {

    }

    @OnClick(R.id.title_back)
    public void onClick(View v) {
        finish();
    }
}
