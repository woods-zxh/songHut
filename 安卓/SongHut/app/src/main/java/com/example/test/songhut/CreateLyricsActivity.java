package com.example.test.songhut;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.test.util.PerformEdit;

/**
 * created by 李军邑
 */
public class CreateLyricsActivity extends AppCompatActivity {

    private Button complete, save, undo, redo;
    private EditText lyrics;
    private PerformEdit mPerformEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lyrics);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void init() {
        complete = findViewById(R.id.btn_complete);
        save = findViewById(R.id.btn_save);
        undo = findViewById(R.id.btn_undo);
        redo = findViewById(R.id.btn_redo);
        lyrics = findViewById(R.id.et_lyrics);

        mPerformEdit = new PerformEdit(lyrics) {
            @Override
            protected void onTextChanged(Editable s) {
                //文本发生改变,可以是用户输入或者是EditText.setText触发.(setDefaultText的时候不会回调)
                super.onTextChanged(s);
            }
        };

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPerformEdit.undo();
            }
        });

        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPerformEdit.redo();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateLyricsActivity.this, "歌词已保存至素材箱", Toast.LENGTH_SHORT).show();
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
    }

    private void showPopupWindow() {
        View popView = View.inflate(this, R.layout.popupwindow, null);
        Button selectBtn = (Button) popView.findViewById(R.id.selectBtn);
        Button cancel = (Button) popView.findViewById(R.id.cancelBtn);
        Button save = (Button) popView.findViewById(R.id.saveBtn);

        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        final PopupWindow popupWindow = new PopupWindow(popView, weight, height);

        //动画
        //popupWindow.setAnimationStyle(R.style.pop_anim_style);

        //控件得到焦点
        popupWindow.setFocusable(true);
        //点击外部控件消失
        popupWindow.setOutsideTouchable(true);

//        popSelectBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, RESULT_SELECT_IMAGE);
//                popupWindow.dismiss();
//            }
//        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });

        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);
    }
}
