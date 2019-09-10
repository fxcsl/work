package com.sinovatio.iesi.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.sinovatio.iesi.BaseActivity;
import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.R;
import com.sinovatio.iesi.contract.LoginContract;
import com.sinovatio.iesi.model.adb.UserInfo;
import com.sinovatio.iesi.presenter.LoginPresenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View  {

    private LoginContract.Presenter mPresenter;

    @BindView(R.id.et_username)
    public EditText et_username;

    @BindView(R.id.et_password)
    public EditText et_password;

    @BindView(R.id.cb_rempass)
    public CheckBox cb_rempass;

    @BindView(R.id.tv_login)
    public TextView tv_login;

    @BindView(R.id.tv_message)
    public TextView tv_message;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new LoginPresenter(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        setTransparentForWindow(this);
        String status = getIntent().getStringExtra("status");
        UserInfo us = LitePal.findFirst(UserInfo.class);
        if(us != null) {
            et_username.setText(us.getAccount());
            et_password.setText(us.getPassword());
        }
        if("1".equals(status)){//在记住密码的情况下,wellcome登陆失败
            showMessage("系统登陆异常!");
            //Toast.makeText(this, "系统登陆异常!", Toast.LENGTH_SHORT).show();
        }
    }

    public  void setTransparentForWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @OnClick(R.id.tv_login)
    public void onViewClicked() {
        String username = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("帐号或密码不能为空!");
            //Toast.makeText(this, "帐号密码不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        mPresenter.login(username, password);
    }

    @Override
    public void onSuccess(BaseArrayBean<Object> bean){
        String code = bean.getCode();
        if("200".equals(code)){
            LinkedTreeMap<String,String> user = (LinkedTreeMap<String,String>)bean.getResult().get(0);
            String account ="";
            String name="";
            String department="";
            account = user.get("account");
            name = user.get("name");
            department = user.get("department");


            boolean rempass = cb_rempass.isChecked();
            boolean result = true;
            String password ="";
            if(rempass){
                password = et_password.getText().toString().trim();
            }
            result = mPresenter.remPass(account,password,name,department);

            if(result){
                Intent intent =new Intent(this,TaskActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
                finish();
            }else{//保存数据异常
                showMessage("系统异常!");
                //Toast.makeText(this, "系统异常！", Toast.LENGTH_SHORT).show();
            }

        }else{//服务端返回登陆错误
            showMessage(bean.getMessage());
            //Toast.makeText(this, bean.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Log.d("dddddd", new Gson().toJson(bean));
    }

    @Override
    public void onError(Throwable throwable) {
        Log.d("dddddd", throwable.toString());
        showMessage("系统登陆异常!");
        //Toast.makeText(this, "系统登陆异常!", Toast.LENGTH_SHORT).show();
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
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出系统！", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showLoading() {
        dialog.show();
    }

    @Override
    public void hideLoading() {
        dialog.dismiss();
    }

    public void showMessage(String msg){
        tv_message.setText(msg);
        tv_message.setVisibility(View.VISIBLE);
    }

    public void hideMessage(){
        tv_message.setVisibility(View.GONE);
    }
}
