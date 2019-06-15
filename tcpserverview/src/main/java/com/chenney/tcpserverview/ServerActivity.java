package com.chenney.tcpserverview;

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

public class ServerActivity extends AppCompatActivity {
    private String TAG = ServerActivity.class.getSimpleName();
    private TextView tv_msg_to_send;
    private Button btn_send_to_client;
    private Button btn_start;
    private Button btn_stop;
    private TextView info_show;
    private ScrollView sclv;
    private final int port = 9000;
    private boolean isFirst = true;

    private SimpleTCPServer mSimpleTCPServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        initData();
        initView();
        Log.i(TAG, "onCreate: end");
    }

    private void initData() {
        tv_msg_to_send = findViewById(R.id.tv_srv_msg);
        btn_send_to_client = findViewById(R.id.btn_restart);
        btn_start = findViewById(R.id.start_server);
        btn_stop = findViewById(R.id.stop_server);
        info_show = findViewById(R.id.info_show);
        sclv = findViewById(R.id.scrl_of_info);
    }

    private void initView() {
//        info_show.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv_msg_to_send.setMovementMethod(ScrollingMovementMethod.getInstance());
        btn_send_to_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSimpleTCPServer != null) {
                    show_info("Send msg to client, client count = " + mSimpleTCPServer.getClientCount());
                    for (int i = 0; i < mSimpleTCPServer.getClientCount(); i++) {
                        mSimpleTCPServer.sendData(i, ("Push From Sever, 服务器发送数据来了" + tv_msg_to_send.getText().toString()).getBytes(Charset.forName("UTF-8")));
                    }
                }
            }
        });

        btn_send_to_client.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tv_msg_to_send.setText("");
                return true;
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSimpleTCPServer == null) {
                    mSimpleTCPServer = new SimpleTCPServer(mHandler);
                    mSimpleTCPServer.listen(port);
                    show_info("START SERVER on port " + port + " DONE!");
                }
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSimpleTCPServer != null) {
                    mSimpleTCPServer.close();
                    show_info("STOP SERVER DONE!");
                    mSimpleTCPServer = null;
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: begin");
        super.onDestroy();
        mSimpleTCPServer.close();
    }

    private MyHandler mHandler = new MyHandler(this);
    private class MyHandler extends Handler {
        WeakReference<ServerActivity> refActivity;

        public MyHandler(ServerActivity activity) {
            refActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                ServerActivity activity = refActivity.get();
                int which = msg.what;

                switch (which) {
                    case 11111:
                        show_info((String)msg.obj);
                        break;
                   default:
                        byte[] data = (byte[]) msg.obj;
                        String result = new String(data, Charset.forName("UTF-8"));
                        Log.i(TAG, "handleMessage: witch = " +  which + " info = " + result);
                        show_info("第" + which + "位访客"
                                + activity.mSimpleTCPServer.getClient(which).getInetAddress().getHostAddress() + ":"
                                + result);
                        activity.mSimpleTCPServer.sendData(msg.what, "Hi你好，I am server".getBytes(Charset.forName("UTF-8")));
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void show_info (String info) {
        synchronized(info_show) {
            if (isFirst) {
                info_show.setText("");
                isFirst = false;
            }
            info_show.append(info + "\r\n");
            sclv.invalidate();
            sclv.fullScroll(View.FOCUS_DOWN);
        }
    }
}
