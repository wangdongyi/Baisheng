package com.gizwits.opensource.appkit.sharingdevice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizMessage;
import com.gizwits.gizwifisdk.enumration.GizMessageStatus;
import com.gizwits.gizwifisdk.enumration.GizMessageType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSharingListener;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wdy.business.listen.NoDoubleClickListener;

public class messageCenterActivity extends GosBaseActivity {

    private View redpoint;
    private LinearLayout gizwitsmes;
    private LinearLayout deviceshared;
    private ImageView icon1;
    private ImageView icon2;
    private ImageView icon3;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_message);
        initView();
        initRecycler();
        getImage();
    }

    @Override
    public void onResume() {
        super.onResume();

        String token = spf.getString("Token", "");
        GizDeviceSharing.queryMessageList(token, GizMessageType.GizMessageSharing);
        //GizDeviceSharing.queryMessageList(token, GizMessageType.GizMessageSystem);

        GizDeviceSharing.setListener(new GizDeviceSharingListener() {

            @Override
            public void didQueryMessageList(GizWifiErrorCode result, List<GizMessage> messageList) {
                super.didQueryMessageList(result, messageList);

                if (messageList.size() > 0) {
                    isShowRedPoint(messageList);
                } else {
                    redpoint.setVisibility(View.GONE);
                }

                if (result.ordinal() != 0) {
                    Toast.makeText(messageCenterActivity.this, toastError(result), Toast.LENGTH_LONG).show();
                }

            }

        });
    }

    private void isShowRedPoint(List<GizMessage> messageList) {

        boolean isshow = false;
        redpoint.setVisibility(View.GONE);
        for (int i = 0; i < messageList.size(); i++) {

            GizMessage gizMessage = messageList.get(i);
            GizMessageStatus status = gizMessage.getStatus();
            if (status.ordinal() == 0) {
                isshow = true;

                redpoint.setVisibility(View.VISIBLE);
            }

        }
    }

    private void initView() {
        // 判断当前的view 是否需要显示这个红点
        redpoint = findViewById(R.id.redpoint);

        gizwitsmes = (LinearLayout) findViewById(R.id.gizwitsmes);

        deviceshared = (LinearLayout) findViewById(R.id.deviceshared);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);


        icon1 = (ImageView) findViewById(R.id.icon1);
        icon1.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://sybsdq.1688.com");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        icon2 = (ImageView) findViewById(R.id.icon2);
        icon2.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://www.sybsdq.com");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        icon3 = (ImageView) findViewById(R.id.icon3);
        icon3.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://shop591402973.taobao.com");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
    }

    private void initRecycler() {
        adapter = new MessageAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // 跳转到设备分享的界面
    public void deviceshared(View v) {

        Intent tent = new Intent(this, deviceSharedMessageActivity.class);
        startActivity(tent);

        deviceshared.setEnabled(false);
        deviceshared.postDelayed(new Runnable() {
            @Override
            public void run() {
                deviceshared.setEnabled(true);
            }
        }, 1000);

    }

    // 跳转到机智云公告页面
    public void gizwitsmes(View v) {

        Intent intent = new Intent(this, MsgNoticeActivity.class);
        startActivity(intent);

        gizwitsmes.setEnabled(false);
        gizwitsmes.postDelayed(new Runnable() {
            @Override
            public void run() {
                gizwitsmes.setEnabled(true);
            }
        }, 1000);


    }

    private void getImage() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                String url = "http://www.kongtiaoguanjia.com/machi/userController/select_all_img.do";
                OkHttpClient okHttpClient = new OkHttpClient();
                final Request request = new Request.Builder()
                        .url(url)
                        .get()//默认就是GET请求，可以不写
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("请求", "onFailure: ");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String data = response.body().string();
                        //http://www.kongtiaoguanjia.com/
                        try {
                            JSONArray jsonObject = new JSONArray(data);
                            for (int i = 0; i < jsonObject.length(); i++) {
                                JSONObject job = jsonObject.getJSONObject(i);
                                list.add(job.optString("img_src"));
                            }
                            handler.sendEmptyMessage(1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    handler handler = new handler();

    @SuppressLint("HandlerLeak")
    private class handler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
