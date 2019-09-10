package com.sinovatio.iesi.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinovatio.iesi.BaseActivity;
import com.sinovatio.iesi.R;
import com.sinovatio.iesi.contract.TargetArchivesInfoContract;
import com.sinovatio.iesi.http.HttpConfig;
import com.sinovatio.iesi.model.adb.UserInfo;
import com.sinovatio.iesi.model.entity.TargetArchivesInfo_entity;
import com.sinovatio.iesi.presenter.TargetArchivesInfoPresenter;
import com.sinovatio.iesi.surfaceview.XCRoundImageView;
import com.sinovatio.iesi.tools.GlideUtil;
import com.sinovatio.iesi.tools.ToastUtil;

import org.litepal.LitePal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 目标档案详情
 * 07-17 zjp
 */
public class TargetArchivesInfoActivity extends BaseActivity<TargetArchivesInfoContract.Presenter> implements TargetArchivesInfoContract.View {

    @BindView(R.id.head_pic)
    ImageView headPic;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_case_num)
    TextView tvCaseNum;
    @BindView(R.id.tv_old_name)
    TextView tvOldName;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @BindView(R.id.tv_license_num)
    TextView tvLicenseNum;
    @BindView(R.id.tv_before_case)
    TextView tvBeforeCase;
    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_tv)
    TextView titleTv;

    TargetArchivesInfoContract.Presenter presenter;
    UserInfo userInfo;
    String missionId;
    @Override
    public int getLayoutId() {
        return R.layout.activity_target_archives_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        titleTv.setText("目标档案详情");
        userInfo = LitePal.findFirst(UserInfo.class);//获取用户信息
        presenter=new TargetArchivesInfoPresenter(this);
        if(userInfo!=null) {
//            SharedPreferences sh = context.getSharedPreferences("taskId", 0);//获取任务信息
//            missionId = sh.getString("MissionId", "0");
            presenter.getCaseInfo(userInfo.getAccount(),getIntent().getStringExtra("id"));
        }
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
    public void onClick(View v ) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
        }
    }

    @Override
    public void getCaseInfoSuccess(TargetArchivesInfo_entity entity) {
        if(entity!=null){
            GlideUtil.viewHttpPic_activity(this, HttpConfig.BASE_URL+entity.getHeadPic(),headPic,GlideUtil.options);
            tvUserName.setText(entity.getName());
            tvCaseNum.setText("案件号："+entity.getCaseCode());
            tvOldName.setText(entity.getOldname());
            tvNickname.setText(entity.getNickName());
            tvPhoneNum.setText(entity.getPhone());
            tvLicenseNum.setText(entity.getLicense());
            tvBeforeCase.setText(entity.getBeforeCase());
        }
    }

    @Override
    public void getError(String info) {
        ToastUtil.toast(info);
    }
}
