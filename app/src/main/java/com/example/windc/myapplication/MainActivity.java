package com.example.windc.myapplication;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button restart = null;
    private WuziqiPanel wuziqi_view;
    private TextView status_text = null;
    private SoundPool mSoundPool = null;
    private int soundId = 0;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        restart = findViewById(R.id.restart_button);
        wuziqi_view = findViewById(R.id.wuziqi_view);
        status_text = findViewById(R.id.status_view);
        String text = "等候 " + (wuziqi_view.getIsWhiteNow() ? "白棋" : "黑棋") + " 落子";
        status_text.setText(text);
        initSoundPool();
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wuziqi_view.start();
            }
        });

        wuziqi_view.parentActivity = this;
    }

    private void initSoundPool() {
        Log.i(TAG, "initSoundPool: begin");
        try {
            AudioAttributes audioAttr = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
            mSoundPool  = new SoundPool.Builder().setMaxStreams(16).setAudioAttributes(audioAttr).build();
            if (null == mSoundPool){
                Log.e(TAG, "initSoundPool: soudpool is null");
            }
            Log.i(TAG, "initSoundPool: begin load");

            Context context = getApplicationContext();
            soundId = mSoundPool.load(context, R.raw.click_sound, 1);
            if (soundId == 0){
                Log.e(TAG, "initSoundPool: load music fail");
            } else
            {
                Log.i(TAG, "initSoundPool: load music , soundId = " + soundId);
            }
        } catch (Exception e){
            Log.e(TAG, "initSoundPool: exception = " + e.toString());
        }

    }
    public void playMusic(){
        try {
            mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
        } catch (Exception e) {
            Log.e(TAG, "playMusic: error : " + e.toString() );
        }
    }
}
