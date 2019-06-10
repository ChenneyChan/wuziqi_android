package com.chenney.tcpclientview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.nio.charset.Charset;

public class ClientActivity extends AppCompatActivity {

    private Button btn_send;
    private TextView tv_msg;
    private SimpleTCPClient mSimpleTCPClient;
    private String TAG = ClientActivity.class.getSimpleName();

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
        mSimpleTCPClient = new SimpleTCPClient() {
            @Override
            public void processData(byte[] data) {
                sendMessageToActivity(new String(data, Charset.forName("UTF-8")));
            }
        };
        mSimpleTCPClient.connect("127.0.0.1", 9000);
    }

    private void initView() {
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        btn_send = (Button) findViewById(R.id.btn_send);

        tv_msg.setMovementMethod(ScrollingMovementMethod.getInstance());
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSimpleTCPClient.send("Hello，from client：测试数据来了");
            }
        });
        btn_send.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tv_msg.setText("");
                return true;
            }
        });
    }

    private void sendMessageToActivity(String msg) {
        Log.i(TAG, "sendMessageToActivity: begin");
        Message message = Message.obtain();
        message.obj = msg + "\n";
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
            activity.tv_msg.append((CharSequence) msg.obj);
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: begin");
        super.onDestroy();
        mSimpleTCPClient.close();
    }
}