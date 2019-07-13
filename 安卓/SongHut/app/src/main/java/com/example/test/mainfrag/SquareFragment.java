package com.example.test.mainfrag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.test.songhut.R;
/**
 * created by 卢羽帆
 */
public class SquareFragment extends Fragment {

        public SquareFragment() {

        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            return inflater.inflate(R.layout.fragment_square, container, false);
        }

}
