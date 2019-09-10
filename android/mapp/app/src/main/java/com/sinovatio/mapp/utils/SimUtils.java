package com.sinovatio.mapp.utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;



public class SimUtils {

    public static String getICCIDInfo(String ICCID, String operatorName) {

        String ICCIDInfo = operatorName + " ";
        switch (operatorName) {
            case "MOBILE":
            case "中国移动":
                ICCIDInfo += " 20" + ICCID.substring(10, 12);
                break;
            case "UNICOM":
            case "中国联通":
                ICCIDInfo += " 20" + ICCID.substring(6, 8);
                break;
            case "TELECOM":
            case "CHN-CT":
            case "中国电信":
                ICCIDInfo += " 20" + ICCID.substring(6, 8);
                break;
            default:
                ICCIDInfo = "";
        }
        return ICCIDInfo;
    }

    public static int getNetworkClassByType(TelephonyManager tel) {
        int type = 0;
        type = tel.getNetworkType();
        switch (type) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
            case 16:
                return 2;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
            case 17:
                return 3;
            case 13:
                return 4;
            default:
                return 0;
        }
    }


    /**
     * 获取数据网类型
     * /**
     * * GPRS    2G(2.5) General Packet Radia Service 114kbps  1
     * * EDGE    2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps  2
     * * UMTS    3G WCDMA 联通3G Universal MOBILE Telecommunication System 完整的3G移动通信技术标准  3
     * * CDMA    2G 电信 Code Division Multiple Access 码分多址  4
     * * EVDO_0  3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G  5
     * * EVDO_A  3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G   6
     * * 1xRTT   2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,  7
     * * HSDPA   3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps  8
     * * HSUPA   3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps  9
     * * HSPA    3G (分HSDPA,HSUPA) High Speed Packet Access       10
     * * IDEN    2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）  11
     * * EVDO_B  3G EV-DO Rev.B 14.7Mbps 下行 3.5G                                              12
     * * LTE     4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G  13
     * * EHRPD   3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级  14
     * * HSPAP   3G HSPAP 比 HSDPA 快些 15
     * *GSM     16
     * * TD_SCDMA  17
     * *IWLAN      18
     */
    public static String getNetworkType(TelephonyManager tel) {
        String networkType = ""; //数据网
//        String netType =""; //小区类型
        int type = 0;
        type = tel.getNetworkType();
        switch (type) {
            case 0:
                break;
            case 1:
                networkType = "GPRS";
                break;
            case 2:
                networkType = "EDGE";
                break;
            case 3:
                networkType = "UMTS";
                break;
            case 4:
                networkType = "CDMA";
                break;
            case 5:
                networkType = "EVDO_0";
                break;
            case 6:
                networkType = "EVDO_A";
                break;
            case 7:
                networkType = "1xRTT";
                break;
            case 8:
                networkType = "HSDPA";
                break;
            case 9:
                networkType = "HSUPA";
                break;
            case 10:
                networkType = "HSPA";
                break;
            case 11:
                networkType = "IDEN";
                break;
            case 12:
                networkType = "EVDO_B";
                break;
            case 13:
                networkType = "LTE";
                break;
            case 14:
                networkType = "EHRPD";
                break;
            case 15:
                networkType = "HSPAP";
                break;
            case 16:
                networkType = "GSM";
                break;
            case 17:
                networkType = "TD-SCDMA";
                break;
            case 18:
                networkType = "IWLAN";
                break;
            default:
                break;
        }
        return networkType;
    }

    public static List<String> getNetworkFrequency(CellInfo cellInfo) {
        List<String> a = new ArrayList<String>();
        if (cellInfo instanceof CellInfoLte) {
            List<Integer> FDDbandPool = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28));
            List<Integer> TDDbandPool = new ArrayList<Integer>(Arrays.asList(33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44));
            int frequency = 0;
            int band = 0;
            String BAND = null;
            String EARFCN = "0";
            if (Build.VERSION.SDK_INT > 23) {
                EARFCN = Integer.toString(((CellInfoLte) cellInfo).getCellIdentity().getEarfcn());
            }
            if (EARFCN != "" && EARFCN != null) {
                int i=0;
                try {
                     i = Integer.parseInt(EARFCN);
                }catch (Exception e){

                }

                if (0 <= i && i < 600) {
                    frequency = (int) (2110 + 0.1 * (i));
                    band = 1;
                }
                if (600 <= i && i < 1200) {
                    frequency = (int) (1930 + 0.1 * (i - 600));
                    band = 2;
                }
                if (1200 <= i && i < 1950) {
                    frequency = (int) (1805 + 0.1 * (i - 1200));
                    band = 3;
                }
                if (1950 <= i && i < 2400) {
                    frequency = (int) (2110 + 0.1 * (i - 1950));
                    band = 4;
                }
                if (2400 <= i && i < 2650) {
                    frequency = (int) (869 + 0.1 * (i - 2400));
                    band = 5;
                }
                if (2650 <= i && i < 2750) {
                    frequency = (int) (875 + 0.1 * (i - 2650));
                    band = 6;
                }
                if (2750 <= i && i < 3450) {
                    frequency = (int) (2620 + 0.1 * (i - 2750));
                    band = 7;
                }
                if (3450 <= i && i < 3800) {
                    frequency = (int) (925 + 0.1 * (i - 3450));
                    band = 8;
                }
                if (3800 <= i && i < 4150) {
                    frequency = (int) (1844.9 + 0.1 * (i - 3800));
                    band = 9;
                }
                if (4150 <= i && i < 4750) {
                    frequency = (int) (2110 + 0.1 * (i - 4150));
                    band = 10;
                }
                if (4750 <= i && i < 4950) {
                    frequency = (int) (1475.9 + 0.1 * (i - 4750));
                    band = 11;
                }
                if (5010 <= i && i < 5180) {
                    frequency = (int) (729 + 0.1 * (i - 5010));
                    band = 12;
                }
                if (5180 <= i && i < 5280) {
                    frequency = (int) (746 + 0.1 * (i - 5180));
                    band = 13;
                }
                if (5280 <= i && i < 5380) {
                    frequency = (int) (758 + 0.1 * (i - 5280));
                    band = 14;
                }
                if (5730 <= i && i < 5850) {
                    frequency = (int) (734 + 0.1 * (i - 5730));
                    band = 17;
                }
                if (5850 <= i && i < 6000) {
                    frequency = (int) (860 + 0.1 * (i - 5850));
                    band = 18;
                }
                if (6000 <= i && i < 6150) {
                    frequency = (int) (875 + 0.1 * (i - 6000));
                    band = 19;
                }
                if (6150 <= i && i < 6450) {
                    frequency = (int) (791 + 0.1 * (i - 6150));
                    band = 20;
                }
                if (6450 <= i && i < 6600) {
                    frequency = (int) (1495.9 + 0.1 * (i - 6450));
                    band = 21;
                }
                if (6600 <= i && i < 7400) {
                    frequency = (int) (3510 + 0.1 * (i - 6600));
                    band = 22;
                }
                if (7500 <= i && i < 7700) {
                    frequency = (int) (2180 + 0.1 * (i - 7500));
                    band = 23;
                }
                if (7700 <= i && i < 8040) {
                    frequency = (int) (1525 + 0.1 * (i - 7700));
                    band = 24;
                }
                if (8040 <= i && i < 8690) {
                    frequency = (int) (1930 + 0.1 * (i - 8040));
                    band = 25;
                }
                if (8690 <= i && i < 9040) {
                    frequency = (int) (859 + 0.1 * (i - 8690));
                    band = 26;
                }
                if (9040 <= i && i < 9209) {
                    frequency = (int) (852 + 0.1 * (i - 9040));
                    band = 27;
                }
                if (9210 <= i && i < 9660) {
                    frequency = (int) (758 + 0.1 * (i - 9210));
                    band = 28;
                }
                if (36000 <= i && i < 36200) {
                    frequency = (int) (1900 + 0.1 * (i - 36000));
                    band = 33;
                }
                if (36200 <= i && i < 36350) {
                    frequency = (int) (2010 + 0.1 * (i - 36200));
                    band = 34;
                }
                if (36350 <= i && i < 36950) {
                    frequency = (int) (1850 + 0.1 * (i - 36350));
                    band = 35;
                }
                if (36950 <= i && i < 37550) {
                    frequency = (int) (1930 + 0.1 * (i - 36950));
                    band = 36;
                }
                if (37550 <= i && i < 38250) {
                    frequency = (int) (2570 + 0.1 * (i - 37550));
                    band = 37;
                }
                if (38250 <= i && i < 38650) {
                    frequency = (int) (1880 + 0.1 * (i - 38250));
                    band = 38;
                }
                if (38650 <= i && i < 39650) {
                    frequency = (int) (2300 + 0.1 * (i - 38650));
                    band = 39;
                }
                if (39650 <= i && i < 41590) {
                    frequency = (int) (2496 + 0.1 * (i - 41590));
                    band = 40;
                }
                if (41590 <= i && i < 43590) {
                    frequency = (int) (3400 + 0.1 * (i - 41590));
                    band = 41;
                }
                if (43590 <= i && i < 45590) {
                    frequency = (int) (3600 + 0.1 * (i - 43590));
                    band = 42;
                }
                if (45590 <= i && i < 46590) {
                    frequency = (int) (703 + 0.1 * (i - 45590));
                    band = 43;
                }
            }
            if (FDDbandPool.contains(band)) {
                BAND = "FDD";
            } else if (TDDbandPool.contains(band)) {
                BAND = "TDD";
            }
            a.add(Integer.toString(frequency));
            a.add("" + band);
            a.add("(" + BAND + ")");
            return a;
        }
        return a;
    }
}
