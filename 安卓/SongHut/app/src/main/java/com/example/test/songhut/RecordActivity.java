package com.example.test.songhut;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.example.test.util.AudioRecorder;
import com.example.test.view.AudioView;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.listener.RecordFftDataListener;
import com.zlw.main.recorderlib.recorder.listener.RecordResultListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * created by 李军邑
 */
public class RecordActivity extends AppCompatActivity{
    //状态
    private boolean isStart = false;
    private boolean isPause = false;
    final RecordManager recordManager = RecordManager.getInstance();
    //控件
    private Chronometer chronometer;
    private ImageButton recordBtn;
    private ImageButton restartBtn;
    private ImageButton saveBtn;
    private AudioView audioView;
    //录音文件路径
    private String fileName;
    //暂停时记录计时器时间
    private long chronometerTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        init();
        initRecord();
        initAudioView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doStop();
    }

    @Override
    protected void onStop() {
        //暂停计时器
        //chronometer.stop();
        //暂停录音
        //audioRecorder.pauseRecord();
        super.onStop();
        doStop();
    }

    @Override
    protected void onDestroy() {
        //暂停计时器
        chronometer.stop();
//        //释放资源
//        audioRecorder.release();
//        audioRecorder = null;
        super.onDestroy();
    }

    private void init() {
        chronometer = (Chronometer)findViewById(R.id.chr_record_time);
        recordBtn = (ImageButton)findViewById(R.id.record_btn);
        restartBtn = (ImageButton)findViewById(R.id.restart_btn);
        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        audioView = (AudioView)findViewById(R.id.audioView);
        fileName = "";
        chronometerTime = 0;
    }

    private void initRecord() {
        recordManager.init( this.getApplication(), false);
        recordManager.changeFormat(RecordConfig.RecordFormat.WAV);
        recordManager.changeRecordConfig(recordManager.getRecordConfig().setSampleRate(41000));
        recordManager.changeRecordConfig(recordManager.getRecordConfig().setEncodingConfig(AudioFormat.ENCODING_PCM_16BIT));
        String recordDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Record/";
        recordManager.changeRecordDir(recordDir);
    }

    private void initAudioView() {

        audioView.setStyle(AudioView.ShowStyle.getStyle("STYLE_HOLLOW_LUMP"), audioView.getDownStyle());
        audioView.setStyle(audioView.getUpStyle(), AudioView.ShowStyle.getStyle("STYLE_HOLLOW_LUMP"));

        //录音可视化监听
        recordManager.setRecordFftDataListener(new RecordFftDataListener() {
            @Override
            public void onFftData(byte[] data) {
                audioView.setWaveData(data);
            }
        });

        //录音结束后获得文件位置
        recordManager.setRecordResultListener(new RecordResultListener() {
            @Override
            public void onResult(File result) {
                fileName = result.getAbsolutePath();
                //传递语音文件路径
                Bundle bundle = new Bundle();
                bundle.putString("fileName", fileName);
                Intent intent = new Intent(RecordActivity.this, PlayHumAuditionActivity.class);
                //Intent intent = new Intent(RecordActivity.this, Convert2MelodyActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    private void doStop() {
        recordManager.stop();
        isPause = false;
        isStart = false;
    }

    private void doPlay() {
        if (isStart) {
            //开始
            recordManager.pause();
            isPause = true;
            isStart = false;
            //按钮切换
            recordBtn.setImageDrawable(getDrawable(R.drawable.record_pause));
            //计时器开始计时
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        if(isPause){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chronometer.setBase(SystemClock.elapsedRealtime());
                                    chronometer.start();
                                }
                            });
                            break;
                        }
                    }
                }
            }).start();
        } else {
            //暂停或继续
            if (isPause) {
                recordManager.resume();
                //暂停计时
                chronometer.stop();
                chronometerTime = SystemClock.elapsedRealtime() - chronometer.getBase();
                //切换按钮
                recordBtn.setImageDrawable(getDrawable(R.drawable.record));
            } else {
                recordManager.start();
                //开始计时
                chronometer.setBase(SystemClock.elapsedRealtime() - chronometerTime);
                chronometer.start();
                //切换按钮
                recordBtn.setImageDrawable(getDrawable(R.drawable.record_pause));
            }
            isStart = true;
        }
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.record_btn:
                    doPlay();
                break;
            case R.id.restart_btn:
//                //重置计时器
//                chronometer.stop();
//                chronometer.setText("00:00");
//                //重置文件名
//                fileName = "";
//                //重置录音机
//                audioRecorder.cancel();
//                //重置按钮图片
//                recordBtn.setImageDrawable(getDrawable(R.drawable.record));
                break;
            case R.id.save_btn:
                doStop();
                break;
        }
    }
}
