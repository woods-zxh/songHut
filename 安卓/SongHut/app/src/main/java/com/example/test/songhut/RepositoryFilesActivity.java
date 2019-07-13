package com.example.test.songhut;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.test.mainfrag.TabItemFragment1;
import com.example.test.mainfrag.TabItemFragment2;
import com.example.test.mainfrag.TabItemFragment3;
import com.example.test.mainfrag.TabItemFragment4;
import com.example.test.util.GetInfoUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.fussen.cache.Cache;

/**
 * created by 李军邑
 */
public class RepositoryFilesActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolBar;

    private String jsonStr;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private List<String> headerList;
    private List<Fragment> fragmentList;
    private ContentPagerAdapter contentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_files);

        String cachePath = getApplicationContext().getCacheDir().getPath();
        jsonStr  = Cache.with(getApplicationContext())
                .path(cachePath)
                .getCache("info", String.class);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        toolBar = (android.support.v7.widget.Toolbar)findViewById(R.id.tb_audition);

        initContent();
        initTab();

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
                Intent intent = new Intent();
                setResult(1, intent);
                finish();
                break;
        }
        return true;
    }

    //初始化TabLayout
    private void initTab(){
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.text_balck), ContextCompat.getColor(this, R.color.green2));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.green2));
        ViewCompat.setElevation(tabLayout, 10);
        //与ViewPager关联
        tabLayout.setupWithViewPager(viewPager);
    }

    //初始化ViewPager
    private void initContent(){
        headerList = new ArrayList<>();
        headerList.add("歌词");
        headerList.add("旋律");
        headerList.add("伴奏");
        headerList.add("演唱");
        fragmentList = new ArrayList<>();

        //rid----------------------
        int rid = 1;


        TabItemFragment1 fragment1 = new TabItemFragment1();
        TabItemFragment2 fragment2 = new TabItemFragment2();
        TabItemFragment3 fragment3 = new TabItemFragment3();
        TabItemFragment4 fragment4 = new TabItemFragment4();
        Bundle bundle = new Bundle();
        bundle.putInt("rid",rid);
        bundle.putString("jsonStr",jsonStr);
        fragment1.setArguments(bundle);
        fragment2.setArguments(bundle);
        fragment3.setArguments(bundle);
        fragment4.setArguments(bundle);
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        fragmentList.add(fragment4);


//        for (String s : tabIndicators) {
//            tabFragments.add(TabContentFragment.newInstance(s));
//        }
        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(contentAdapter);
    }


    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return headerList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return headerList.get(position);
        }
    }
}
