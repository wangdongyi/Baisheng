package com.gizwits.opensource.appkit.ConfigModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.view.GifView;

import java.util.ArrayList;
import java.util.List;

import wdy.business.listen.NoDoubleClickListener;
import wdy.business.util.StatusBarUtil;

public class GosAirlinkReadyActivity extends GosConfigModuleBaseActivity implements OnClickListener {

	/** The cb Select */
	CheckBox cbSelect;

	/** The tv Select */
	TextView tvSelect;

	/** The btn Next */
	Button btnNext;

	private TextView moudlechoose;

	private List<String> modeList;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actvity_gos_airlink_ready);
		// 设置ActionBar
//		setActionBar(true, true, R.string.airlink_ready_title);
		StatusBarUtil.setStatusBarDark(getWindow(),false);
		initData();
		initView();
		initEvent();
	}

	private void initData() {
		// workSSID = spf.getString("workSSID", "");

		modeList = new ArrayList<String>();
		String[] modes = this.getResources().getStringArray(R.array.mode);
		for (String string : modes) {
			modeList.add(string);
		}
	}

	private void initView() {
		cbSelect = (CheckBox) findViewById(R.id.cbSelect);
		tvSelect = (TextView) findViewById(R.id.tvSelect);
		btnNext = (Button) findViewById(R.id.btnNext);

		moudlechoose = (TextView) findViewById(R.id.moudlechoose);
		ImageView back = findViewById(R.id.imageView_back);
		back.setOnClickListener(new NoDoubleClickListener() {
			@Override
			protected void onNoDoubleClick(View v) {
				finish();
			}
		});
		TextView title = findViewById(R.id.textView_title);
		title.setText(R.string.airlink_ready_title);
		String mdchoose = getResources().getString(R.string.moudlechoose);

		String[] split = mdchoose.split("xx");

		int setModuleSelectOn = GosDeploy.setModuleSelectOn();

		if (setModuleSelectOn == 0) {
			moudlechoose.setVisibility(View.INVISIBLE);
		} else {
			moudlechoose.setVisibility(View.INVISIBLE);
		}

		if (split.length > 1) {
			moudlechoose.setText(
					split[0] + " " + modeList.get(GosAirlinkChooseDeviceWorkWiFiActivity.modeNum) + " " + split[1]);
		} else {
			moudlechoose.setText(
					split[0] + " " + modeList.get(GosAirlinkChooseDeviceWorkWiFiActivity.modeNum) + " " );
		}

		/** 加载Gif */
		GifView gif = (GifView) findViewById(R.id.softreset);
		gif.setMovieResource(R.drawable.airlink);

		// 配置文件部署
		btnNext.setBackgroundDrawable(GosDeploy.setButtonBackgroundColor());
		btnNext.setTextColor(GosDeploy.setButtonTextColor());

	}

	private void initEvent() {
		btnNext.setOnClickListener(this);
		tvSelect.setOnClickListener(this);
		btnNext.setClickable(false);
		btnNext.setBackgroundResource(R.drawable.btn_next_shape_gray);

		cbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					btnNext.setBackgroundDrawable(GosDeploy.setButtonBackgroundColor());
					btnNext.setClickable(true);
				} else {
					btnNext.setBackgroundResource(R.drawable.btn_next_shape_gray);
					btnNext.setClickable(false);
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.btnNext) {
			Intent intent = new Intent(this, GosAirlinkConfigCountdownActivity.class);
			startActivity(intent);
			finish();

		} else if (i == R.id.tvSelect) {
			if (cbSelect.isChecked()) {
				cbSelect.setChecked(false);
			} else {
				cbSelect.setChecked(true);
			}

		} else {
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			this.finish();
			break;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent = new Intent(this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		this.finish();
		return true;
	}

}
