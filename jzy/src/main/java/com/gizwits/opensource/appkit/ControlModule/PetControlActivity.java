package com.gizwits.opensource.appkit.ControlModule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.opensource.appkit.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import wdy.business.event.MessageEvent;
import wdy.business.listen.NoDoubleClickListener;
import wdy.business.util.StatusBarUtil;
import wdy.business.view.ProtractorView;

import static com.gizwits.opensource.appkit.ControlModule.SelectedWeekActivity.clean;
import static com.gizwits.opensource.appkit.ControlModule.SelectedWeekActivity.selected1;
import static com.gizwits.opensource.appkit.ControlModule.SelectedWeekActivity.selected2;
import static com.gizwits.opensource.appkit.ControlModule.SelectedWeekActivity.selected3;
import static com.gizwits.opensource.appkit.ControlModule.SelectedWeekActivity.selected4;
import static com.gizwits.opensource.appkit.ControlModule.SelectedWeekActivity.selected5;
import static com.gizwits.opensource.appkit.ControlModule.SelectedWeekActivity.selected6;
import static com.gizwits.opensource.appkit.ControlModule.SelectedWeekActivity.selected7;

/**
 * 作者：王东一
 * 创建时间：2018/6/19.
 */
public class PetControlActivity extends PetControlModuleBaseActivity implements CompoundButton.OnCheckedChangeListener {
    private AppCompatImageView imageView_back;
    private TextView textView_title;
    private TextView text_show;
    private AppCompatImageView imageView_right;
    private ProtractorView protractor;
    private View edit_view;
    private boolean isEdit = false;
    private Switch switch_all;
    private TextView text_tem;
    private TextView textView_start_time;
    private RelativeLayout start_time_layout;
    private TextView textView_end_time;
    private RelativeLayout end_time_layout;
    private TextView textView_repeat;
    private RelativeLayout repeat_layout;
    private LinearLayout layout_automatic;
    private Button button;
    private Calendar mCalendar;
    private boolean userDone = false;
    private GizWifiDevice mDevice;//设备

    private enum handler_key {
        //更新界面
        UPDATE_UI,
        UPDATE,
        DISCONNECT,
    }

    private Runnable mRunnable = new Runnable() {
        public void run() {
            if (isDeviceCanBeControlled()) {
                progressDialog.cancel();
            } else {
                toastDeviceNoReadyAndExit();
            }
        }
    };

    /**
     * The handler.
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PetControlActivity.handler_key key = PetControlActivity.handler_key.values()[msg.what];
            switch (key) {
                case UPDATE_UI:
                    if (!isEdit)
                        updateUI();
                    break;
                case UPDATE:
                    userDone = false;
                    isEdit = false;
                    getStatusOfDevice();
                    break;
                case DISCONNECT:
                    toastDeviceDisconnectAndExit();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_control);
        StatusBarUtil.setStatusBarDark(getWindow(), false);
        EventBus.getDefault().register(this);
        initView();
        initDevice();
        getStatusOfDevice();
        initEvent();
    }

    private void initView() {
        imageView_back = (AppCompatImageView) findViewById(R.id.imageView_back);
        textView_title = (TextView) findViewById(R.id.textView_title);
        text_show = (TextView) findViewById(R.id.text_show);
        imageView_right = (AppCompatImageView) findViewById(R.id.imageView_right);
        protractor = (ProtractorView) findViewById(R.id.protractor);
        edit_view = findViewById(R.id.edit_view);
        switch_all = (Switch) findViewById(R.id.switch_all);
        text_tem = (TextView) findViewById(R.id.text_tem);
        textView_start_time = (TextView) findViewById(R.id.textView_start_time);
        start_time_layout = (RelativeLayout) findViewById(R.id.start_time_layout);
        textView_end_time = (TextView) findViewById(R.id.textView_end_time);
        end_time_layout = (RelativeLayout) findViewById(R.id.end_time_layout);
        textView_repeat = (TextView) findViewById(R.id.textView_repeat);
        repeat_layout = (RelativeLayout) findViewById(R.id.repeat_layout);
        layout_automatic = (LinearLayout) findViewById(R.id.layout_automatic);
        button = (Button) findViewById(R.id.button);
    }

    private void initClick() {
        imageView_right.setImageResource(R.mipmap.close);
        imageView_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {//是否可以编辑
                    imageView_right.setImageResource(R.mipmap.close);
                    isEdit = false;
                    edit_view.setVisibility(View.VISIBLE);
                    button.setVisibility(View.GONE);
                } else {
                    imageView_right.setImageResource(R.mipmap.open);
                    isEdit = true;
                    edit_view.setVisibility(View.GONE);
                    button.setVisibility(View.VISIBLE);
                }
            }
        });
        imageView_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
        edit_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myToast("想修改请先点右上角解锁");
            }
        });
        textView_title.setText("宠物之家");
        text_show.setText(String.valueOf(protractor.getAngle()));
        protractor.setOnProtractorViewChangeListener(new ProtractorView.OnProtractorViewChangeListener() {
            @Override
            public void onProgressChanged(ProtractorView protractorView, int progress, boolean fromUser) {
                text_show.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(ProtractorView protractorView) {

            }

            @Override
            public void onStopTrackingTouch(ProtractorView protractorView) {

            }
        });
        start_time_layout.setOnClickListener(new NoDoubleClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onNoDoubleClick(View v) {
                if (switch_all.isChecked()) {
                    TimeShow(automaticBean.getStarHour(), automaticBean.getStarMinute(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            userDone = true;
                            automaticBean.setStarHour(hourOfDay);
                            automaticBean.setStarMinute(minute);
                            textView_start_time.setText((hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (minute < 10 ? "0" + minute : minute));
                        }
                    });
                } else {
                    myToast("设备关机中不能修改");
                }
            }
        });
        end_time_layout.setOnClickListener(new NoDoubleClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onNoDoubleClick(View v) {
                if (switch_all.isChecked()) {
                    TimeShow(automaticBean.getEndHour(), automaticBean.getEndMinute(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            userDone = true;
                            automaticBean.setEndHour(hourOfDay);
                            automaticBean.setEndMinute(minute);
                            textView_end_time.setText((hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (minute < 10 ? "0" + minute : minute));
                        }
                    });
                } else {
                    myToast("设备关机中不能修改");
                }
            }
        });
        repeat_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (switch_all.isChecked()) {
                    userDone = true;
                    Intent intent = new Intent(PetControlActivity.this, SelectedWeekActivity.class);
                    startActivity(intent);
                } else {
                    myToast("设备关机中不能修改");
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                int sn = 5;
                ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
                hashMap.put(KEY_DATA14, switch_all.isChecked());
                hashMap.put("Set_Temp", protractor.getAngle());
                hashMap.put(KEY_DATA6, automaticBean.getStarHour());
                hashMap.put(KEY_DATA7, automaticBean.getStarMinute());
                hashMap.put(KEY_DATA8, automaticBean.getEndHour());
                hashMap.put(KEY_DATA9, automaticBean.getEndMinute());
                hashMap.put(KEY_DATA12, automaticBean.getWeek());
                mDevice.write(hashMap, sn);
            }
        });
    }

    private void TimeShow(int hour, int minute, TimePickerDialog.OnTimeSetListener listener) {
        mCalendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(PetControlActivity.this, listener, hour, minute, true);
        dialog.show();
    }

    private void initDevice() {
        Intent intent = getIntent();
        mDevice = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        mDevice.setListener(gizWifiDeviceListener);
        Log.i("Apptest", mDevice.getProductKey());
    }

    private void initEvent() {
        clean();
        initClick();
        initSwitch();
    }

    private void initSwitch() {
        switch_all.setOnCheckedChangeListener(this);
    }

    private void changeDevice(boolean change) {
        protractor.setEnabled(change);
        start_time_layout.setClickable(change);
        end_time_layout.setClickable(change);
        repeat_layout.setClickable(change);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        changeDevice(isChecked);//修改开机关机状态
    }

    /**
     * Description:根据保存的的数据点的值来更新UI
     */
    @SuppressLint("SetTextI18n")
    protected void updateUI() {
        switch_all.setChecked(switch1Selected);
        protractor.setAngle(Set_Temp);
        text_tem.setText("当前温度" + Temp_Left1);
        textView_start_time.setText((automaticBean.getStarHour() < 10 ? "0" + automaticBean.getStarHour() : automaticBean.getStarHour()) +
                ":" + (automaticBean.getStarMinute() < 10 ? "0" + automaticBean.getStarMinute() : automaticBean.getStarMinute()));
        textView_end_time.setText((automaticBean.getEndHour() < 10 ? "0" + automaticBean.getEndHour() : automaticBean.getEndHour()) +
                ":" + (automaticBean.getEndMinute() < 10 ? "0" + automaticBean.getEndMinute() : automaticBean.getEndMinute()));
        textView_repeat.setText(getWeek(automaticBean.getWeek()));
    }

    private String getWeek(int week) {
        String value = Integer.toBinaryString(week);
        int length = 7 - value.length();
        String[] weekBuffer = getResources().getStringArray(R.array.week);
        for (int i = 0; i < length; i++) {
            value = "0" + value;
        }
        changeWeek(value);
        StringBuilder weekSelected = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            if (String.valueOf(value.charAt(i)).endsWith("1")) {
                weekSelected.append(weekBuffer[i]).append(" ");
            }
        }
        return weekSelected.toString();
    }

    /**
     * Description:页面加载后弹出等待框，等待设备可被控制状态回调，如果一直不可被控，等待一段时间后自动退出界面
     */
    private void getStatusOfDevice() {
        // 设备是否可控
        if (isDeviceCanBeControlled()) {
            // 可控则查询当前设备状态
            mDevice.getDeviceStatus();
        } else {
            // 显示等待栏
            progressDialog.show();
            if (mDevice.isLAN()) {
                // 小循环10s未连接上设备自动退出
                mHandler.postDelayed(mRunnable, 10000);
            } else {
                // 大循环20s未连接上设备自动退出
                mHandler.postDelayed(mRunnable, 20000);
            }
        }
    }

    private boolean isDeviceCanBeControlled() {
        return mDevice.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceControlled;
    }

    private void toastDeviceNoReadyAndExit() {
        Toast.makeText(this, "设备无响应，请检查设备是否正常工作", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void toastDeviceDisconnectAndExit() {
        Toast.makeText(PetControlActivity.this, "连接已断开", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void changeWeek(String value) {
        for (int i = 0; i < value.length(); i++) {
            switch (i) {
                case 0:
                    selected7 = String.valueOf(value.charAt(i)).endsWith("1");
                    break;
                case 1:
                    selected6 = String.valueOf(value.charAt(i)).endsWith("1");
                    break;
                case 2:
                    selected5 = String.valueOf(value.charAt(i)).endsWith("1");
                    break;
                case 3:
                    selected4 = String.valueOf(value.charAt(i)).endsWith("1");
                    break;
                case 4:
                    selected3 = String.valueOf(value.charAt(i)).endsWith("1");
                    break;
                case 5:
                    selected2 = String.valueOf(value.charAt(i)).endsWith("1");
                    break;
                case 6:
                    selected1 = String.valueOf(value.charAt(i)).endsWith("1");
                    break;

            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        switch (messageEvent.getMessage()) {
            case "刷新复用":
                automaticBean.setWeek((Integer) messageEvent.getObject());
                textView_repeat.setText(getWeek((Integer) messageEvent.getObject()));
                break;

        }
    }

    /**
     * 展示设备硬件信息
     *
     * @param hardwareInfo
     */
    private void showHardwareInfo(String hardwareInfo) {
        String hardwareInfoTitle = "设备硬件信息";
        new AlertDialog.Builder(this).setTitle(hardwareInfoTitle).setMessage(hardwareInfo).setPositiveButton(R.string.besure, null).show();
    }

    /*
     * 获取设备硬件信息回调
     */
    @Override
    protected void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, String> hardwareInfo) {
        super.didGetHardwareInfo(result, device, hardwareInfo);
        StringBuffer sb = new StringBuffer();
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
            myToast("获取设备硬件信息失败：" + result.name());
        } else {
            sb.append("Wifi Hardware Version:" + hardwareInfo.get(WIFI_HARDVER_KEY) + "\r\n");
            sb.append("Wifi Software Version:" + hardwareInfo.get(WIFI_SOFTVER_KEY) + "\r\n");
            sb.append("MCU Hardware Version:" + hardwareInfo.get(MCU_HARDVER_KEY) + "\r\n");
            sb.append("MCU Software Version:" + hardwareInfo.get(MCU_SOFTVER_KEY) + "\r\n");
            sb.append("Wifi Firmware Id:" + hardwareInfo.get(WIFI_FIRMWAREID_KEY) + "\r\n");
            sb.append("Wifi Firmware Version:" + hardwareInfo.get(WIFI_FIRMWAREVER_KEY) + "\r\n");
            sb.append("Product Key:" + "\r\n" + hardwareInfo.get(PRODUCT_KEY) + "\r\n");

            // 设备属性
            sb.append("Device ID:" + "\r\n" + mDevice.getDid() + "\r\n");
            sb.append("Device IP:" + mDevice.getIPAddress() + "\r\n");
            sb.append("Device MAC:" + mDevice.getMacAddress() + "\r\n");
        }
        showHardwareInfo(sb.toString());
    }

    /*
     * 设置设备别名和备注回调
     */
    @Override
    protected void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
        super.didSetCustomInfo(result, device);
    }

    /*
     * 设备状态改变回调，只有设备状态为可控才可以下发控制命令
     */
    @Override
    protected void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
        super.didUpdateNetStatus(device, netStatus);
        if (netStatus == GizWifiDeviceNetStatus.GizDeviceControlled) {
            mHandler.removeCallbacks(mRunnable);
            progressDialog.cancel();
        } else {
            mHandler.sendEmptyMessage(PetControlActivity.handler_key.DISCONNECT.ordinal());
        }
    }

    /*
     * 设备上报数据回调，此回调包括设备主动上报数据、下发控制命令成功后设备返回ACK
     */
    @Override
    protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
        super.didReceiveData(result, device, dataMap, sn);
        progressDialog.cancel();
        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
            if (dataMap.get("data") != null) {
                getDataFromReceiveDataMap(dataMap);
                mHandler.sendEmptyMessage(PetControlActivity.handler_key.UPDATE_UI.ordinal());
            } else {
                myToast("提交成功");
                mHandler.sendEmptyMessage(PetControlActivity.handler_key.UPDATE.ordinal());
                imageView_right.setImageResource(R.mipmap.close);
                isEdit = false;
                edit_view.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
