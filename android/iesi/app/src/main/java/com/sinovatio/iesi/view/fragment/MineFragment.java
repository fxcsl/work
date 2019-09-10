package com.sinovatio.iesi.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.BaseFragment;
import com.sinovatio.iesi.R;
import com.sinovatio.iesi.contract.LogoutContract;
import com.sinovatio.iesi.model.adb.UserInfo;
import com.sinovatio.iesi.model.entity.TargetBean;
import com.sinovatio.iesi.presenter.LogoutPresenter;
import com.sinovatio.iesi.tools.DialogUtil;
import com.sinovatio.iesi.tools.ToastUtil;
import com.sinovatio.iesi.view.EquipmentInforActivity;
import com.sinovatio.iesi.view.LoginActivity;
import com.sinovatio.iesi.view.StatusActivity;
import com.sinovatio.iesi.view.TaskActivity;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 个人中心
 */
public class MineFragment extends BaseFragment<LogoutContract.Presenter> implements LogoutContract.View {

    Unbinder unbinder;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.tv_collection)
    TextView tvCollection;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.tv_mytask)
    TextView tvMytask;
    @BindView(R.id.tv_statusinformation)
    TextView tvStatusinformation;
    @BindView(R.id.tv_equipmentstatus)
    TextView tvEquipmentstatus;
    @BindView(R.id.tv_quit)
    TextView tvQuit;
    @BindView(R.id.bt_name)
    Button btName;
    @BindView(R.id.tv_missionname)
    TextView tvMissionname;
    @BindView(R.id.iv_bindimage)
    ImageView ivBindimage;
    @BindView(R.id.tv_bindname)
    TextView tvBindname;
    @BindView(R.id.iv_state)
    TextView ivState;
    @BindView(R.id.tv_state)
    TextView tvState;

    Activity mContext;

    private LogoutContract.Presenter mPresenter;
    String account = LitePal.findFirst(UserInfo.class).getAccount();
    String missionId = "";
    String missionName = "";
    String queryType = "0";
    List<TargetBean> targetList = new ArrayList<>();

    @Override
    public void onQuerySuccess(List<TargetBean> list) {
        targetList.clear();
        targetList.addAll(list);
        if (targetList != null && targetList.size() == 1) {
            ivBindimage.setBackgroundResource(R.mipmap.bind);
            tvBindname.setText("已绑定目标人：" + targetList.get(0).getName());
        } else {
            ivBindimage.setBackgroundResource(R.mipmap.unbind);
            tvBindname.setText("未绑定目标人");
        }
    }

    @Override
    public void onSuccess(BaseArrayBean<Object> bean) {
        ToastUtil.toast("退出登录成功！");
    }

    @Override
    public void onError(Throwable throwable) {
        ToastUtil.toast("退出登录失败！");
    }

    @Override
    public void onWorkingState( int isLeader,String workingState) {
        if(isLeader==0){//组长
            tvStatusinformation.setClickable(true);
            tvState.setText(workingState);
            tvState.setTextColor(0xff1FC101);
            ivState.setTextColor(0xff1FC101);
        }else {
            tvStatusinformation.setClickable(false);
            tvState.setText("无状态");
            tvState.setTextColor(0xffcccccc);
            ivState.setTextColor(0xffcccccc);
        }
    }

    @Override
    public void showLoading() {
//        dialog.show();
    }

    @Override
    public void hideLoading() {
//        dialog.dismiss();
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        UserInfo userInfo = LitePal.findFirst(UserInfo.class);
        SharedPreferences sh = mContext.getSharedPreferences("taskId", 0);//获取任务信息
        missionId = sh.getString("MissionId", "0");
        missionName = sh.getString("MissionName", "");
        tvName.setText(userInfo.getName());//查询
        tvDepartment.setText(userInfo.getDepartment());//查询
        tvMissionname.setText(missionName);
        if (userInfo.getName().length() > 2) {
            btName.setText(userInfo.getName().substring(userInfo.getName().length() - 2, userInfo.getName().length()));
        } else if (userInfo.getName().length() == 2) {
            btName.setText(userInfo.getName().substring(userInfo.getName().length() - 1, userInfo.getName().length()));
        } else {
            btName.setText(userInfo.getName());
        }
        mPresenter = new LogoutPresenter(this);
        mPresenter.getTargetUser(account, missionId, queryType);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sh = mContext.getSharedPreferences("taskId", 0);//获取任务信息
        missionName = sh.getString("MissionName", "");
        tvMissionname.setText(missionName);
        mPresenter.getTargetUser(account, missionId, queryType);
        mPresenter.getWorkingState(account,missionId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_name, R.id.tv_department, R.id.tv_collection, R.id.tv_setting, R.id.tv_about, R.id.tv_mytask, R.id.tv_statusinformation, R.id.tv_equipmentstatus, R.id.tv_quit, R.id.bt_name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_name:
                break;
            case R.id.tv_department:
                break;
            case R.id.tv_collection:
                break;
            case R.id.tv_setting:
                break;
            case R.id.tv_about:
                break;
            case R.id.tv_mytask:
                Intent intentTask = new Intent(mContext, TaskActivity.class);
                intentTask.putExtra("type", "0");
                startActivity(intentTask);
                break;
            case R.id.tv_statusinformation:
                Intent intent_state = new Intent(mContext, StatusActivity.class);
                startActivity(intent_state);
                getActivity().overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_bottom_out);//activity 开启结束动画
                break;
            case R.id.tv_equipmentstatus:
                Intent intent = new Intent(mContext, EquipmentInforActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_quit:
                DialogUtil.showAlertDialog(getActivity(), "退出", "你确定要退出吗？",
                        "取消", "确定", true, new DialogUtil.AlertDialogBtnClickListener() {
                            @Override
                            public void clickPositive() {
                                //positive
                                mPresenter.logout(account);
                                LitePal.deleteAll(UserInfo.class);
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }

                            @Override
                            public void clickNegative() {
                                //negative
                            }
                        });
                break;
            case R.id.bt_name:
                break;

        }
    }

}
