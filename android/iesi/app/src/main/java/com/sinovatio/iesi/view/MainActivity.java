package com.sinovatio.iesi.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sinovatio.iesi.BaseActivity;
import com.sinovatio.iesi.BaseBean;
import com.sinovatio.iesi.BaseFragment;
import com.sinovatio.iesi.R;
import com.sinovatio.iesi.contract.MainContract;
import com.sinovatio.iesi.model.adb.UserInfo;
import com.sinovatio.iesi.model.entity.VersionBean;
import com.sinovatio.iesi.presenter.MainPresenter;
import com.sinovatio.iesi.service.LocationService;
import com.sinovatio.iesi.view.adapter.TabFragmentPagerAdapter;
import com.sinovatio.iesi.view.fragment.ContactsFragment;
import com.sinovatio.iesi.view.fragment.ConversationFragment;
import com.sinovatio.iesi.view.fragment.FunctionFragment;
import com.sinovatio.iesi.view.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.myViewPager)
    ViewPager myViewPager;
    @BindView(R.id.tv_item_one)
    TextView tvItemOne;
    @BindView(R.id.tv_item_two)
    TextView tvItemTwo;
    @BindView(R.id.tv_item_three)
    TextView tvItemThree;
    @BindView(R.id.tv_item_four)
    TextView tvItemFour;

    @BindViews({R.id.tv_item_one,R.id.tv_item_two,R.id.tv_item_three,R.id.tv_item_four})
    TextView[] tvs;
    private MainContract.Presenter mPresenter;

    List<BaseFragment> list;
    TabFragmentPagerAdapter adapter;

    private int[] mIconUnselectIds = {
            R.mipmap.news_unselected,
            R.mipmap.contact_unselected,
            R.mipmap.function_unselected,
            R.mipmap.mine_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.news_selected,
            R.mipmap.contact_selected,
            R.mipmap.function_selected,
            R.mipmap.mine_select};

    Intent serviceIntent;

    private LocationService mainService;
    //绑定Service监听
    ServiceConnection sconnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("activity---->", "已断开Service");
        }
        /**当绑定时执行*/
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            Log.i("activity---->", "已绑定到Service");
            mainService = ((LocationService.MyBinder)service).getService();
            Intent i = new Intent();
            mainService.onStartCommand(i, 0, 0);    //绑定成功后可以调用Service方法
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceIntent = new Intent(this, LocationService.class);
        bindService(serviceIntent, sconnection, Context.BIND_AUTO_CREATE);
        Window window = getWindow();
        //After LOLLIPOP not translucent status bar
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //Then call setStatusBarColor.
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#FF108EE9"));

        mPresenter=new MainPresenter(this);//绑定Presenter

        myViewPager.setOnPageChangeListener(new MyPagerChangeListener()); //把Fragment添加到List集合里面
        list = new ArrayList<>();
        list.add(new ConversationFragment());
        list.add(new ContactsFragment());
        list.add(new FunctionFragment());
        list.add(new MineFragment());
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list);
        myViewPager.setAdapter(adapter);
        myViewPager.setCurrentItem(3);  //初始化显示第一个页面
        setMyViewPager(3);//初始化tob
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void success(UserInfo u) {
        //tv.setText(u.getNames() + " " + u.getIntroduction());
    }

    @Override
    public void HttpSuccess(BaseBean<VersionBean> m) {
    }

    @Override
    public void fail(String s) {
    }

    @OnClick({R.id.tv_item_one, R.id.tv_item_two, R.id.tv_item_three, R.id.tv_item_four})
    public void onClick(View view) {
        int type=0;
        switch (view.getId()) {
            case R.id.tv_item_one:
                type=0;
                break;
            case R.id.tv_item_two:
                type=1;
                break;
            case R.id.tv_item_three:
                type=2;
                break;
            case R.id.tv_item_four:
                type=3;
                break;
        }
        myViewPager.setCurrentItem(type,false);//跳转指定页面，取消滑动效果
        setMyViewPager(type);//初始化tob
    }

    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            setMyViewPager(arg0);//初始化tob
        }
    }

    private void setMyViewPager(int arg0) {

        myViewPager.setCurrentItem(arg0,false);
        Drawable drawable = null;
        for(int i=0;i<4;i++) {
            if(i == arg0) {
                drawable = this.getDrawable(mIconSelectIds[arg0]);
                tvs[i].setTextColor(Color.parseColor("#108EE9"));
            }
            else {
                drawable = this.getDrawable(mIconUnselectIds[i]);
                tvs[i].setTextColor(Color.parseColor("#999999"));
            }
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvs[i].setCompoundDrawables(null, drawable, null, null);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(sconnection);

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
}

