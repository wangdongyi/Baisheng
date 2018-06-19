package com.gizwits.opensource.appkit.ControlModule;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
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
    }

    private void initClick() {
        imageView_right.setImageResource(R.mipmap.close);
        imageView_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
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
