package com.example.test.util;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.example.test.songhut.RecordActivity;

/**
 * created by 李军邑
 */
public class VoiceLine extends VoiceLineView {

    private boolean isRun = false;




    public VoiceLine(Context context) {
        super(context);
    }

    public VoiceLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VoiceLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isRun){
            super.onDraw(canvas);
        }
    }
}
