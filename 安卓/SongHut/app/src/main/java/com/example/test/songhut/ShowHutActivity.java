package com.example.test.songhut;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.test.mainfrag.ImageFragment;
import com.example.test.mainfrag.LyricsFragment;
import com.example.test.mainfrag.TabItemFragment1;
import com.example.test.mainfrag.TabItemFragment2;
import com.example.test.mainfrag.TabItemFragment3;
import com.example.test.mainfrag.TabItemFragment4;
import com.example.test.util.GetInfoUtil;
import com.example.test.util.UploadUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.fussen.cache.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * created by 李军邑
 */
public class ShowHutActivity extends AppCompatActivity {

    private String downLoadFileUrl = "http://47.106.107.107:8080/api/repository/downLoadFile";

    private String jsonStr;
    private int rid;
    Map<String, String> mapPath;

    MediaPlayer mediaPlayer1;
    MediaPlayer mediaPlayer2;

    private ImageButton pauseBtn;
    private SeekBar auditionSb;
    private TextView currentTimeTv;
    private TextView totalTimeTv;


    private ViewPager viewPager;
    private ArrayList<Fragment> fragmentList;
    //PagerAdapter pagerAdapter = new ViewAdapter(fragmentList);

    // private ContentPagerAdapter contentAdapter;

    private int currIndex;//当前页卡编号
    private int bmpW;//横线图片宽度
    private int offset;//图片移动的偏移量

    //接受多线程信息，安卓中不允许主线程实现UI更新
    private Handler seekbarHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            auditionSb.setProgress(msg.what);
            currentTimeTv.setText(formatTime(msg.what));
//
//            sbConvertAudition.setProgress(msg.what);
////            tvCurrentTime.setText(formatTime(msg.what));
        }
    };
//
//    private Handler musicHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            //初始化播放器
//            mediaPlayer1 = new MediaPlayer();
//            mediaPlayer1.reset();
//            mediaPlayer2 = new MediaPlayer();
//            mediaPlayer2.reset();
//            try {
//                mediaPlayer1.setDataSource(mapPath.get("type_2"));
//                mediaPlayer1.prepare();
//                mediaPlayer2.setDataSource(mapPath.get("type_4"));
//                mediaPlayer2.prepare();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            initSeekBar();

//
//            sbConvertAudition.setProgress(msg.what);
//            tvCurrentTime.setText(formatTime(msg.what));
        //}
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_hut);
        String fileName = getIntent().getStringExtra("fileName");
//
        pauseBtn = findViewById(R.id.btn_pause);
        auditionSb = findViewById(R.id.sb_convert_audition);
        currentTimeTv = findViewById(R.id.tv_current_time);
        totalTimeTv = findViewById(R.id.tv_total_time);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        initViewPager();

        String cachePath = getApplicationContext().getCacheDir().getPath();
        jsonStr = Cache.with(getApplicationContext())
                .path(cachePath)
                .getCache("info", String.class);
        Log.d("jjjjjj", jsonStr);

        android.support.v7.widget.Toolbar toolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.tb_audition);
        //初始化ToolBar
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowTitleEnabled(false);
            ab.setHomeAsUpIndicator(R.drawable.toolbar_back);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        String convertFileName = Environment.getExternalStorageDirectory() + "/pauseRecordDemo/wav/20190713024922.wav";

                    //初始化播放器
            mediaPlayer1 = new MediaPlayer();
            mediaPlayer1.reset();
            mediaPlayer2 = new MediaPlayer();
            mediaPlayer2.reset();
            try {
                mediaPlayer1.setDataSource(convertFileName);
                mediaPlayer1.prepare();
                mediaPlayer2.setDataSource(fileName);
                mediaPlayer2.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            initSeekBar();

//
//        new Thread(new initThread()).start();
////
////        Map<String, String> map = GetInfoUtil.GetFilesInRepository(jsonStr, 1);
//        for (String key : map.keySet()) {
//            Log.d("eeeeeeeeee", "key= "+ key + " and value= " + map.get(key));
//        }


    }

    /*
     * 初始化ViewPager
     */
    public void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewPager2);
        fragmentList = new ArrayList<Fragment>();
        Fragment secondFragment = new ImageFragment();
        Fragment thirdFragment = new LyricsFragment();
        fragmentList.add(secondFragment);
        fragmentList.add(thirdFragment);

        //给ViewPager设置适配器
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setCurrentItem(0);//设置当前显示标签页为第一页
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());//页面变化时的监听器
    }


    //初始化SeekBar
    private void initSeekBar() {
        int t1 = mediaPlayer1.getDuration();
        int t2 =mediaPlayer1.getDuration();
        int totalTime = (t1>t2) ? t1 : t2;
        totalTimeTv.setText("/" + formatTime(totalTime));
        //开启SeekBar监听进程
        new Thread(new SeekBarThread()).start();
        auditionSb.setMax(totalTime);
        auditionSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer1.seekTo(progress);
                    mediaPlayer2.seekTo(progress);
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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more_btn:
                Intent intent = new Intent(ShowHutActivity.this, RepositoryFilesActivity.class);
                startActivityForResult(intent, 1);
                //startActivity(intent);
                break;
            case R.id.btn_pause:
                if (mediaPlayer1.isPlaying()) {
                    pauseBtn.setImageDrawable(getDrawable(R.drawable.play_music));
                    mediaPlayer1.pause();
                    mediaPlayer2.pause();
                } else {
                    pauseBtn.setImageDrawable(getDrawable(R.drawable.pause_music));
                    mediaPlayer1.start();
                    mediaPlayer2.start();
                }
            break;
        }

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1 && resultCode == 1) {
            //cccccccccccccccccc


//            Log.d("wwwwwwww", "wwwwww");
//            Bundle bundle=data.getExtras();
//            String strResult = bundle.getString("result");
//            Log.i(TAG,"onActivityResult: "+ strResult);
//            Toast.makeText(MainActivity.this, strResult, Toast.LENGTH_LONG).show();
//        }
//    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private int one = offset * 2 + bmpW;//两个相邻页面的偏移量

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            Animation animation = new TranslateAnimation(currIndex * one, arg0 * one, 0, 0);//平移动画
            currIndex = arg0;
            animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
            animation.setDuration(200);//动画持续时间0.2秒
            //image.startAnimation(animation);//是用ImageView来显示动画的
            int i = currIndex + 1;
            //Toast.makeText(MainActivity.this, "您选择了第"+i+"个页卡", Toast.LENGTH_SHORT).show();
        }
    }

    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list;

        public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }

    }

//
//    class initThread implements Runnable {
//        @Override
//        public void run() {
//            String token = GetInfoUtil.GetToken(jsonStr);
//            UploadUtil.getAllInfo(token);
//            String cachePath = getApplicationContext().getCacheDir().getPath();
//            Cache.with(getApplication())
//                    .path(cachePath)
//                    .saveCache("info", jsonStr);
//            Map<String, String> map = GetInfoUtil.GetFilesInRepository(jsonStr, 1);
//            Log.d("mpaeeeeeeaaeeee", "wwwwwww");
//            for (String key : map.keySet()) {
//                downloadFile(token, map.get(key), rid, key);
//                Log.d("mpaeeeeeeeeee", key);
//            }
//
//            musicHandler.sendEmptyMessage(0);
//
//        }
//    }
//
//    private void downloadFile(String token, String filePath, int rid, final String key) {
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//        RequestBody body = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("token", token)
//                .addFormDataPart("filePath", filePath)
//                .addFormDataPart("rid", rid + "")
//                .build();
//        Log.d("mpaeeeeeeeeee", "wwwwwww");
//        final Request request = new Request.Builder().url(downLoadFileUrl).post(body).build();
//        final Call call = okHttpClient.newCall(request);
//        try {
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    Log.d("mpaeeeeeeeeee", "wwwwwwwss");
//                    Toast.makeText(ShowHutActivity.this, "下载失败，请检查是否联网", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    //初始化录音
//                    String SDPath = Environment.getExternalStorageDirectory() + "/pauseRecordDemo/wav/"
//                            + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".wav";
//                    Log.d("mpaeeeeeeeeee", "wwwwawww");
//                    //从服务器得到输入流对象
//                    InputStream is = response.body().byteStream();
//                    long sum = 0;
//                    File file = new File(SDPath);
//                    if (!file.exists()) {
//                        file.createNewFile();
//                        Log.d("success", "s");
//                        //file.mkdirs();
//                    }
//                    Log.d("mpaeeeeeseeeee", "wwwwwww");
//                    FileOutputStream fos = new FileOutputStream(file);
//                    byte[] buf = new byte[1024 * 8];
//                    int len = 0;
//                    while ((len = is.read(buf)) != -1) {
//                        fos.write(buf, 0, len);
//                    }
//                    fos.flush();
//                    mapPath.put(key, SDPath);
//                    Log.d("mpa", mapPath.toString());
//                }
//            });
//        } catch (final Exception e) {
//            e.printStackTrace();
//            ShowHutActivity.this.runOnUiThread(new Runnable() {
//                public void run() {
//                    Toast.makeText(ShowHutActivity.this, "转换失败，请检查是否联网", Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//    }

    class SeekBarThread implements Runnable {
        @Override
        public void run() {
            while (mediaPlayer1 != null) {
                // 将SeekBar位置设置到当前播放位置
                seekbarHandler.sendEmptyMessage(mediaPlayer1.getCurrentPosition());
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
