package com.gizwits.opensource.appkit.ConfigModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.R;

import wdy.business.util.StatusBarUtil;

public class GosConfigFailedActivity extends GosConfigModuleBaseActivity implements OnClickListener {

	/** The btn Again */
	Button btnAgain;

	/** The soft SSID */
	String softSSID;

	/** The data */
	String promptText, cancelBesureText, beSureText, cancelText;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_config_failed);
		// 设置ActionBar
		setActionBar(true, true, R.string.join_failed_title);
		StatusBarUtil.setStatusBarDark(getWindow(),false);
		initView();
		initEvent();
		initData();
	}

	private void initView() {
		btnAgain = (Button) findViewById(R.id.btnAgain);

		// 配置文件部署
		btnAgain.setBackgroundDrawable(GosDeploy.setButtonBackgroundColor());
		btnAgain.setTextColor(GosDeploy.setButtonTextColor());
	}

	private void initEvent() {
		btnAgain.setOnClickListener(this);
	}

	private void initData() {
		promptText = (String) getText(R.string.prompt);
		cancelBesureText = (String) getText(R.string.cancel_besure);
		beSureText = (String) getText(R.string.besure);
		cancelText = (String) getText(R.string.no);
	}

	// 屏蔽掉返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			quitAlert(this);
			return true;
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menu) {
		switch (menu.getItemId()) {
		case android.R.id.home:
			quitAlert(this);
			break;

		default:
			break;
		}

		return true;
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.btnAgain) {
			Intent intent = new Intent(this, GosCheckDeviceWorkWiFiActivity.class);
			startActivity(intent);
			finish();

		} else {
		}
	}
}
