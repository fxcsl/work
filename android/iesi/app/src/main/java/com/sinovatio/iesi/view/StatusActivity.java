package com.sinovatio.iesi.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.sinovatio.iesi.BaseActivity;
import com.sinovatio.iesi.R;
import com.sinovatio.iesi.contract.StatusContract;
import com.sinovatio.iesi.model.adb.UserInfo;
import com.sinovatio.iesi.model.entity.StatusBean;
import com.sinovatio.iesi.presenter.StatusPresenter;
import com.sinovatio.iesi.tools.ToastUtil;
import com.sinovatio.iesi.view.adapter.StatusListAdapter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StatusActivity extends BaseActivity<StatusPresenter> implements StatusContract.View, StatusListAdapter.CheckBoxChangeed {
    @BindView(R.id.top_view)
    View topView;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_null)
    TextView tvNull;

    StatusListAdapter statusListAdapter;
    StatusPresenter presenter;
    UserInfo userInfo;
    String missionId;
    List<StatusBean> list = new ArrayList<>();
    String dc_value = "";

    public void windowColor() {
        Window window = getWindow();
        //取消设置Window半透明的Flag
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏为透明
        window.setStatusBarColor(getResources().getColor(R.color.color_4c686969));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        windowColor();
        presenter = new StatusPresenter(this);

        SharedPreferences sh = context.getSharedPreferences("taskId", 0);//获取任务信息
        missionId = sh.getString("MissionId", "0");
        userInfo = LitePal.findFirst(UserInfo.class);
        if (userInfo != null) {
            presenter.getWorkingStateDic();
        }

        statusListAdapter = new StatusListAdapter(context, list, this);
        lv.setAdapter(statusListAdapter);
    }

    @OnClick({R.id.top_view, R.id.tv_exit, R.id.tv_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_view:
            case R.id.tv_exit:
                finishs();
                break;
            case R.id.tv_ok:
                if (!dc_value.isEmpty()) {
                    presenter.changeGroupWorkingState(userInfo.getAccount(), missionId, dc_value);
                } else {
                    ToastUtil.toast("请选择有效状态");
                }
                break;
        }
    }


//    @OnItemClick(R.id.lv)
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//
//    }

    @Override
    public void updateStateSuccess(String message) {
        ToastUtil.toast(message);
        finishs();
    }

    @Override
    public void getStateDicSuccess(List<StatusBean> list_) {
        if(list_.size()==0){
            tvNull.setVisibility(View.VISIBLE);
        }else {
            tvNull.setVisibility(View.GONE);
            list.clear();
            list.addAll(list_);
            statusListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void httpError(int code,String message) {
        if(code==2){
            tvNull.setVisibility(View.VISIBLE);
        }
        ToastUtil.toast(message);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishs();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void finishs() {
        finish();
        overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_bottom_out);
    }

    @Override
    public void checkBoxChange(int pos) {
        dc_value = list.get(pos).getDicValue();
        changeListValue(pos);
        statusListAdapter.notifyDataSetChanged();
    }

    public void changeListValue(int pos) {
        for (int i = 0; i < list.size(); i++) {
            if (i == pos) {
                list.get(i).setMeans("-1");
            } else {
                list.get(i).setMeans("0");
            }
        }
    }
}
