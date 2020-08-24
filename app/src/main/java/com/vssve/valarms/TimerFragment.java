package com.vssve.valarms;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class TimerFragment extends Fragment {

    public Button HourButton,MinuteButton,SecondButton;
    public ImageButton PlayButton,addbtn;
    public TableLayout tableLayout;

    boolean isPlaying;

    Button currentSelected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View FragmentView = inflater.inflate(R.layout.timer_fragment,container,false);

        HourButton = FragmentView.findViewById(R.id.hour);
        MinuteButton = FragmentView.findViewById(R.id.minute);
        SecondButton = FragmentView.findViewById(R.id.second);

        tableLayout = FragmentView.findViewById(R.id.inputgrid);

        PlayButton = FragmentView.findViewById(R.id.play2);
        addbtn = FragmentView.findViewById(R.id.add10);

        currentSelected = SecondButton;

        SecondButton.setTextColor(getResources().getColor(R.color.colorPrimary));

        HourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HourButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                SecondButton.setTextColor(getResources().getColor(R.color.TextColor));
                MinuteButton.setTextColor(getResources().getColor(R.color.TextColor));
                currentSelected = (Button) view;
            }
        });
        MinuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MinuteButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                SecondButton.setTextColor(getResources().getColor(R.color.TextColor));
                HourButton.setTextColor(getResources().getColor(R.color.TextColor));
                currentSelected = (Button) view;
            }
        });
        SecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SecondButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                HourButton.setTextColor(getResources().getColor(R.color.TextColor));
                MinuteButton.setTextColor(getResources().getColor(R.color.TextColor));
                currentSelected = (Button) view;
            }
        });

        View.OnClickListener buttonListen = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = -1;
                try {
                    number = Integer.parseInt(((Button) view).getText().toString());
                }catch (Exception e){}

                if (number >= 0) {
                    if (Integer.parseInt(currentSelected.getText().toString()) < 10) {
                        currentSelected.setText(currentSelected.getText() + "" + number);
                        if (currentSelected.getText().toString().length() > 2)
                        {
                            currentSelected.setText(currentSelected.getText().toString().substring(1));
                        }
                    }
                }
                else
                {
                    currentSelected.setText( "0" + currentSelected.getText().toString().substring(0,1));
                }

                if (Integer.parseInt(HourButton.getText().toString()) > 0 || Integer.parseInt(MinuteButton.getText().toString()) > 0 || Integer.parseInt(SecondButton.getText().toString()) > 0 )
                {
                    PlayButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    PlayButton.setVisibility(View.INVISIBLE);
                }


            }
        };

        PlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying)
                {
                    PlayButton.setImageDrawable(getResources().getDrawable(R.drawable.play));
                    isPlaying = false;
                    tableLayout.setVisibility(View.VISIBLE);
                    addbtn.setVisibility(View.GONE);
                }
                else
                {
                    isPlaying = true;
                    tableLayout.setVisibility(View.GONE);
                    addbtn.setVisibility(View.VISIBLE);
                    PlayButton.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                    OneSecCheck();
                }
            }
        });
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = Integer.parseInt(HourButton.getText().toString());
                int min = Integer.parseInt(MinuteButton.getText().toString());
                int sec = Integer.parseInt(SecondButton.getText().toString());


                sec += 10;

                min += sec/60;
                hour += min/60;
                min = min %60;
                sec = sec %60;

                HourButton.setText(String.format("%02d",hour));
                MinuteButton.setText(String.format("%02d",min));
                SecondButton.setText(String.format("%02d",sec));

            }
        });

        for (int i = 0; i < 11;i++)
        {
            ((Button) FragmentView.findViewById(getResources().getIdentifier("n" + i,"id", getContext().getPackageName()))).setOnClickListener(buttonListen);
        }

        return FragmentView;
    }

    public void OneSecCheck()
    {
        if (isPlaying) {
            if (Integer.parseInt(HourButton.getText().toString()) > 0 || Integer.parseInt(MinuteButton.getText().toString()) > 0 || Integer.parseInt(SecondButton.getText().toString()) > 0) {
                Handler A = new Handler();
                A.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isPlaying) {
                            if (Integer.parseInt(SecondButton.getText().toString()) == 0) {
                                if (Integer.parseInt(MinuteButton.getText().toString()) == 0) {
                                    HourButton.setText(String.format("%02d", (Integer.parseInt(HourButton.getText().toString()) - 1)));
                                    MinuteButton.setText("59");
                                    SecondButton.setText("59");
                                } else {
                                    MinuteButton.setText(String.format("%02d", (Integer.parseInt(MinuteButton.getText().toString()) - 1)));
                                    SecondButton.setText("59");
                                }
                            } else {
                                SecondButton.setText(String.format("%02d", (Integer.parseInt(SecondButton.getText().toString()) - 1)));
                            }

                            OneSecCheck();
                        }
                    }
                }, 1000);
            }
            else
            {
                PlayButton.setImageDrawable(getResources().getDrawable(R.drawable.play));
                isPlaying = false;
                tableLayout.setVisibility(View.VISIBLE);
                addbtn.setVisibility(View.GONE);
                PlayButton.setVisibility(View.INVISIBLE);

                final Ringtone A = RingtoneManager.getRingtone(getContext(),RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
                A.play();
                Handler s = new Handler();
                s.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        A.stop();
                    }
                },2000);
            }
        }
    }

}
