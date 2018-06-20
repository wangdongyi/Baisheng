package com.gizwits.opensource.appkit.ControlModule;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.gizwits.opensource.appkit.R;

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
    private RelativeLayout repeatlayout;
    private LinearLayout layout_automatic;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_control);
        StatusBarUtil.setStatusBarDark(getWindow(), false);
        initView();
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
        repeatlayout = (RelativeLayout) findViewById(R.id.repeatlayout);
        layout_automatic = (LinearLayout) findViewById(R.id.layout_automatic);
        button = (Button) findViewById(R.id.button);
    }

    private void initClick() {
        imageView_right.setImageResource(R.mipmap.close);
        imageView_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    imageView_right.setImageResource(R.mipmap.close);
                    isEdit = false;
                    edit_view.setVisibility(View.GONE);
                } else {
                    imageView_right.setImageResource(R.mipmap.open);
                    isEdit = true;
                    edit_view.setVisibility(View.VISIBLE);
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
    }
}
