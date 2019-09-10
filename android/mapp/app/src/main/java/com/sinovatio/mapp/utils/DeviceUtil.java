package com.sinovatio.mapp.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.BatteryManager;

import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import android.os.StatFs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Locale;

import java.util.List;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

import static android.content.Context.BATTERY_SERVICE;

public class DeviceUtil {

    /**
     * 获取设备宽度（px）
     *
     * @param context
     * @return
     */
    public static int deviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取设备高度（px）
     *
     * @param context
     * @return
     */
    public static int deviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 返回版本号
     * 对应build.gradle中的versionCode
     *
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        String versionCode = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = String.valueOf(packInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取设备的唯一标识IMEI，deviceId
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ContextCompat.checkSelfPermission(context,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //没有权限则返回""
            return "";
        } else {
            String deviceId = tm.getDeviceId();
            if (deviceId == null) {
                return "N/A";
            } else {
                return deviceId;
            }
        }
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机Android SDK_INT
     *
     * @return
     */
    public static String getBuildLevel() {
        return Integer.toString(android.os.Build.VERSION.SDK_INT);
    }

    /**
     * 获取手机Android RELEASE
     *
     * @return
     */
    public static String getBuildVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机Android RELEASE
     *
     * @return
     */
    public static String getBootLoader() {
        return Build.BOOTLOADER;
    }

    /**
     * 获取手机JAVA_VM
     *
     * @return
     */
    public static String getJavaVM() {
        String vm = null;
        vm = System.getProperty("java.vm.name");
        if (vm != null) {
            vm = vm + System.getProperty("java.vm.version");
        }
        return vm;
    }

    /**
     * 获取手机OS_VERSION
     *
     * @return
     */
    public static String getOSVerrsion() {
        String os = null;
        os = System.getProperty("os.version");
        return os;
    }

    /**
     * 获取手机OS Architecture
     *
     * @return
     */
    public static String OSArchitecture() {
        String os1 = null;
        os1 = System.getProperty("os.arch");
        return os1;
    }

    /**
     * 获取手机Android 版本名称
     *
     * @return
     */
    public static String getBuildVersionName() {
        String release = android.os.Build.VERSION.RELEASE;
        char i = release.charAt(0);
        String name = "N/A";
        switch (i) {
            case '1': name = "Q";break;
            case '9': name = "P";break;
            case '8': name = "O";break;
            case '7': name = "N";break;
            case '6': name = "M";break;
            case '5': name = "L";break;
            default: name = "N/A";
        }
        return name;
    }

    /**
     * @ 获取当前手机屏幕的尺寸(单位:像素)
     *  @param context
     */
    public static String getSCreenSize(Context context) {
        int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        float density = context.getResources().getDisplayMetrics().density;
        float xdpi = context.getResources().getDisplayMetrics().xdpi;
        float ydpi = context.getResources().getDisplayMetrics().ydpi;
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;

        // 这样可以计算屏幕的物理尺寸
        float width2 = (width / xdpi)*(width / xdpi);
        float height2 = (height / ydpi)*(height / ydpi);
        int inch = (int) (Math.sqrt(width2+height2)*10);

        return Float.toString( (float)inch/10);
    }

    /**
     * @ 获取当前手机像素密度
     *  @param context
     */
    public static String getDensity(Context context) {
        float size = Float.parseFloat(getSCreenSize(context));
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        return Integer.toString((int)(Math.sqrt(width * width + height * height) / size));
    }

    /**
     * @ 获取基带信息
     */
    public static String getBaseband(){
        String Version = "";
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[] { String.class,String.class });
            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
            Version = (String)result;
        } catch (Exception e) {
        }
        return Version;
    }

    /**
     * @ 获取序列号IMEI
     */
    public static String getDeviceSN(){
        String serialNumber = android.os.Build.SERIAL;
        return serialNumber;
    }

    /**
     * @ 获取可用内存
     */
    public static String getFreeMem(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Activity.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(info);
        NumberFormat formatter = new DecimalFormat("0.00");
        String fm = formatter.format((float) info.availMem/(1024*1024*1024));
        return fm;
    }

    /**
     * @ 获取总内存
     */
    public static String getTotalMem(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Activity.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(info);
        NumberFormat formatter = new DecimalFormat("0.00");
        String fm = formatter.format((float) info.totalMem/(1024*1024*1024));
        return fm;
    }

    /**
     * @ 获取存储信息
     */
    public static String getStorage(Context context) {

        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memInfo);

        final StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long tcounts = statFs.getBlockCount();//总共的block数
        long counts = statFs.getAvailableBlocks() ; //获取可用的block数
        long size = statFs.getBlockSize(); //每格所占的大小
        long availROMSize = counts * size;//可用内部存储大小
        long totalROMSize = tcounts *size;
        NumberFormat formatter = new DecimalFormat("0.00");
        String sto1 = formatter.format((float) availROMSize/(1024*1024*1024));
        String sto2 = formatter.format((float) totalROMSize/(1024*1024*1024));
        return sto1+"/"+sto2+"GB";
    }

    /**
     * @ 获取ABI
     */
    public static String getABI() {
        return Arrays.toString(Build.SUPPORTED_ABIS);
    }

    /**
     * @ 获取主板信息
     */
    public static String getBoard() {
        return Build.BOARD;
    }

    /**
     * @ 获取硬件信息
     */

     public static String getHardWare() {
     return Build.HARDWARE;
     }

     /**
     * @ 获取电量百分比
     */
    public static String getBatteryPercent(Context context) {
        BatteryManager manager = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        return Integer.toString(manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY))+"%";
    }

    /**
     * @ 获取电池状态
     */
    public static String getBatteryState(Context context) {
        BatteryManager manager = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        String state = "N/A";
        switch (manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)){
            case 5:
                state = "已充满";
                break;
            case 4:
                state = "未充电";
                break;
            case 3:
                state = "放电中";
                break;
            case 2:
                state = "充电中";
                break;
            case 1:
                state = "未知";
        }
        return state;
    }

    /**
     * @ 获取电池总电量 mAh
     */
    public static String getBatteryInfo(Context context) {
        Object mPowerProfile;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(context);

            batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(batteryCapacity + " mAh");
    }

    /**
     * 获取手机mac地址
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        String mac = "02:00:00:00:00:00";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mac = getMacDefault(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mac = getMacAddress();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mac = getMacFromHardware();
        }
        return mac;
    }

    /**
     * Android  6.0 之前（不包括6.0）
     * 必须的权限  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     * @param context
     * @return
     */
    private static String getMacDefault(Context context) {
        String mac = "02:00:00:00:00:00";
        if (context == null) {
            return mac;
        }

        WifiManager wifi = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) {
            return mac;
        }
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {
        }
        if (info == null) {
            return null;
        }
        mac = info.getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
        }
        return mac;
    }

    /**
     * Android 6.0（包括） - Android 7.0（不包括）
     * @return
     */
    private static String getMacAddress() {
        String WifiAddress = "02:00:00:00:00:00";
        try {
            WifiAddress = new BufferedReader(new FileReader(new File("/sys/class/net/wlan0/address"))).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return WifiAddress;
    }

    /**
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET" />
     * @return
     */
    private static String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }


    /**
     * 获取设备ID
     */
    public static String getID() {
        return Build.ID;
    }

    /**
     * 获取设备产品名
     */
    public static String getProduct() {
        return Build.PRODUCT;
    }

    /**
     * 获取设备名
     */
    public static String getDevice() {
        return Build.DEVICE;
    }

    /**
     * 获取设备DISPLAY
     */
    public static String getDisplay() {
        return Build.DISPLAY;
    }
}
