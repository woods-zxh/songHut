package com.example.test.mainfrag;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.test.songhut.R;
import com.example.test.util.LyricsAdapter;

import java.util.ArrayList;
import java.util.List;
/**
 * created by 卢羽帆
 */
public class LyricsFragment extends Fragment {
    List<String> lyrics = new ArrayList<>();
    private String text = "东湖之滨 珞珈山上，这是我们美丽的学堂";
    private ListView list_view;

    public LyricsFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lyrics, container, false);

        textToLyrics(text);

        LyricsAdapter adapter = new LyricsAdapter(lyrics);
        list_view = view.findViewById(R.id.list_view);
        list_view.setDivider(null);
        list_view.setAdapter(adapter);

        return view;
    }

    private void textToLyrics(String text) {
        String[] temp = text.split("\\,|\\.+|\\s+|，|。");
        for (String s : temp) {
            lyrics.add(s);
        }
    }
}
