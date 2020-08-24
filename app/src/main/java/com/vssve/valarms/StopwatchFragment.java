package com.vssve.valarms;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Locale;

public class StopwatchFragment extends Fragment {

    TextView Stopwatch;
    ListView Laps;
    ImageButton Play,Reset,LapsBtn;
    ValueAnimator Timer;
    int Long;
    boolean Playing;
    boolean addedonce = false;

    public ArrayList<Long> LapsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View FragmentView = inflater.inflate(R.layout.stopwatch_fragment,container,false);
        Stopwatch = FragmentView.findViewById(R.id.stopwatch);
        Laps = FragmentView.findViewById(R.id.Laps);
        Play = FragmentView.findViewById(R.id.play);
        Reset = FragmentView.findViewById(R.id.reset);
        LapsBtn = FragmentView.findViewById(R.id.laps);

        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Playing)
                {
                    addedonce = false;
                    Play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                    Playing = true;
                    Reset.setVisibility(View.VISIBLE);
                    LapsBtn.setVisibility(View.VISIBLE);
                    if (Timer == null)
                    {
                        LapsList = null;
                        Laps.setVisibility(View.GONE);
                        Long = 0;
                        SetTimer();
                    }
                    else
                    {
                        Timer.resume();
                    }
                }
                else
                {
                    Play.setImageDrawable(getResources().getDrawable(R.drawable.play));
                    Playing = false;
                    Timer.pause();
                }
            }
        });

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reset.setVisibility(View.GONE);
                LapsBtn.setVisibility(View.GONE);
                Play.setImageDrawable(getResources().getDrawable(R.drawable.play));
                Timer.pause();
                Playing = false;
                Timer = null;
            }
        });

        LapsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!addedonce)
                {
                    if (LapsList == null)
                    {
                        LapsList = new ArrayList<>();
                        Laps.setVisibility(View.VISIBLE);
                    }
                    LapsList.add(Long + java.lang.Long.valueOf( (int) Timer.getAnimatedValue()));
                    Laps.setAdapter(new LapsListAdapter(LapsList,getContext()));
                    if (!Playing)
                    {
                        addedonce = true;
                    }
                }
            }
        });

        return FragmentView;
    }

    public void SetTimer()
    {
        Timer = ValueAnimator.ofInt(0,1000);
        Timer.setDuration(1000);
        Timer.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int ms = (int) valueAnimator.getAnimatedValue();
                int sec = Long/1000;
                int min = sec/60;
                sec = sec % 60;
                ms = ms / 10;
                Stopwatch.setText(String.format(Locale.getDefault(),"%02d:%02d.%02d", min, sec,ms));
            }
        });
        Timer.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Long += 1000;
                SetTimer();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        Timer.start();
    }
}
