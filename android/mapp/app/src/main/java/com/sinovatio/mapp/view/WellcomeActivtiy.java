package com.sinovatio.mapp.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.sinovatio.mapp.R;
import com.sinovatio.mapp.base.BaseActivity;

public class WellcomeActivtiy extends BaseActivity {

    private Context context;

    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int PERMISSON_REQUESTCODE = 0;

    public void showTakePermission(Activity activity, Context context) {//        判断手机版本,如果低于6.0 则不用申请权限,直接
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity, needPermissions, PERMISSON_REQUESTCODE);//获取多条权限
        } else {
            startNext();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wellcome;
    }

    @Override
    public void initView() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        if(isPermission(context,needPermissions)){//权限全部通过
            startNext();
        }else {
            showTakePermission(this, context);
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
                    Toast.makeText(context,  "必要权限申请失败！", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
            startNext();
        }
    }

    //判断是否有指定权限
    public boolean isPermission( Context context, String[] perValue) {//        判断手机版本,如果低于6.0 则不用申请权限,直接拍照

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//7.0及以上
             for (int i = 0; i < perValue.length; i++) {

                if ((ContextCompat.checkSelfPermission(context, perValue[i]) != PackageManager.PERMISSION_GRANTED)) {
                    return false;
                }
            }
        }
        return true;
    }


    public void startNext(){
         new Thread(new Runnable() {
             @Override
             public void run() {
                 try {
                     Thread.sleep(1000);
                     Intent intent=new Intent(context,MonitorActivity.class);
                     startActivity(intent);
                     finish();
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
         }).start();
    }
}
