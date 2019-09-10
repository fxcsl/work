package com.sinovatio.mapp.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.maning.updatelibrary.InstallUtils;
import com.sinovatio.mapp.R;
import com.sinovatio.mapp.base.BaseActivity;
import com.sinovatio.mapp.base.MyApplication;
import com.sinovatio.mapp.bean.BaseObjectBean;
import com.sinovatio.mapp.bean.VersionBean;
import com.sinovatio.mapp.constant.Constants;
import com.sinovatio.mapp.entity.DeviceFactoryEntity;
import com.sinovatio.mapp.entity.TabEntity;
import com.sinovatio.mapp.greendao.DaoSession;
import com.sinovatio.mapp.model.db.DeviceFactoryUtils;
import com.sinovatio.mapp.net.RetrofitClient;
import com.sinovatio.mapp.net.RxScheduler;
import com.sinovatio.mapp.service.UploadInfoService;
import com.sinovatio.mapp.utils.AppUtils;
import com.sinovatio.mapp.utils.FileConvert;
import com.sinovatio.mapp.utils.ViewFindUtils;
import com.sinovatio.mapp.view.fragment.DeviceFragment;
import com.sinovatio.mapp.view.fragment.LocationFragment;
import com.sinovatio.mapp.view.fragment.Slot1Fragment;
import com.sinovatio.mapp.view.fragment.WifiFragment;

import org.xutils.common.util.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class MonitorActivity extends BaseActivity {
    private Context mContext = this;

    private Activity context = this;

    // 权限请求码
    private static final int PERMISSION_REQUEST_CODE = 10;
    // 危险权限需要动态申请
    private static final String[] NEEDED_PERMISSIONS = new String[]
            {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_SETTINGS
            };

    private boolean mHasPermission;


    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private String[] mTitles = {"卡槽1", "WiFi", "位置", "设备"};
    private int[] mIconUnselectIds = {
            R.mipmap.slot_1,
            R.mipmap.wifi,
            R.mipmap.location,
            R.mipmap.device};
    private int[] mIconSelectIds = {
            R.mipmap.slot_1_selected,
            R.mipmap.wifi_selected,
            R.mipmap.location_selected,
            R.mipmap.device_selected};

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private View mDecorView;
    private ViewPager mViewPager;
    private CommonTabLayout mTabLayout;

    private UploadInfoService.UploadInfoBinder uploadInfoBinder;

    @BindView(R.id.cbox_refresh)
    public CheckBox cbox_refresh;

    //是否实时刷新
    private boolean refreshFlag = true;

    public boolean getRefreshFlag() {
        return refreshFlag;
    }

    public void setRefreshFlag(boolean refreshFlag) {
        this.refreshFlag = refreshFlag;
    }


    public Intent uploadInfoIntent;
    //是否需要检查更新
    private boolean isNeedheckAPKVersion = true;
    //处理下载相关
    private InstallUtils.DownloadCallBack downloadCallBack;
    private String apkDownloadPath;


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            uploadInfoBinder = (UploadInfoService.UploadInfoBinder) service;
            uploadInfoBinder.setRefreshFlag(refreshFlag);
            //uploadInfoBinder.initActivity(MonitorActivity.this);


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("MonitorActivity onCreate!");
        if (savedInstanceState != null) {
            refreshFlag = savedInstanceState.getBoolean("refreshFlag");
        }

    }

    long exitTime;
    //按两次返回
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出系统！", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                //System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.d("MonitorActivity onSaveInstanceState!");
        outState.putBoolean("refreshFlag", refreshFlag);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        outState.putBoolean("refreshFlag", refreshFlag);
        LogUtil.d("MonitorActivity onRestoreInstanceState!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d("MonitorActivity onResume!");

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_monitor;
    }

    @Override
    public void initView() {
        if (uploadInfoIntent == null) {
            uploadInfoIntent = new Intent(this, UploadInfoService.class);
        }
        bindService(uploadInfoIntent,connection,BIND_AUTO_CREATE);
        //开启服务
        startService(uploadInfoIntent);
        initRefresh();
        initMonitorData();
        //检查更新
        if(isNeedheckAPKVersion){
            initCallBack();
            checkAPKVersion();
        }
    }

//    @Override
//    public void refreshView() {
//        initMonitorData();
//    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        //关闭服务
        //stopService(uploadInfoIntent);
        LogUtil.d("MonitorActivity onDestory!!");
    }

    public void initRefresh() {

        cbox_refresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                uploadInfoBinder.setRefreshFlag(isChecked);
                Intent intent =new Intent(); //向service发送广播
                intent.setAction("com.sinovatio.mapp.service.intents");
                intent.putExtra("refreshFlag",isChecked);
                mContext.sendBroadcast(intent);
                setRefreshFlag(isChecked);
            }
        });
        cbox_refresh.setChecked(refreshFlag);
    }


    public void initMonitorData() {

        mTabEntities.clear();
        mFragments.clear();
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        mFragments.add(Slot1Fragment.getInstance("Slot1"));
        mFragments.add(WifiFragment.getInstance("Wifi"));
        mFragments.add(LocationFragment.getInstance("Location"));
        mFragments.add(DeviceFragment.getInstance("Device"));

        mDecorView = getWindow().getDecorView();
        mViewPager = ViewFindUtils.find(mDecorView, R.id.vPager);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        mTabLayout = ViewFindUtils.find(mDecorView, R.id.public_header_tl);
        mTabLayout.setTabData(mTabEntities);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(0);
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasAllPermission = true;
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i : grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED) {
                    hasAllPermission = false;
                    break;
                }
            }
            if (hasAllPermission) {
                initMonitorData();
            } else {
                mHasPermission = false;
                Toast.makeText(mContext, "获取权限失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 检查apk更新情况
     */
    public void checkAPKVersion(){

        RetrofitClient.getInstance().getApi().checkAPKVersion()
                .compose(RxScheduler.<BaseObjectBean<VersionBean>>Flo_io_main())
                .subscribe(new Consumer<BaseObjectBean<VersionBean>>() {
                    @Override
                    public void accept(BaseObjectBean<VersionBean> bean) throws Exception {
                        if(null!=bean){
                        VersionBean version =  bean.getResult();
                        int versionCode = version.getVersionCode();
                        if(versionCode>AppUtils.getVersionCode(mContext)) {
                            //显示content，下载dialog界面
                            LogUtil.d("发现新版本，版本号为：" + versionCode);
                            buildNewVersionDialog(version);
                        }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.d("error!"+throwable);
                    }
                });

    }


    private void buildNewVersionDialog(VersionBean version) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("发现新版本")
                .setIcon(R.mipmap.ic_mapp)
                .setMessage(version.getContent())
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String url = Constants.PRE_SERVER_URL+"download/"+Constants.APP_NAME
                                +"-v"+version.getVersionName()+".apk";
                        downLoadAPK(url);
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        dialog.show();
    }

    private void downLoadAPK(String url){
        LogUtil.d("下载地址："+url);
        InstallUtils.with(this)
                //必须-下载地址
                .setApkUrl(url)
                //非必须-下载保存的文件的完整路径+name.apk
                .setApkPath(Constants.APK_SAVE_PATH)
                //非必须-下载回调
                .setCallBack(downloadCallBack)
                //开始下载
                .startDownload();


    }


    private void initCallBack() {
        downloadCallBack = new InstallUtils.DownloadCallBack() {
            @Override
            public void onStart() {
//                Log.i(TAG, "InstallUtils---onStart");
//                tv_progress.setText("0%");
//                tv_info.setText("正在下载...");
//                btnDownload.setClickable(false);
//                btnDownload.setBackgroundResource(R.color.colorGray);
            }

            @Override
            public void onComplete(String path) {
//                Log.i(TAG, "InstallUtils---onComplete:" + path);
                apkDownloadPath = path;
//                tv_progress.setText("100%");
//                tv_info.setText("下载成功");
//                btnDownload.setClickable(true);
//                btnDownload.setBackgroundResource(R.color.colorPrimary);
//
//                //先判断有没有安装权限
                InstallUtils.checkInstallPermission(context, new InstallUtils.InstallPermissionCallBack() {
                    @Override
                    public void onGranted() {
                        //去安装APK
                        installApk(apkDownloadPath);
                    }

                    @Override
                    public void onDenied() {
                        //弹出弹框提醒用户
                        AlertDialog alertDialog = new AlertDialog.Builder(context)
                                .setTitle("温馨提示")
                                .setMessage("必须授权才能安装APK，请设置允许安装")
                                .setNegativeButton("取消", null)
                                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //打开设置页面
                                        InstallUtils.openInstallPermissionSetting(context, new InstallUtils.InstallPermissionCallBack() {
                                            @Override
                                            public void onGranted() {
                                                //去安装APK
                                                installApk(apkDownloadPath);
                                            }

                                            @Override
                                            public void onDenied() {
                                                //还是不允许咋搞？
                                                Toast.makeText(context, "不允许安装咋搞？强制更新就退出应用程序吧！", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                })
                                .create();
                        alertDialog.show();
                    }
                });
            }

            @Override
            public void onLoading(long total, long current) {
//                //内部做了处理，onLoading 进度转回progress必须是+1，防止频率过快
//                Log.i(TAG, "InstallUtils----onLoading:-----total:" + total + ",current:" + current);
//                int progress = (int) (current * 100 / total);
//                tv_progress.setText(progress + "%");
            }

            @Override
            public void onFail(Exception e) {
//                Log.i(TAG, "InstallUtils---onFail:" + e.getMessage());
//                tv_info.setText("下载失败:" + e.toString());
//                btnDownload.setClickable(true);
//                btnDownload.setBackgroundResource(R.color.colorPrimary);
            }

            @Override
            public void cancle() {
//                Log.i(TAG, "InstallUtils---cancle");
//                tv_info.setText("下载取消");
//                btnDownload.setClickable(true);
//                btnDownload.setBackgroundResource(R.color.colorPrimary);

            }
        };
    }

    private void installApk(String path) {
        InstallUtils.installAPK(context, path, new InstallUtils.InstallCallBack() {
            @Override
            public void onSuccess() {
                //onSuccess：表示系统的安装界面被打开
                //防止用户取消安装，在这里可以关闭当前应用，以免出现安装被取消
                Toast.makeText(mContext, "正在安装程序", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(Exception e) {
                LogUtil.d("安装失败:" + e.toString());
            }
        });
    }






    public void initDataToDb() {
        boolean exist = new File("/data/data/com.sinovatio.mapp/databases/mapp.db")
                .exists();
        DaoSession daoSession = ((MyApplication) getApplication()).getDaoSession();
        if (exist) {
            String factory = DeviceFactoryUtils.getFactoryByMac(daoSession, "74AC5F");
            if (factory != null && factory != "") {
                LogUtil.d("已初始化！！");
                return;
            }
            LogUtil.d("factory:" + factory);
        }
        FileConvert.writeFile();
        FileReader fr = null;
        try {
            fr = new FileReader(new File("/data/data/com.sinovatio.mapp/new_oui.txt"));
            BufferedReader br = new BufferedReader(fr);
            String str;
            String mac;
            DeviceFactoryEntity dfe;
            while ((str = br.readLine()) != null) {
                if (str.equals("") || str.length() < 7) {
                    continue;
                }
                mac = str.substring(0, 6);
                if (FileConvert.regexTest(mac)) {//检查某一行的前六位是否是mac，如果是则进行抽取
                    dfe = new DeviceFactoryEntity();
                    dfe.setThreeByteMac(mac);
                    dfe.setFactory(str.substring(7));
                    daoSession.insert(dfe);
                    LogUtil.d(mac);
                }
            }
            br.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
