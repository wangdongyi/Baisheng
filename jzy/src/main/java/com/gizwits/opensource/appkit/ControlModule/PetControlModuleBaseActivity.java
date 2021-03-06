package com.gizwits.opensource.appkit.ControlModule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.utils.HexStrUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 作者：王东一
 * 创建时间：2018/6/28.
 */
public class PetControlModuleBaseActivity extends GosBaseActivity {

    /*
     * ===========================================================
     * 以下key值对应开发者在云端定义的数据点标识名
     * ===========================================================
     */
    // 数据点"data"对应的标识名
    protected static final String KEY_DATA = "Temp";
    protected static final String KEY_DATA6 = "StartTimeHour";
    protected static final String KEY_DATA7 = "StartTimeMin";
    protected static final String KEY_DATA8 = "CloseTimeHour";
    protected static final String KEY_DATA9 = "CloseTimeMin";
    protected static final String KEY_DATA12 = "Week";
    protected static final String KEY_DATA13 = "Waring";
    protected static final String KEY_DATA14 = "switch1";

    /*
     * ===========================================================
     * 以下数值对应开发者在云端定义的可写数值型数据点增量值、数据点定义的分辨率、seekbar滚动条补偿值
     * _ADDITION:数据点增量值
     * _RATIO:数据点定义的分辨率
     * _OFFSET:seekbar滚动条补偿值
     * APP与设备定义的协议公式为：y（APP接收的值）=x（设备上报的值）* RATIO（分辨率）+ADDITION（增量值）
     * 由于安卓的原生seekbar无法设置最小值，因此代码中增加了一个补偿量OFFSET
     * 实际上公式中的：x（设备上报的值）=seekbar的值+补偿值
     * ===========================================================
     */

    /*
     * ===========================================================
     * 以下变量对应设备上报类型为布尔、数值、扩展数据点的数据存储
     * ===========================================================
     */
    // 数据点"data"对应的存储数据
    protected static int Temp_Left1;
    protected static int Set_Temp;
    protected static int Temp_Left3;
    protected static int Temp_Right1;
    protected static int Temp_Right2;
    protected static int Temp_Right3;
    protected static int TempRealLeft1;
    protected static int TempRealLeft2;
    protected static int TempRealLeft3;
    protected static int TempRealRight1;
    protected static int TempRealRight2;
    protected static int TempRealRight3;
    protected static int StartTimeHour;
    protected static int StartTimeMin;
    protected static int CloseTimeHour;
    protected static int CloseTimeMin;
    protected static int TimeHour;
    protected static int TimeMin;
    protected static int Week;
    protected static int Waring;
    protected static boolean switch1Selected;
    protected static boolean switch2Selected;
    protected static boolean switch3Selected;
    protected static boolean switch4Selected;
    protected static boolean switch5Selected;
    protected static boolean switch6Selected;
    protected static boolean switch7Selected;
    protected static boolean switch8Selected;
    protected static int mode;
    protected static int Hour_double;
    protected static int Minutes_double;
    /*
     * ===========================================================
     * 以下key值对应设备硬件信息各明细的名称，用与回调中提取硬件信息字段。
     * ===========================================================
     */
    protected static final String WIFI_HARDVER_KEY = "wifiHardVersion";
    protected static final String WIFI_SOFTVER_KEY = "wifiSoftVersion";
    protected static final String MCU_HARDVER_KEY = "mcuHardVersion";
    protected static final String MCU_SOFTVER_KEY = "mcuSoftVersion";
    protected static final String WIFI_FIRMWAREID_KEY = "wifiFirmwareId";
    protected static final String WIFI_FIRMWAREVER_KEY = "wifiFirmwareVer";
    protected static final String PRODUCT_KEY = "productKey";
    protected BaseBean baseBean;
    protected ArrayList<TemperatureBean> temperatureBeanArrayList = new ArrayList<>();
    protected ArrayList<Integer> temperatureArrayList = new ArrayList<>();
    protected int type = 0;
    private Toast mToast;
    protected AutomaticBean automaticBean = new AutomaticBean();
    ;

    @SuppressWarnings("unchecked")
    protected void getDataFromReceiveDataMap(ConcurrentHashMap<String, Object> dataMap) {
        // 已定义的设备数据点，有布尔、数值和枚举型数据

        if (dataMap.get("data") != null) {
            ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("data");
            for (String dataKey : map.keySet()) {
                switch (dataKey) {
                    case KEY_DATA:
                        Temp_Left1 = (Integer) map.get(dataKey);
                        if (Temp_Left1 > 60) {
                            Temp_Left1 = 60;
                        }
                        break;
                    case "Set_Temp":
                        Set_Temp = (int) map.get("Set_Temp");
                        if (Set_Temp > 60) {
                            Set_Temp = 60;
                        }
                        if (Set_Temp < 0) {
                            Set_Temp = 0;
                        }
                        break;
                    case KEY_DATA6:
                        StartTimeHour = (Integer) map.get(dataKey);
                        break;
                    case KEY_DATA7:
                        StartTimeMin = (Integer) map.get(dataKey);
                        break;
                    case KEY_DATA8:
                        CloseTimeHour = (Integer) map.get(dataKey);
                        break;
                    case KEY_DATA9:
                        CloseTimeMin = (Integer) map.get(dataKey);
                        break;

                    case KEY_DATA12:
                        Week = (Integer) map.get(dataKey);
                        break;
                    case KEY_DATA13:
                        Waring = (Integer) map.get(dataKey);
                        break;
                    case KEY_DATA14:
                        switch1Selected = (boolean) map.get(dataKey);
                        break;
                }
            }
            buildBase();

        }
        StringBuilder sBuilder = new StringBuilder();

        // 已定义的设备报警数据点，设备发生报警后该字段有内容，没有发生报警则没内容
        if (dataMap.get("alerts") != null) {
            ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("alerts");
            for (String alertsKey : map.keySet()) {
                if ((Boolean) map.get(alertsKey)) {
                    sBuilder.append("报警:" + alertsKey + "=true" + "\n");
                }
            }
        }

        // 已定义的设备故障数据点，设备发生故障后该字段有内容，没有发生故障则没内容
        if (dataMap.get("faults") != null) {
            ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("faults");
            for (String faultsKey : map.keySet()) {
                if ((Boolean) map.get(faultsKey)) {
                    sBuilder.append("故障:" + faultsKey + "=true" + "\n");
                }
            }
        }

        if (sBuilder.length() > 0) {
            sBuilder.insert(0, "[设备故障或报警]\n");
            myToast(sBuilder.toString().trim());
        }

        // 透传数据，无数据点定义，适合开发者自行定义协议自行解析
        if (dataMap.get("binary") != null) {
            byte[] binary = (byte[]) dataMap.get("binary");
            Log.i("", "Binary data:" + HexStrUtils.bytesToHexString(binary));
        }
    }

    private void buildBase() {
        if (baseBean == null) {
            baseBean = new BaseBean();
            baseBean.setList(temperatureBeanArrayList);
        }
        temperatureArrayList.clear();
        temperatureArrayList.add(TempRealLeft1);
        temperatureArrayList.add(TempRealLeft2);
        temperatureArrayList.add(TempRealLeft3);
        temperatureArrayList.add(TempRealRight1);
        temperatureArrayList.add(TempRealRight2);
        temperatureArrayList.add(TempRealRight3);
        baseBean.setAllSwitch(switch1Selected);
        if (mode > 2) {
            mode = 0;
        }
        baseBean.setType(mode);
        ManualBean manualBean = new ManualBean();
        manualBean.setKillSwitch(switch8Selected);
        baseBean.setManualBean(manualBean);

        automaticBean.setStarHour(StartTimeHour);
        automaticBean.setStarMinute(StartTimeMin);
        automaticBean.setEndHour(CloseTimeHour);
        automaticBean.setEndMinute(CloseTimeMin);
        automaticBean.setWeek(Week);

        TimingBean timingBean = new TimingBean();
        timingBean.setHour(TimeHour);
        timingBean.setMinute(TimeMin);
        timingBean.setHourRight(Hour_double);
        timingBean.setMinuteRight(Minutes_double);
        baseBean.setTimingBean(timingBean);

        temperatureBeanArrayList.clear();
    }

    private void buildTemperature(boolean selected, int temp) {
        TemperatureBean temperatureBean = new TemperatureBean();
        if (temp < 20) {
            temperatureBean.setNum(20);
        } else {
            temperatureBean.setNum(temp);
        }
        temperatureBean.setSelected(selected);
        temperatureBeanArrayList.add(temperatureBean);

    }

    GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {

        /** 用于设备订阅 */
        public void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
            PetControlModuleBaseActivity.this.didSetSubscribe(result, device, isSubscribed);
        }

        /** 用于获取设备状态 */
        public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device,
                                   java.util.concurrent.ConcurrentHashMap<String, Object> dataMap, int sn) {
            PetControlModuleBaseActivity.this.didReceiveData(result, device, dataMap, sn);
        }

        /** 用于设备硬件信息 */
        public void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device,
                                       java.util.concurrent.ConcurrentHashMap<String, String> hardwareInfo) {
            PetControlModuleBaseActivity.this.didGetHardwareInfo(result, device, hardwareInfo);
        }

        /** 用于修改设备信息 */
        public void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
            PetControlModuleBaseActivity.this.didSetCustomInfo(result, device);
        }

        /** 用于设备状态变化 */
        public void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
            PetControlModuleBaseActivity.this.didUpdateNetStatus(device, netStatus);
        }

    };

    /**
     * 设备订阅回调
     *
     * @param result       错误码
     * @param device       被订阅设备
     * @param isSubscribed 订阅状态
     */
    protected void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
    }

    /**
     * 设备状态回调
     *
     * @param result  错误码
     * @param device  当前设备
     * @param dataMap 当前设备状态
     * @param sn      命令序号
     */
    protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, java.util.concurrent.ConcurrentHashMap<String, Object> dataMap, int sn) {
    }

    /**
     * 设备硬件信息回调
     *
     * @param result       错误码
     * @param device       当前设备
     * @param hardwareInfo 当前设备硬件信息
     */
    protected void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device, java.util.concurrent.ConcurrentHashMap<String, String> hardwareInfo) {
    }

    /**
     * 修改设备信息回调
     *
     * @param result 错误码
     * @param device 当前设备
     */
    protected void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
    }

    /**
     * 设备状态变化回调
     */
    protected void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ShowToast")
    public void myToast(String string) {
        if (mToast != null) {
            mToast.setText(string);
        } else {
            mToast = Toast.makeText(this, string, Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    protected void hideKeyBoard() {
        // 隐藏键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void UpdateSingle(Boolean s1, Boolean s2, Boolean s3, int type, int num, int week, int startTimeHour, int startTimeMin, int endHour, int endMin, int selectedH, int sm) {


    }

    /**
     * Description:显示格式化数值，保留对应分辨率的小数个数，比如传入参数（20.3656，0.01），将返回20.37
     *
     * @param date 传入的数值
     * @return
     * @radio 保留多少位小数
     */
    protected String formatValue(double date, Object scale) {
        if (scale instanceof Double) {
            DecimalFormat df = new DecimalFormat(scale.toString());
            return df.format(date);
        }
        return Math.round(date) + "";
    }

    //提示框
    public void showAlertDialog(String msg, DialogInterface.OnClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(msg)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", listener)
                .create();
        dialog.show();
    }

    //提示框
    public void showAlertDialog(String msg, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener listenerC) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(msg)
                .setNegativeButton("取消", listenerC)
                .setPositiveButton("确定", listener)
                .create();
        dialog.show();
    }
}
