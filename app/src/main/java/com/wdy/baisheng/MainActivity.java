package com.wdy.baisheng;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gizwits.opensource.appkit.UserModule.LoadingActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Intent intent=new Intent(MainActivity.this, LoadingActivity.class);
        startActivity(intent);
        finish();
    }

}
