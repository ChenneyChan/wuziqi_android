package com.chenney.tcpclientview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;


import java.lang.ref.WeakReference;
import java.nio.charset.Charset;

public class ClientActivity extends AppCompatActivity {

    private Button btn_send;
    private Button start_connect;
    private TextView tv_msg_to_send;
    private TextView tv_port;
    private TextView tv_ip;
    private TextView info_show;
    private ScrollView sclv;
    private SimpleTCPClient mSimpleTCPClient;
    private String TAG = ClientActivity.class.getSimpleName();
    private  boolean isFirstLog = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        initData();
        initView();
        Log.i(TAG, "onCreate: end");
    }

    private void initData() {
        tv_msg_to_send = findViewById(R.id.tv_msg);
        btn_send = findViewById(R.id.btn_send);
        tv_ip = findViewById(R.id.host_ip);
        tv_port = findViewById(R.id.port);
        start_connect = findViewById(R.id.start_connect);
        info_show = findViewById(R.id.info_show);
        sclv = findViewById(R.id.scrl_of_info);
    }


    private void initView() {
        tv_msg_to_send.setMovementMethod(ScrollingMovementMethod.getInstance());
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSimpleTCPClient.send(tv_msg_to_send.getText().toString());
            }
        });
        btn_send.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tv_msg_to_send.setText("");
                return true;
            }
        });
        start_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSimpleTCPClient = new SimpleTCPClient() {
                    @Override
                    public void processData(byte[] data) {
                        sendMessageToActivity(new String(data, Charset.forName("UTF-8")));
                    }
                    @Override
                    public void log_print(String log) {
                        Message message = mHandler.obtainMessage(0);
                        message.obj = log;
                        mHandler.sendMessage(message);
                    }
                };
                int port = Integer.valueOf(tv_port.getText().toString());
                String host_ip = tv_ip.getText().toString();
                mSimpleTCPClient.connect(host_ip, port);
                show_info("start connect to " + host_ip + ":" + port);
            }
        });
    }

    private void sendMessageToActivity(String msg) {
        Log.i(TAG, "sendMessageToActivity: begin");
        Message message = mHandler.obtainMessage();
        message.what = 1;
        message.obj = msg;
        mHandler.sendMessage(message);
        Log.i(TAG, "sendMessageToActivity: end");
    }

    private MyHandler mHandler = new MyHandler(this);
    private static class MyHandler extends Handler {
        private WeakReference<ClientActivity> refActivity;

        MyHandler(ClientActivity activity) {
            refActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ClientActivity activity = refActivity.get();
            switch (msg.what) {
                case 1:
                    activity.show_info((String)msg.obj);
                    break;
                case 0:
                    activity.show_info((String)msg.obj);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: begin");
        super.onDestroy();
        mSimpleTCPClient.close();
    }
    private void show_info(String info) {
        synchronized(info_show) {
            if (isFirstLog) {
                info_show.setText("");
                isFirstLog = false;
            }
            info_show.append(info + "\r\n");
            sclv.invalidate();
            sclv.fullScroll(View.FOCUS_DOWN);
        }
    }
}