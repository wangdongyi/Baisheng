package com.gizwits.opensource.appkit.ControlModule;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.opensource.appkit.R;

import java.util.Calendar;

import wdy.business.listen.NoDoubleClickListener;
import wdy.business.util.StatusBarUtil;
import wdy.business.view.ProtractorView;

/**
 * 作者：王东一
 * 创建时间：2018/6/19.
 */
public class PetControlActivity extends GosControlModuleBaseActivity {
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
    private int starHour, starMinute, endHour, endMinute;
    private boolean userDone = false;
    private GizWifiDevice mDevice;//设备
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_control);
        StatusBarUtil.setStatusBarDark(getWindow(), false);
        initView();
        initDevice();
        initClick();
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
        protractor.setAngle(45);
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
                    TimeShow(starHour, starMinute, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            userDone = true;
                            starHour = hourOfDay;
                            starMinute = minute;
                            textView_start_time.setText((starHour < 10 ? "0" + starHour : starHour) + ":" + (starMinute < 10 ? "0" + starMinute : starMinute));
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
                    TimeShow(endHour, endMinute, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            userDone = true;
                            endHour = hourOfDay;
                            endMinute = minute;
                            textView_end_time.setText((endHour < 10 ? "0" + endHour : endHour) + ":" + (endMinute < 10 ? "0" + endMinute : endMinute));
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
        Log.i("Apptest", mDevice.getDid());
    }
}
