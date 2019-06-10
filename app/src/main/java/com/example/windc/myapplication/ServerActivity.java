package com.example.windc.myapplication;

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

public class ServerActivity extends AppCompatActivity {
    private String TAG = ServerActivity.class.getSimpleName();
    private TextView tv_msg;
    private Button btn_restart;

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
        mSimpleTCPServer = new SimpleTCPServer(mHandler);
        mSimpleTCPServer.listen(9000);
    }

    private void initView() {
        tv_msg = (TextView) findViewById(R.id.tv_srv_msg);
        btn_restart = (Button) findViewById(R.id.btn_restart);

        tv_msg.setMovementMethod(ScrollingMovementMethod.getInstance());
        btn_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSimpleTCPServer != null) {
                    for (int i = 0; i < mSimpleTCPServer.getClientCount(); i++) {
                        mSimpleTCPServer.sendData(i, "Push From Sever, 服务器发送数据来了".getBytes(Charset.forName("UTF-8")));
                    }
                }
            }
        });
        btn_restart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tv_msg.setText("");
                return true;
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
    private static class MyHandler extends Handler {
        WeakReference<ServerActivity> refActivity;

        public MyHandler(ServerActivity activity) {
            refActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ServerActivity activity = refActivity.get();
            int which = msg.what;
            byte[] data = (byte[]) msg.obj;
            String result = new String(data, Charset.forName("UTF-8"));
            activity.tv_msg.append("第" + which + "位访客"
                    + activity.mSimpleTCPServer.getClient(which).getInetAddress().getHostAddress() + ":"
                    + result + "\n");
            activity.mSimpleTCPServer.sendData(msg.what, "Hi你好，I am server".getBytes(Charset.forName("UTF-8")));
        }
    }
}