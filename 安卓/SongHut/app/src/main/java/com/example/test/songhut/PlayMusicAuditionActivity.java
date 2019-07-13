package com.example.test.songhut;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.gsonUtil.PostFileToRepositoryBean;
import com.example.test.util.GetInfoUtil;
import com.example.test.util.UploadUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cc.fussen.cache.Cache;

/**
 * created by 李军邑
 */
public class PlayMusicAuditionActivity extends AppCompatActivity implements View.OnClickListener {

    private android.support.v7.widget.Toolbar toolBar;
    private ImageButton pauseBtn;
    private SeekBar auditionSb;
    private TextView currentTimeTv;
    private TextView totalTimeTv;
    private EditText nameEd;
    private EditText bioEd;
    private Spinner selectSp;
    private Button saveBtn;

    private String fileName;
    private MediaPlayer mediaPlayer;

    //所得乐库名字和id
    private String jsonStr;
    private String token;
    private Map<Integer, String> repMap;
    private List<String> repList = new ArrayList<String>();

    private int rid = 0;
    private int fid = 0;
    //private int type = 0;
    private int spPos;

    //接受多线程信息，安卓中不允许主线程实现UI更新
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            auditionSb.setProgress(msg.what);
            currentTimeTv.setText(formatTime(msg.what));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music_audition);
        //从上个界面接受所录歌曲
        fileName = getIntent().getStringExtra("fileName");
        init();
        //初始化ToolBar
        setSupportActionBar(toolBar);
        ActionBar ab=getSupportActionBar();
        if(ab!=null){
            ab.setDisplayShowTitleEnabled(false);
            ab.setHomeAsUpIndicator(R.drawable.toolbar_back);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void init() {
        toolBar = (android.support.v7.widget.Toolbar)findViewById(R.id.tb_audition);
        pauseBtn = findViewById(R.id.btn_pause);
        auditionSb = findViewById(R.id.sb_convert_audition);
        currentTimeTv = findViewById(R.id.tv_current_time);
        totalTimeTv = findViewById(R.id.tv_total_time);
        nameEd = findViewById(R.id.ed_audition_name);
        bioEd = findViewById(R.id.et_audition_bio);
        selectSp = findViewById(R.id.sp_select_repository);
        saveBtn = findViewById(R.id.btn_save);
        spPos = 0;

        //初始化播放器
        mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(fileName);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        initSpinner();
        initSeekBar();
    }

    //初始化spinner
    private void initSpinner(){
        /**选项选择监听*/
        selectSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spPos = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //获得所有乐库名称
        String cachePath = getApplicationContext().getCacheDir().getPath();
        jsonStr  = Cache.with(getApplicationContext())
                .path(cachePath)
                .getCache("info", String.class);
        repMap = GetInfoUtil.GetAllRepositoryName(jsonStr);
        //返回值为null
        if(repMap == null){
            return;
        }
        //得到所有乐库名称
        Collection<String> values = repMap.values();
        for(String value: values){
            repList.add(value);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, repList);
        selectSp.setAdapter(adapter);
    }

    //初始化SeekBar
    private void initSeekBar(){
        int totalTime = mediaPlayer.getDuration();
        totalTimeTv.setText("/" + formatTime(totalTime));
        //开启SeekBar监听进程
        new Thread(new PlayMusicAuditionActivity.SeekBarThread()).start();
        auditionSb.setMax(totalTime);
        auditionSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //将歌曲时长转化为标准格式
    private String formatTime(int length) {
        Date date = new Date(length);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        String TotalTime = simpleDateFormat.format(date);
        return TotalTime;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_pause:
                if (mediaPlayer.isPlaying()) {
                    pauseBtn.setImageDrawable(getDrawable(R.drawable.play_music));
                    mediaPlayer.pause();
                } else {
                    pauseBtn.setImageDrawable(getDrawable(R.drawable.pause_music));
                    mediaPlayer.start();
                }
                break;
            case R.id.btn_save:
                if(TextUtils.isEmpty(nameEd.getText())){
                    Toast.makeText(getApplicationContext(), "请命名" ,Toast.LENGTH_SHORT).show();
                    break;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //素材命名
                        String name = nameEd.getText().toString();
                        //素材简介
                        String bio = bioEd.getText().toString();
                        //保存位置
                        String repName = repList.get(spPos);
                        int tRid = 1;
                        for(Map.Entry<Integer, String> entry : repMap.entrySet()){
                            if(entry.getValue() == repName){
                                tRid = entry.getKey();
                                break;
                            }
                        }
                        rid = tRid;

                        String returnStr = UploadUtil.postFileToRepository(GetInfoUtil.GetToken(jsonStr), fileName,
                                bio, name, tRid, 3, 1, 0);

                        Gson gson = new Gson();
                        PostFileToRepositoryBean postFileToRepositoryBean =
                                gson.fromJson(returnStr, PostFileToRepositoryBean.class);
                        //postFileToRepositoryBean.getErrorCode()
                        if(!postFileToRepositoryBean.getErrorCode().equals("0000")){
                            PlayMusicAuditionActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "上传失败，请检查是否联网" ,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            fid = postFileToRepositoryBean.getData().getFid();
                            token = postFileToRepositoryBean.getToken();

                            PlayMusicAuditionActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    //页面转换
                                    Bundle bundle = new Bundle();
                                    bundle.putString("fileName", fileName);
                                    bundle.putString("token", token);
                                    bundle.putInt("rid", rid);
                                    bundle.putInt("fid", fid);
                                    Log.d("pppppp", rid + "a"+ fid + "");
                                    Intent intent = new Intent(PlayMusicAuditionActivity.this, ShowHutActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                }
                            });
                        }
                    }
                }).start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.reset();
    }

    class SeekBarThread implements Runnable {
        @Override
        public void run() {
            while (mediaPlayer != null) {
                // 将SeekBar位置设置到当前播放位置
                handler.sendEmptyMessage(mediaPlayer.getCurrentPosition());
                try {
                    // 每100毫秒更新一次位置
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
