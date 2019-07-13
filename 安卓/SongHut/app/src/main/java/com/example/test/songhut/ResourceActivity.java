package com.example.test.songhut;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.test.adapter.*;
import com.example.test.baseclass.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
/**
 * created by 李军邑
 */
public class ResourceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        final ListView resource_lv = (ListView)findViewById(R.id.resource_list_view);

        //resList数组与adapter绑定
        final List<Resource> resList = new ArrayList<>();
        for(int i=0;i<10;i++){
            Resource resource = new Resource();
            resource.setName("1111");
            resource.setCreators(new String[]{"1111", "2222"});
            resource.setBio("3333");
            resource.setFavorites(4444);
            resource.setForks(5555);
            resList.add(resource);
        }
        ResourceAdapter resAdapter = new ResourceAdapter(
                ResourceActivity.this, resList
        );
        resource_lv.setAdapter(resAdapter);

        //划到底部时继续加载的操作

        //获得ListView中点击歌曲
        resource_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Resource resource = resList.get(position);
            }
        });
    }
}