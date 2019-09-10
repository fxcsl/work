package com.sinovatio.mapp.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sinovatio.mapp.view.MonitorActivity;
import com.sinovatio.mapp.R;
import com.sinovatio.mapp.adapter.RecyclerViewAdapter;
import com.sinovatio.mapp.base.BaseLazyLoadFragment;
import com.sinovatio.mapp.broadcast.BatteryReceiver;
import com.sinovatio.mapp.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.os.BatteryManager.BATTERY_HEALTH_COLD;
import static android.os.BatteryManager.BATTERY_HEALTH_DEAD;
import static android.os.BatteryManager.BATTERY_HEALTH_GOOD;
import static android.os.BatteryManager.BATTERY_HEALTH_OVERHEAT;
import static android.os.BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE;
import static android.os.BatteryManager.BATTERY_HEALTH_UNKNOWN;
import static android.os.BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE;
import static android.os.BatteryManager.BATTERY_PLUGGED_AC;
import static android.os.BatteryManager.BATTERY_PLUGGED_USB;
import static android.os.BatteryManager.BATTERY_PLUGGED_WIRELESS;
import static android.os.BatteryManager.EXTRA_HEALTH;
import static android.os.BatteryManager.EXTRA_PLUGGED;
import static android.os.BatteryManager.EXTRA_TECHNOLOGY;
import static android.os.BatteryManager.EXTRA_TEMPERATURE;
import static android.os.BatteryManager.EXTRA_VOLTAGE;

public class DeviceFragment extends BaseLazyLoadFragment {

    @BindView(R.id.recycler_view)
    public RecyclerView recycler_view;
    @BindView(R.id.battery_view)
    public RecyclerView battery_view;
    @BindView(R.id.system_view)
    public RecyclerView system_view;

    private List<String> mTitle;
    private List<String> mText;
    private List<String> bTitle;
    private List<String> bText;
    private List<String> sTitle;
    private List<String> sText;
    private Context mContext;
    private RecyclerViewAdapter adapter;

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    private BatteryReceiver batteryBroad;

    public DeviceFragment() {
    }

    public static DeviceFragment getInstance(String param1) {
        DeviceFragment fragment = new DeviceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext.unregisterReceiver(batteryBroad);

    }

    @Override
    protected void initView(View view) {
        mContext = getActivity();
        BatteryReceiver.Handler handler = new DeviceFragmentHandler();
        batteryBroad = new BatteryReceiver(handler);
        IntentFilter  IntentFilter = new IntentFilter();
        IntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryIntent = mContext.registerReceiver(batteryBroad,IntentFilter);

        //三个不同的块内的数据赋值
        init_Device_Hardware();
        init_Device_Battery(batteryIntent);
        init_Device_System();

        //设置为垂直的样式  ---硬件信息内容的recyclerView
        recycler_view.setLayoutManager(new LinearLayoutManager(mContext));
        //设置适配器
        recycler_view.setAdapter(adapter=new RecyclerViewAdapter(mContext,mTitle,mText));

        //设置为垂直的样式   ---电池信息内容的recyclerView
        battery_view.setLayoutManager(new LinearLayoutManager(mContext));
        //设置适配器
        battery_view.setAdapter(adapter=new RecyclerViewAdapter(mContext,bTitle,bText));

        //设置为垂直的样式   ---系统信息内容的recyclerView
        system_view.setLayoutManager(new LinearLayoutManager(mContext));
        //设置适配器
        system_view.setAdapter(adapter=new RecyclerViewAdapter(mContext,sTitle,sText));
    }

    @Override
    protected void initData(){

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_device;
    }

    private void init_Device_Hardware() {
        mTitle = new ArrayList<>();
        mText = new ArrayList<>();

        mTitle.add("品牌");
        mText.add(DeviceUtil.getPhoneBrand());
        mTitle.add("型号");
        mText.add(DeviceUtil.getPhoneModel());
        mTitle.add("硬件");
        mText.add(DeviceUtil.getHardWare());
        mTitle.add("BOARD");
        mText.add(DeviceUtil.getBoard());
        mTitle.add("支持的ABI");
        mText.add(DeviceUtil.getABI());
        mTitle.add("ID");
        mText.add(DeviceUtil.getID());
        mTitle.add("DISPLAY");
        mText.add(Build.DISPLAY);
        mTitle.add("产品");
        mText.add(DeviceUtil.getProduct());
        mTitle.add("设备");
        mText.add(DeviceUtil.getDevice());
        mTitle.add("基带");
        mText.add(DeviceUtil.getBaseband());
        mTitle.add("序列");
        mText.add(DeviceUtil.getDeviceSN());
        mTitle.add("主屏尺寸");
        mText.add(DeviceUtil.getSCreenSize(getContext())+"英寸");
        mTitle.add("主屏分辨率");
        mText.add(Integer.toString(DeviceUtil.deviceHeight(getContext())) +" X "+ Integer.toString(DeviceUtil.deviceWidth(getContext())));
        mTitle.add("屏幕像素密度");
        mText.add(DeviceUtil.getDensity(getContext())+"dpi");
        mTitle.add("内存 可用/全部");
        mText.add(DeviceUtil.getFreeMem(getContext()) + "/" + DeviceUtil.getTotalMem(getContext())+"GB");
        mTitle.add("存储 可用/全部");
        mText.add(DeviceUtil.getStorage(getContext()));
    }

    private void init_Device_Battery(Intent batteryIntent) {
        bTitle = new ArrayList<>();
        bText = new ArrayList<>();

        bTitle.add("状态");
        bText.add(DeviceUtil.getBatteryState(mContext));
        bTitle.add("类型");
        bText.add(batteryIntent.getStringExtra(EXTRA_TECHNOLOGY));
        bTitle.add("健康");
        bText.add(getHealth(batteryIntent.getIntExtra(EXTRA_HEALTH, BATTERY_HEALTH_UNKNOWN)));
        bTitle.add("电源");
        bText.add(getPlugged(batteryIntent.getIntExtra(EXTRA_PLUGGED,-1)));
        bTitle.add("百分比");
        bText.add(DeviceUtil.getBatteryPercent(mContext));
        bTitle.add("电压");
        bText.add(String.format("%.3f V", batteryIntent.getIntExtra(EXTRA_VOLTAGE, -1) / 1000.0));
        bTitle.add("温度");
        bText.add(String.format("%.1f ℃", batteryIntent.getIntExtra(EXTRA_TEMPERATURE, -1) / 10.0));
        bTitle.add("电池容量");
        bText.add(DeviceUtil.getBatteryInfo(mContext));
    }

    public void init_Device_System() {
        sTitle = new ArrayList<>();
        sText = new ArrayList<>();

        sTitle.add("SDK_INT");
        sText.add(DeviceUtil.getBuildLevel());
        sTitle.add("RELEASE");
        sText.add(DeviceUtil.getBuildVersion());
        sTitle.add("VERSION NAME");
        sText.add(DeviceUtil.getBuildVersionName());
        sTitle.add("BOOTLOADER");
        sText.add(DeviceUtil.getBootLoader());
        sTitle.add("JAVA_VM");
        sText.add(DeviceUtil.getJavaVM());
        sTitle.add("OS ARCHITECTURE");
        sText.add(DeviceUtil.OSArchitecture());
        sTitle.add("OS VERSION");
        sText.add(DeviceUtil.getOSVerrsion());
        sTitle.add("RUNTIME VERSION");
        sText.add(DeviceUtil.getVersionCode(getContext()));
    }

    private String getHealth(int health) {
        String result = "未知状态";

        switch (health) {
            case BATTERY_HEALTH_UNKNOWN:
                break;
            case BATTERY_HEALTH_GOOD:
                result = "健康";
                break;
            case BATTERY_HEALTH_OVERHEAT:
                result = "过热";
                break;
            case BATTERY_HEALTH_DEAD:
                result = "损坏";
                break;
            case BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                result = "未明示故障";
                break;
            case BATTERY_HEALTH_OVER_VOLTAGE:
                result = "电压过大";
                break;
            case BATTERY_HEALTH_COLD:
                result = "过冷";
                break;
        }

        return result;
    }

    private String getPlugged(int plugged) {
        String result = "电池";

        switch (plugged) {
            case BATTERY_PLUGGED_AC:
                result = "电源";
                break;
            case BATTERY_PLUGGED_USB:
                result = "USB";
                break;
            case BATTERY_PLUGGED_WIRELESS:
                result = "无线";
                break;
        }

        return result;
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }


    /**
     * 广播handler
     */
    public class DeviceFragmentHandler implements BatteryReceiver.Handler{

        @Override
        public void onBatteryChanged(Intent batteryIntent) {
            MonitorActivity myActivity = (MonitorActivity ) getActivity();
            init_Device_Battery(batteryIntent);
            if (myActivity.getRefreshFlag()) {
                //设置为垂直的样式   ---电池信息内容的recyclerView
                battery_view.setLayoutManager(new LinearLayoutManager(mContext));
                //设置适配器
                battery_view.setAdapter(adapter = new RecyclerViewAdapter(mContext, bTitle, bText));
            }
        }
    }

}
