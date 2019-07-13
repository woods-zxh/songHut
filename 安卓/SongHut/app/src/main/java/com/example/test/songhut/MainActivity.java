package com.example.test.songhut;

import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.test.mainfrag.AccountFragment;
import com.example.test.mainfrag.CreateFragment;
import com.example.test.mainfrag.MessageFragment;
import com.example.test.mainfrag.MomentFragment;
import com.example.test.mainfrag.SquareFragment;
import com.example.test.util.popupwindow.PathPopupWindow;

/**
 * created by 卢羽帆
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //底部菜单栏4个TextView和1个ImageButton
    private TextView mTextSquare;
    private TextView mTextMoment;
    private ImageButton mButtonCreate;
    private TextView mTextMessage;
    private TextView mTextAccount;

    //5个Fragment
    private Fragment mSquareFragment;
    private Fragment mMomentFragment;
    private Fragment mCreateFragment;
    private Fragment mMessageFragment;
    private Fragment mAccountFragment;


    //标记当前显示的Fragment
    private int fragmentId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        init();
        //根据传入的Bundle对象判断Activity是正常启动还是销毁重建
        if (savedInstanceState == null) {
            //设置第一个Fragment默认选中
            mTextSquare.performClick();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.text_square:
                setFragment(0);
                break;
            case R.id.text_moment:
                setFragment(1);
                break;
            case R.id.text_message:
                setFragment(2);
                break;
            case R.id.text_account:
                setFragment(3);
                break;
            case R.id.btn_create:
                setFragment(4);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //通过onSaveInstanceState方法保存当前显示的fragment
        outState.putInt("fragment_id", fragmentId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        FragmentManager mFragmentManager = getSupportFragmentManager();
        //通过FragmentManager获取保存在FragmentTransaction中的Fragment实例
        mSquareFragment = (SquareFragment) mFragmentManager.findFragmentByTag("square_fragment");
        mMomentFragment = (MomentFragment) mFragmentManager.findFragmentByTag("moment_fragment");
        mMessageFragment = (MessageFragment) mFragmentManager.findFragmentByTag("message_fragment");
        mAccountFragment = (AccountFragment) mFragmentManager.findFragmentByTag("account_fragment");
        mCreateFragment = (CreateFragment) mFragmentManager.findFragmentByTag("create_fragment");
        //恢复销毁前显示的Fragment
        setFragment(savedInstanceState.getInt("fragment_id"));
    }

    private void init() {
        //初始化控件
        mTextSquare = (TextView) findViewById(R.id.text_square);
        mTextMoment = (TextView) findViewById(R.id.text_moment);
        mTextMessage = (TextView) findViewById(R.id.text_message);
        mTextAccount = (TextView) findViewById(R.id.text_account);
        mButtonCreate = (ImageButton) findViewById(R.id.btn_create);

        //设置监听
        mTextSquare.setOnClickListener(this);
        mTextMoment.setOnClickListener(this);
        mTextMessage.setOnClickListener(this);
        mTextAccount.setOnClickListener(this);
        mButtonCreate.setOnClickListener(this);
    }

    private void setFragment(int index) {
        //获取Fragment管理器
        FragmentManager mFragmentManager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();

        //隐藏所有Fragment
        hideFragments(mTransaction);

        switch (index) {
            default:
                break;
            case 0:
                if (mCreateFragment != null && CreateFragment.popupWindow.isShowing()) {
                    PathPopupWindow.isDimissing = false;
                    CreateFragment.popupWindow.dismiss();
                    CreateFragment.num--;
                }
                //设置菜单栏为选中状态（修改文字和图片）
                mTextSquare.setTextColor(getResources().getColor(R.color.text_color_pressed));
                mTextSquare.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.square_pressed, 0, 0);
                //显示对应Fragment
                if (mSquareFragment == null) {
                    mSquareFragment = new SquareFragment();
                    mTransaction.add(R.id.container, mSquareFragment, "square_fragment");
                } else {
                    mTransaction.show(mSquareFragment);
                }
                break;
            case 1:
                if (mCreateFragment != null && CreateFragment.popupWindow.isShowing()) {
                    PathPopupWindow.isDimissing = false;
                    CreateFragment.popupWindow.dismiss();
                    CreateFragment.num--;
                }
                mTextMoment.setTextColor(getResources().getColor(R.color.text_color_pressed));
                mTextMoment.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.moment_pressed, 0, 0);
                if (mMomentFragment == null) {
                    mMomentFragment = new MomentFragment();
                    mTransaction.add(R.id.container, mMomentFragment, "moment_fragment");
                } else {
                    mTransaction.show(mMomentFragment);
                }
                break;
            case 2:
                if (mCreateFragment != null && CreateFragment.popupWindow.isShowing()) {
                    PathPopupWindow.isDimissing = false;
                    CreateFragment.popupWindow.dismiss();
                    CreateFragment.num--;
                }
                mTextMessage.setTextColor(getResources().getColor(R.color.text_color_pressed));
                mTextMessage.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.message_pressed, 0, 0);
                if (mMessageFragment == null) {
                    mMessageFragment = new MessageFragment();
                    mTransaction.add(R.id.container, mMessageFragment, "message_fragment");
                } else {
                    mTransaction.show(mMessageFragment);
                }
                break;
            case 3:
                if (mCreateFragment != null && CreateFragment.popupWindow.isShowing()) {
                    PathPopupWindow.isDimissing = false;
                    CreateFragment.popupWindow.dismiss();
                    CreateFragment.num--;
                }
                mTextAccount.setTextColor(getResources().getColor(R.color.text_color_pressed));
                mTextAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.account_pressed, 0, 0);
                if (mAccountFragment == null) {
                    mAccountFragment = new AccountFragment();
                    mTransaction.add(R.id.container, mAccountFragment, "account_fragment");
                } else {
                    mTransaction.show(mAccountFragment);
                }
                break;
            case 4:
                PathPopupWindow.isDimissing = true;
                mButtonCreate.setBackgroundResource(R.drawable.create_pressed);
                if (mCreateFragment == null) {
                    mCreateFragment = new CreateFragment();
                    mTransaction.add(R.id.container, mCreateFragment, "create_fragment");
                } else {
                    mTransaction.show(mCreateFragment);
                    if(CreateFragment.num==0){
                        CreateFragment.btn_create.performClick();
                    }
                }
                break;
        }
        //提交事务
        mTransaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mSquareFragment != null) {
            //隐藏Fragment
            transaction.hide(mSquareFragment);
            //将对应菜单栏设置为默认状态
            mTextSquare.setTextColor(getResources().getColor(R.color.text_color));
            mTextSquare.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.square, 0, 0);
        }
        if (mMomentFragment != null) {
            transaction.hide(mMomentFragment);
            mTextMoment.setTextColor(getResources().getColor(R.color.text_color));
            mTextMoment.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.moment, 0, 0);
        }
        if (mMessageFragment != null) {
            transaction.hide(mMessageFragment);
            mTextMessage.setTextColor(getResources().getColor(R.color.text_color));
            mTextMessage.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.message, 0, 0);
        }
        if (mAccountFragment != null) {
            transaction.hide(mAccountFragment);
            mTextAccount.setTextColor(getResources().getColor(R.color.text_color));
            mTextAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.account, 0, 0);
        }
        if (mCreateFragment != null) {
            transaction.hide(mCreateFragment);
            mButtonCreate.setBackgroundResource(R.drawable.create);
        }
    }
}
