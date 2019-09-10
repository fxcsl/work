package com.sinovatio.iesi.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sinovatio.iesi.BaseActivity;
import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.R;
import com.sinovatio.iesi.contract.LoginContract;
import com.sinovatio.iesi.model.adb.UserInfo;
import com.sinovatio.iesi.presenter.LoginPresenter;
import com.sinovatio.iesi.tools.SharedPreferencesHelper;

import org.litepal.LitePal;

public class WellcomeActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    private LoginContract.Presenter mPresenter;

    private Context context;
    private String currentMissionId = "";

    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int PERMISSON_REQUESTCODE = 0;

    public void showTakePermission(Activity activity, Context context) {//        判断手机版本,如果低于6.0 则不用申请权限,直接
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//7.0及以上
            ActivityCompat.requestPermissions(activity, needPermissions, PERMISSON_REQUESTCODE);//获取多条权限
        } else {
            //读取数据库中数据，自动登陆进入系统
            beforeLogin();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wellcome;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        mPresenter = new LoginPresenter(this);
        if (isPermission(context, needPermissions)) {//权限全部通过
            beforeLogin();
        } else {
            showTakePermission(this, context);
        }

        SharedPreferencesHelper sharedPreferences = new SharedPreferencesHelper(context, "taskId");
//        sharedPreferences.set("MissionId", "null");
        if (null != sharedPreferences.getString("MissionId")) {
            currentMissionId = sharedPreferences.getString("MissionId");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (paramArrayOfInt[i] == PackageManager.PERMISSION_GRANTED) {
                    if (i == 0) {

                    }
                } else {
                    Toast.makeText(context, "必要权限申请失败！", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
            beforeLogin();
        }
    }

    //判断是否有指定权限
    public boolean isPermission(Context context, String[] perValue) {//        判断手机版本,如果低于6.0 则不用申请权限,直接拍照

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//7.0及以上
            for (int i = 0; i < perValue.length; i++) {

                if ((ContextCompat.checkSelfPermission(context, perValue[i]) != PackageManager.PERMISSION_GRANTED)) {
                    return false;
                }
            }
        }
        return true;
    }


    public void startNext() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * @return
     */
    public void beforeLogin() {
        UserInfo us = LitePal.findFirst(UserInfo.class);
        if (us != null) {//login
            mPresenter.login(us.getAccount(), us.getPassword());
        } else {
            startNext();
        }


    }

    @Override
    public void onSuccess(BaseArrayBean<Object> bean) {
        String code = bean.getCode();
        if ("200".equals(code)) {
            Intent intent = new Intent();
            intent.setClass(this, TaskActivity.class);
            intent.putExtra("type", "1");
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, bean.getMessage(), Toast.LENGTH_SHORT).show();
            startNext();
        }
//        Log.d("dddddd", new Gson().toJson(bean));
    }

    @Override
    public void onError(Throwable throwable) {
        //登陆异常，一般是网络问题
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("status", "1");
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoading() {
//        dialog.show();
    }

    @Override
    public void hideLoading() {
//        dialog.dismiss();
    }
}
