package com.example.locationtracker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class firstFragment extends Fragment {
    ImageView im1;
    Button btn;
    Button btn2;
    int page = 0;
    int current = 1;


    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_fragment, container, false);
        //Setting our variables to correct page we need to edit
        im1 = view.findViewById(R.id.ivInfo);
        btn = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        //Check which page are we on, if we are on the first page its for the running app make sure the button text is set correctly
        switch(current){
            case 1:
                im1.setImageResource(R.drawable.run);
                btn.setText("Go on a run");
                btn.setTextColor(Color.BLACK);
                page =1;
                break;

            case 2:
                im1.setImageResource(R.drawable.lifting);
                btn.setVisibility(View.INVISIBLE);
                btn2.setVisibility(View.VISIBLE);
                btn2.setText("Start Workout");
                btn2.setTextColor(Color.BLACK);

                break;
        }
        return view;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getCurrent() {
        return current;
    }
}

