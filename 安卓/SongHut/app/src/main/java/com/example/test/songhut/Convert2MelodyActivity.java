package com.example.test.songhut;

import android.app.Instrumentation;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.gsonUtil.CheckTaskStateBean;
import com.example.test.gsonUtil.SetTransferTaskBean;
import com.example.test.util.UploadUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.fussen.cache.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.Thread.sleep;

/**
 * created by 李军邑
 */
public class Convert2MelodyActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.spinner_select_instrument)
    Spinner spinnerSelectInstrument;
    @BindView(R.id.tb_audition)
    Toolbar tbAudition;
    @BindView(R.id.btn_start_convert)
    Button btnStartConvert;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn_pause)
    ImageButton btnPause;
    @BindView(R.id.sb_convert_audition)
    SeekBar sbConvertAudition;
    @BindView(R.id.tv_current_time)
    TextView tvCurrentTime;
    @BindView(R.id.tv_total_time)
    TextView tvTotalTime;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.et_melody_name)
    EditText etMelodyName;
    @BindView(R.id.et_melody_bio)
    EditText etMelodyBio;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.layout_hide)
    ConstraintLayout layoutHide;

    private String jsonStr = "";
    private String token = "";
    private int rid = 0;
    private int fid = 0;
    private int instrument = 0;

    private String downLoadFileUrl = "http://47.106.107.107:8080/api/repository/downLoadFile";

    private String humFileName = "";
    private String convertFileName = "";

    private MediaPlayer mediaPlayer;

    //接受多线程信息，安卓中不允许主线程实现UI更新
    private Handler seekbarHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            sbConvertAudition.setProgress(msg.what);
            tvCurrentTime.setText(formatTime(msg.what));
        }
    };

    private Handler initLayoutHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initHideLayout();
            progressBar.setVisibility(View.GONE);
            layoutHide.setVisibility(View.VISIBLE);
        }
    };

    private Handler failHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(getApplicationContext(), "转换失败，请检查是否联网", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert2_melody);
        ButterKnife.bind(this);

        String cachePath = getApplicationContext().getCacheDir().getPath();
        jsonStr = Cache.with(getApplicationContext())
                .path(cachePath)
                .getCache("info", String.class);
        //从上个界面接受所录歌曲
        humFileName = getIntent().getStringExtra("fileName");
        rid = getIntent().getIntExtra("rid", 0);
        fid = getIntent().getIntExtra("fid", 0);
        token = getIntent().getStringExtra("token");// getIntExtra("rid", 0);
        mediaPlayer = new MediaPlayer();

        //spinner设置响应事件
        spinnerSelectInstrument.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        instrument = 1;
                        break;
                    case 1:
                        instrument = 41;
                        break;
                    case 2:
                        instrument = 66;
                        break;
                    case 3:
                        instrument = 74;
                        break;
                    case 4:
                        instrument = 100;
                        break;
                }
                Log.d("iiiiiiiiiiiiii", instrument+"");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.reset();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_convert:
                progressBar.setVisibility(View.VISIBLE);
                //开启转换任务
                convertFile();
                break;
            case R.id.btn_pause:
                if (mediaPlayer.isPlaying()) {
                    btnPause.setImageDrawable(getDrawable(R.drawable.play_music));
                    mediaPlayer.pause();
                } else {
                    btnPause.setImageDrawable(getDrawable(R.drawable.pause_music));
                    mediaPlayer.start();
                }
                break;
            case R.id.btn_save:
                Intent intent = new Intent(Convert2MelodyActivity.this, MainActivity.class);
                startActivity(intent);
        }
    }

    private void convertFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String setTaskJson;
                if(instrument == 100){
                    setTaskJson = UploadUtil.setTransferTask(token, fid, rid, 1, 1);
                }
                else{
                    setTaskJson = UploadUtil.setTransferTask(token, fid, rid, instrument, 0);
                }
                Log.d("setTaskJson", setTaskJson);
                //解析json
                Gson gson = new Gson();
                SetTransferTaskBean setTransferTaskBean =
                        gson.fromJson(setTaskJson, SetTransferTaskBean.class);
                //判断是否上传成功
                if (!setTransferTaskBean.getErrorCode().equals("0000")) {
                    Convert2MelodyActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            //上传任务未成功
                            Toast.makeText(getApplicationContext(), "转换失败，请检查是否联网", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    try {
                        //上传成功
                        while (true) {
                            String checkJson = UploadUtil.checkTaskState(token, fid, rid);
                            Gson checkGson = new Gson();
                            CheckTaskStateBean checkTaskStateBean =
                                    checkGson.fromJson(checkJson, CheckTaskStateBean.class);
                            if (checkTaskStateBean.getErrorCode().equals("0000")) {
                                //任务完成，下载歌曲
                                String returnToken = checkTaskStateBean.getToken();
                                String returnPath = checkTaskStateBean.getData().getFilePath();
                                downloadFile(returnToken, returnPath, rid);
                                break;
                            } else {
                                sleep(1000);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void downloadFile(String token, String filePath, int rid) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("filePath", filePath)
                .addFormDataPart("rid", rid + "")
                .build();
        final Request request = new Request.Builder().url(downLoadFileUrl).post(body).build();
        final Call call = okHttpClient.newCall(request);
        try {
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(Convert2MelodyActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //初始化录音
                    convertFileName = Environment.getExternalStorageDirectory() + "/pauseRecordDemo/wav/"
                            + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".wav";
                    //从服务器得到输入流对象
                    InputStream is = response.body().byteStream();
                    long sum = 0;
                    File file = new File(convertFileName);
                    if (!file.exists()) {
                        file.createNewFile();
                        Log.d("success", "s");
                        //file.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buf = new byte[1024 * 8];
                    int len = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();

                    //新线程刷新UI界面
                    initLayoutHandler.sendEmptyMessage(0);
                    //new Thread(new LayoutThread()).start();
                }
            });
        } catch (final Exception e) {
            e.printStackTrace();
            Convert2MelodyActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(Convert2MelodyActivity.this, "转换失败，请检查是否联网", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void initHideLayout() {
        btnPause.setImageDrawable(getDrawable(R.drawable.play_music));
        //重置播放器
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(convertFileName);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initSeekBar();
    }

    //初始化SeekBar
    private void initSeekBar() {
        int totalTime = mediaPlayer.getDuration();
        Log.d("TTTTTTTTTTTime", totalTime + "");
        tvTotalTime.setText("/" + formatTime(totalTime));
        //开启SeekBar监听进程
        new Thread(new SeekBarThread()).start();
        sbConvertAudition.setMax(totalTime);
        sbConvertAudition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

    class SeekBarThread implements Runnable {
        @Override
        public void run() {
            while (mediaPlayer != null) {
                // 将SeekBar位置设置到当前播放位置
                seekbarHandler.sendEmptyMessage(mediaPlayer.getCurrentPosition());
                try {
                    // 每100毫秒更新一次位置
                    sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class LayoutThread implements Runnable {
        @Override
        public void run() {
            initLayoutHandler.sendEmptyMessage(0);
        }
    }

    class FailThread implements Runnable {
        @Override
        public void run() {
            failHandler.sendEmptyMessage(0);
        }
    }
}
