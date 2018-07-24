package com.gizwits.opensource.appkit.ControlModule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gizwits.opensource.appkit.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import wdy.business.event.MessageEvent;
import wdy.business.listen.NoDoubleClickListener;
import wdy.business.util.StatusBarUtil;

/**
 * 作者：王东一
 * 创建时间：2018/7/19.
 */
public class SelectedPetActivity extends FragmentActivity {
    private AppCompatImageView imageView_back;
    private TextView textView_title;
    private List<ImageView> imageViewList = new ArrayList<>();
    private List<RelativeLayout> relativeLayouts = new ArrayList<>();
    private List<Boolean> booleanList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_pet);
        StatusBarUtil.setStatusBarDark(getWindow(), false);
        int week = getIntent().getIntExtra("week", 0);
        getWeek(week);
        initView();
        setTitle();
        initClick();
    }

    private void initView() {
        imageView_back = (AppCompatImageView) findViewById(R.id.imageView_back);
        textView_title = (TextView) findViewById(R.id.textView_title);
        AppCompatImageView imageView_right = (AppCompatImageView) findViewById(R.id.imageView_right);
        ImageView imageView_1 = (ImageView) findViewById(R.id.imageView_1);
        RelativeLayout layout_1 = (RelativeLayout) findViewById(R.id.layout_1);
        ImageView imageView_2 = (ImageView) findViewById(R.id.imageView_2);
        RelativeLayout layout_2 = (RelativeLayout) findViewById(R.id.layout_2);
        ImageView imageView_3 = (ImageView) findViewById(R.id.imageView_3);
        RelativeLayout layout_3 = (RelativeLayout) findViewById(R.id.layout_3);
        ImageView imageView_4 = (ImageView) findViewById(R.id.imageView_4);
        RelativeLayout layout_4 = (RelativeLayout) findViewById(R.id.layout_4);
        ImageView imageView_5 = (ImageView) findViewById(R.id.imageView_5);
        RelativeLayout layout_5 = (RelativeLayout) findViewById(R.id.layout_5);
        ImageView imageView_6 = (ImageView) findViewById(R.id.imageView_6);
        RelativeLayout layout_6 = (RelativeLayout) findViewById(R.id.layout_6);
        ImageView imageView_7 = (ImageView) findViewById(R.id.imageView_7);
        RelativeLayout layout_7 = (RelativeLayout) findViewById(R.id.layout_7);
        ImageView imageView_8 = (ImageView) findViewById(R.id.imageView_8);
        RelativeLayout layout_8 = (RelativeLayout) findViewById(R.id.layout_8);
        imageViewList.add(imageView_1);
        imageViewList.add(imageView_2);
        imageViewList.add(imageView_3);
        imageViewList.add(imageView_4);
        imageViewList.add(imageView_5);
        imageViewList.add(imageView_6);
        imageViewList.add(imageView_7);
        imageViewList.add(imageView_8);
        relativeLayouts.add(layout_1);
        relativeLayouts.add(layout_2);
        relativeLayouts.add(layout_3);
        relativeLayouts.add(layout_4);
        relativeLayouts.add(layout_5);
        relativeLayouts.add(layout_6);
        relativeLayouts.add(layout_7);
        relativeLayouts.add(layout_8);

    }

    private void getWeek(int week) {
        if (week <= 0) {
            for (int i = 0; i < 8; i++) {
                booleanList.add(false);
            }
        } else {
            StringBuilder value = new StringBuilder(Integer.toBinaryString(week));
            int length = 8 - value.length();
            for (int i = 0; i < length; i++) {
                value.insert(0, "0");
            }
            for (int i = 0; i < value.length(); i++) {
                if (String.valueOf(value.charAt(value.length() - 1 - i)).endsWith("1")) {
                    booleanList.add(true);
                } else {
                    booleanList.add(false);
                }
            }
        }


    }

    private void initClick() {
        imageView_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
        for (int i = 0; i < relativeLayouts.size(); i++) {
            final int finalI = i;
            relativeLayouts.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalI != 7) {
                        if (booleanList.get(finalI)) {
                            booleanList.set(finalI, false);
                            imageViewList.get(finalI).setVisibility(View.GONE);
                        } else {
                            booleanList.set(finalI, true);
                            imageViewList.get(finalI).setVisibility(View.VISIBLE);
                        }
                        unSelectedOnce();
                    } else {
                        if (booleanList.get(finalI)) {
                            unSelectedOnce();
                        } else {
                            selectedOnce();
                        }
                    }
                    EventBus.getDefault().post(new MessageEvent("刷新复用", getWeek()));
                }
            });
        }
    }

    private void unSelectedOnce() {
        booleanList.set(booleanList.size() - 1, false);
        imageViewList.get(booleanList.size() - 1).setVisibility(View.GONE);
    }

    private void selectedOnce() {
        for (int i = 0; i < booleanList.size(); i++) {
            booleanList.set(i, false);
            imageViewList.get(i).setVisibility(View.GONE);
        }
        booleanList.set(booleanList.size() - 1, true);
        imageViewList.get(booleanList.size() - 1).setVisibility(View.VISIBLE);
    }

    private void setTitle() {
        textView_title.setText("重复");
        for (int i = 0; i < booleanList.size(); i++) {
            if (booleanList.get(i)) {
                imageViewList.get(i).setVisibility(View.VISIBLE);
            } else {
                imageViewList.get(i).setVisibility(View.GONE);
            }

        }

    }

    private int getWeek() {
        long week = 0;
        for (int i = 0; i < booleanList.size(); i++) {
            if (booleanList.get(i)) {
                week = (long) (week + Math.pow(10, i));
            }
        }
        int back = 0;
        String binaryString = week + "";
        back = Integer.parseInt(binaryString, 2);
        return back;
    }


}
