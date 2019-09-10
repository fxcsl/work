package com.sinovatio.mapp.view.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.style.LineStyle;
import com.bin.david.form.data.table.ArrayTableData;
import com.sinovatio.mapp.view.MonitorActivity;
import com.sinovatio.mapp.R;
import com.sinovatio.mapp.adapter.RecyclerViewAdapter;
import com.sinovatio.mapp.base.BaseLazyLoadFragment;
import com.sinovatio.mapp.overwrite.CustTextDrawFormat;
import com.sinovatio.mapp.utils.SimUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;

import static android.content.Context.TELEPHONY_SERVICE;

public class Slot1Fragment extends BaseLazyLoadFragment {
    public final static int REQUEST_READ_PHONE_STATE = 1;

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    public static int flag = 0;

    @BindView(R.id.network_operator_image)
    public ImageView network_operator_image;
    @BindView(R.id.card_slot_view)
    public RecyclerView card_slot_view;
    @BindView(R.id.operator_view)
    public RecyclerView operator_view;
    @BindView(R.id.service_area_view)
    public RecyclerView service_area_view;

    @BindView(R.id.table_NeiCellInfo)
    public SmartTable table_NeiCellInfo;
    Timer timer;
    Handler myhandler = new Handler();

    //定时器定时刷新数据
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            MonitorActivity myActivity = (MonitorActivity) getActivity();
            if (myActivity.getRefreshFlag()) {
                allDataInit();
            }
            myhandler.postDelayed(this, 5000);// 50ms后执行this，即runable
        }
    };

    //各个recycler中的数据列表
    //卡槽数据容器
    private List<String> Title1;
    private List<String> Text1;
    //网络运营商数据容器
    private List<String> Title2;
    private List<String> Text2;
    //服务小区数据容器
    private List<String> Title3;
    private List<String> Text3;

    private RecyclerViewAdapter adapter;

    public Slot1Fragment() {
    }

    public static Slot1Fragment getInstance(String param1) {
        Slot1Fragment fragment = new Slot1Fragment();
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
        myhandler.removeCallbacks(runnable);// 关闭定时器处理
//        LogUtil.d("Runable1 callback!");
    }

    @Override
    protected void initView(View view) {
        //初始化邻小区表格
        table_NeiCellInfo.getConfig()
                .setShowXSequence(false)
                .setShowYSequence(false)
                .setColumnTitleGridStyle(new LineStyle(mContext, 1, Color.parseColor("#2A56C6")))
                .setColumnTitleStyle(new FontStyle(mContext, 12, Color.parseColor("#2A56C6")))
                .setColumnTitleBackground(new BaseBackgroundFormat(Color.parseColor("#1F1D5CA0")))
                .setContentGridStyle(new LineStyle(mContext, 1, Color.parseColor("#2A56C6")))
                .setContentStyle(new FontStyle(mContext, 12, Color.parseColor("#2A56C6")))
                .setContentBackground(new BaseBackgroundFormat(Color.parseColor("#1F1D5CA0")));
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    @Override
    protected void initData() {
        allDataInit();
        myhandler.postDelayed(runnable, 5000);// 打开定时器，一定时间后执行runnable操作
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_slot;
    }

    public void init_Slot(TelephonyManager tel, List Title, List Text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (mContext.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

            }
        }
        if (tel.getDeviceId() != "" && tel.getDeviceId() != null) {
            Title.add("IMEI");
            Text.add(tel.getDeviceId());
        }
        if (tel.getSubscriberId() != "" && tel.getSubscriberId() != null) {
            Title.add("SIM IMSI");
            Text.add(tel.getSubscriberId());
        }
        if (tel.getSimSerialNumber() != "" && tel.getSimSerialNumber() != null) {
            Title.add("ICCID");
            Title.add("ICCID信息");
            Text.add(tel.getSimSerialNumber());
            Text.add(SimUtils.getICCIDInfo(tel.getSimSerialNumber(), tel.getNetworkOperatorName()));
        }
    }

    //对网络运营商数据赋值
    public void init_Network_Operators(TelephonyManager tel, List Title, List Text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (mContext.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

            }
        }
        if (tel.getNetworkOperatorName() != "" && tel.getNetworkOperatorName() != null) {
            Title.add("运营商");
            Text.add(tel.getNetworkOperatorName());
        }
        if (tel.getNetworkOperator() != "" && tel.getNetworkOperator() != null) {
            Title.add("MCC");
            Text.add(tel.getNetworkOperator().substring(0, 3));
            Title.add("MNC");
            Text.add(tel.getNetworkOperator().substring(3, 5));
        }
        String operatorName = tel.getNetworkOperatorName();
        switch (operatorName) {
            case "UNICOM":
            case "中国联通": {
                network_operator_image.setVisibility(View.VISIBLE);
                network_operator_image.setImageResource(R.mipmap.operator_unicom);
                break;
            }
            case "MOBILE":
            case "中国移动": {
                network_operator_image.setVisibility(View.VISIBLE);
                network_operator_image.setImageResource(R.mipmap.operator_mobile);
                break;
            }
            case "TELECOM":
            case "CHN-CT":
            case "中国电信": {
                network_operator_image.setVisibility(View.VISIBLE);
                network_operator_image.setImageResource(R.mipmap.operator_telecom);
                break;
            }
            default:
        }

    }

    //对服务小区数据赋值和邻小区赋值
    public void init_Service_Area(TelephonyManager tel, List Title, List Text) {
        Object[][] data = new Object[0][];
        String BAND = null;
        String EARFCN = null;
        String PCI = null;
        String RSSI = null;
        String RSSP = null;
        String RSSQ = null;
        String LAC = null;
        String CI = null;
        String ARFCN = null;
        String BSIC = null;
        String PSC = null;
        String UARFCN = null;
        int i = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (mContext.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

            }
        }
        if (SimUtils.getNetworkType(tel) == "") {
            return;
        }
        List<CellInfo> cellInfoList = tel.getAllCellInfo();
        if (null == cellInfoList || cellInfoList.size() == 0) {
            return;
        }
        int register = 0;
        for (CellInfo cellInfo : cellInfoList) {
            if(cellInfo.isRegistered()){
                register++;
            }
        }
        data = new Object[cellInfoList.size() - register][];
        for (CellInfo cellInfo : cellInfoList) {
            if (cellInfo.isRegistered()) {
                Title.clear();
                Text.clear();
                Title.add("数据网类型");
                Text.add(SimUtils.getNetworkType(tel));
                if ((cellInfo instanceof CellInfoLte)) {
                    Title.add("小区类型");
                    Text.add("LTE");
                    Title.add("TAC");
                    Text.add("" + ((CellInfoLte) cellInfo).getCellIdentity().getTac());
                    Title.add("PCI");
                    Text.add("" + ((CellInfoLte) cellInfo).getCellIdentity().getPci());
                    Title.add("ECI");
                    Text.add("" + ((CellInfoLte) cellInfo).getCellIdentity().getCi());
                    if (Build.VERSION.SDK_INT > 23) {
                        Title.add("EARFCN");
                        Text.add("" + ((CellInfoLte) cellInfo).getCellIdentity().getEarfcn());
                        Title.add("FREQUENCY");
                        Text.add((SimUtils.getNetworkFrequency(cellInfo).get(0)) + "MHz");
                        Title.add("BAND");
                        Text.add((SimUtils.getNetworkFrequency(cellInfo).get(1))
                                + (SimUtils.getNetworkFrequency(cellInfo).get(2)));
                    }
                } else if ((cellInfo instanceof CellInfoGsm)) {
                    Title.add("小区类型");
                    Text.add("GSM");
                    Title.add("LAC");
                    Text.add("" + ((CellInfoGsm) cellInfo).getCellIdentity().getLac());
                    Title.add("CI");
                    Text.add("" + ((CellInfoGsm) cellInfo).getCellIdentity().getCid());
                    if (Build.VERSION.SDK_INT > 23) {
                        Title.add("ARFCN");
                        Text.add("" + ((CellInfoGsm) cellInfo).getCellIdentity().getArfcn());
                        CellSignalStrengthGsm a = ((CellInfoGsm) cellInfo).getCellSignalStrength();
                    }
                } else if ((cellInfo instanceof CellInfoCdma)) {
                    Title.add("小区类型");
                    Text.add("CDMA");
                    Title.add("LAC");//CDMA网络编号NID
                    Text.add("" + ((CellInfoCdma) cellInfo).getCellIdentity().getNetworkId());
                    Title.add("CI");//基站识别号BID
                    Text.add("" + ((CellInfoCdma) cellInfo).getCellIdentity().getBasestationId());
                } else if ((cellInfo instanceof CellInfoWcdma)) {
                    Title.add("小区类型");
                    Text.add("WCDMA");
                    Title.add("LAC");
                    Text.add("" + ((CellInfoWcdma) cellInfo).getCellIdentity().getLac());
                    Title.add("CI");
                    Text.add("" + ((CellInfoWcdma) cellInfo).getCellIdentity().getCid());
                    Title.add("PSC");
                    Text.add("" + ((CellInfoWcdma) cellInfo).getCellIdentity().getPsc());
                    if (Build.VERSION.SDK_INT > 23) {
                        Title.add("UARFCN");
                        Text.add("" + ((CellInfoWcdma) cellInfo).getCellIdentity().getUarfcn());
                    }
                }
            } else {
                if (cellInfo instanceof CellInfoLte) {
                    if (Build.VERSION.SDK_INT > 23) {
                        BAND = (SimUtils.getNetworkFrequency(cellInfo).get(1));
                        EARFCN = Integer.toString(((CellInfoLte) cellInfo).getCellIdentity().getEarfcn());
                    } else {
                        BAND = "--";
                        EARFCN = "--";
                    }
                    PCI = ("" + ((CellInfoLte) cellInfo).getCellIdentity().getPci());
                    RSSI = Integer.toString(((CellInfoLte) cellInfo).getCellSignalStrength().getDbm());
                    if (Build.VERSION.SDK_INT > 25) {
                        RSSP = Integer.toString(((CellInfoLte) cellInfo).getCellSignalStrength().getRsrp());
                        RSSQ = Integer.toString(((CellInfoLte) cellInfo).getCellSignalStrength().getRsrq());
                    } else {
                        RSSP = "--";
                        RSSQ = "--";
                    }
                    Object[] m = {BAND, EARFCN, PCI, RSSI, RSSP, RSSQ};
                    data[i] = m;
                    i++;
                } else if (cellInfo instanceof CellInfoGsm) {
                    LAC = Integer.toString(((CellInfoGsm) cellInfo).getCellIdentity().getLac());
                    CI = Integer.toString(((CellInfoGsm) cellInfo).getCellIdentity().getCid());
                    if (Build.VERSION.SDK_INT > 23) {
                        ARFCN = Integer.toString(((CellInfoGsm) cellInfo).getCellIdentity().getArfcn());
                        BSIC = Integer.toString(((CellInfoGsm) cellInfo).getCellIdentity().getBsic());
                    } else {
                        ARFCN = "--";
                        BSIC = "--";
                    }
                    Object[] m = {LAC, CI, ARFCN, BSIC};
                    data[i] = m;
                    i++;
                } else if (cellInfo instanceof CellInfoWcdma) {
                    LAC = Integer.toString(((CellInfoWcdma) cellInfo).getCellIdentity().getLac());
                    CI = Integer.toString(((CellInfoWcdma) cellInfo).getCellIdentity().getCid());
                    PSC = Integer.toString(((CellInfoWcdma) cellInfo).getCellIdentity().getPsc());
                    RSSI = Integer.toString(((CellInfoWcdma) cellInfo).getCellSignalStrength().getDbm());
                    if (Build.VERSION.SDK_INT > 23) {
                        UARFCN = Integer.toString(((CellInfoWcdma) cellInfo).getCellIdentity().getUarfcn());
                    } else {
                        UARFCN = "--";
                    }
                    Object[] m = {LAC, CI, PSC, RSSI, UARFCN};
                    data[i] = m;
                    i++;
                }
                else if ((cellInfo instanceof CellInfoWcdma)) {

                }
            }
        }
        if (data.length > 0 && data[0].length == 6) {
            Object[][] data_transformed = ArrayTableData.transformColumnArray(data);
            ArrayTableData tabledata = ArrayTableData.create("",
                    new String[]{"BAND", "EARFCN", "PCI", "RSSI", "RSRP", "RSRQ"},
                    data_transformed,
                    new CustTextDrawFormat(mContext, 35));

            table_NeiCellInfo.setTableData(tabledata);
            table_NeiCellInfo.setVisibility(View.VISIBLE);

        } else if (data.length > 0 && data[0].length == 4) {
            Object[][] data_transformed = ArrayTableData.transformColumnArray(data);
            ArrayTableData tabledata = ArrayTableData.create("",
                    new String[]{"LAC", "CI", "ARFCN", "BSIC"},
                    data_transformed,
                    new CustTextDrawFormat(mContext, 80));

            table_NeiCellInfo.setTableData(tabledata);
            table_NeiCellInfo.setVisibility(View.VISIBLE);
        } else if (data.length > 0 && data[0].length == 5) {
            Object[][] data_transformed = ArrayTableData.transformColumnArray(data);
            ArrayTableData tabledata = ArrayTableData.create("",
                    new String[]{"LAC", "CI", "PSC", "RSSI", "UARFCN"},
                    data_transformed,
                    new CustTextDrawFormat(mContext, 40));

            table_NeiCellInfo.setTableData(tabledata);
            table_NeiCellInfo.setVisibility(View.VISIBLE);
        } else {
            table_NeiCellInfo.setVisibility(View.GONE);
        }

    }

    //加载所有数据
    public void allDataInit() {
        Title1 = new ArrayList<>();
        Text1 = new ArrayList<>();
        Title2 = new ArrayList<>();
        Text2 = new ArrayList<>();
        Title3 = new ArrayList<>();
        Text3 = new ArrayList<>();

        TelephonyManager tel = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
        //对卡槽数据赋值
        init_Slot(tel, Title1, Text1);
        //对网络运营商数据赋值
        init_Network_Operators(tel, Title2, Text2);
        //对服务小区数据赋值
        init_Service_Area(tel, Title3, Text3);

        if (Title1.size() > 0) {
            //设置为垂直的样式  ----卡槽模块
            card_slot_view.setLayoutManager(new LinearLayoutManager(mContext));
            //设置适配器
            card_slot_view.setAdapter(adapter = new RecyclerViewAdapter(mContext, Title1, Text1));
        }

        if (Title2.size() > 0) {
            //设置为垂直的样式  -----网络运营商模块
            operator_view.setLayoutManager(new LinearLayoutManager(mContext));
            //设置适配器
            operator_view.setAdapter(adapter = new RecyclerViewAdapter(mContext, Title2, Text2));
        }

        if (Title3.size() > 0) {
            //设置为垂直的样式  -----服务小区模块
            service_area_view.setLayoutManager(new LinearLayoutManager(mContext));
            //设置适配器
            service_area_view.setAdapter(adapter = new RecyclerViewAdapter(mContext, Title3, Text3));
        }
    }
}
