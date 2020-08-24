package com.vssve.valarms;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class AlarmsListAdapter extends BaseAdapter {

    ArrayList<Alarm> Objects;

    public AlarmsListAdapter(Context context) {
        this.context = context;

        SharedPreferences S = context.getSharedPreferences("AlarmsData",Context.MODE_PRIVATE);
        int total = S.getInt("Total",0);

        Objects = new ArrayList<>();
        for (int i = 0;i<total;i++)
        {
            Objects.add(new Alarm(i,S.getInt("Hour" + i,0),S.getInt("Minute" + i,0),S.getBoolean("state" + i,false),S.getBoolean("repeat" + i,false),S.getBoolean("wakecheck" + i,false),S.getString("ringer" + i,"def"),S.getString("days" + i,",")));
        }

        for (int i = 0; i < total; i++)
        {
            for (int j = 0; j < total - 1;j++)
            {
                if (Objects.get(i).hour > Objects.get(j).hour)
                {
                    Alarm A = Objects.get(i);
                    Objects.set(i,Objects.get(j));
                    Objects.set(j,A);
                }
                else if (Objects.get(i).hour == Objects.get(j).hour && Objects.get(i).min > Objects.get(j).min)
                {
                    Alarm A = Objects.get(i);
                    Objects.set(i,Objects.get(j));
                    Objects.set(j,A);
                }
            }
        }

    }

    Context context;

    @Override
    public int getCount() {
        return Objects.size();
    }

    @Override
    public Object getItem(int i) {
        return Objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Objects.get(i).id;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final View V = LayoutInflater.from(context).inflate(R.layout.alarmslistobject,viewGroup,false);
        final View Edit = V.findViewById(R.id.edit);

        if (Objects.get(i).repeat == true)
        {
            V.findViewById(R.id.repeatobjs).setVisibility(View.VISIBLE);
        }
        else
        {
            V.findViewById(R.id.repeatobjs).setVisibility(View.GONE);
        }

        String[] w = new String[]{"M","T","W","Th","F","Sa","S"};
        for (int j = 1; j < 8; j++)
        {
            if (Objects.get(i).Days.contains("," + w[j - 1] + ","))
            {
                ((ToggleButton)V.findViewById(context.getResources().getIdentifier("w" + j,"id",context.getPackageName()))).setBackgroundColor(context.getColor(R.color.colorAccent));
            }
        }

        if (Objects.get(i).repeat)
        {
            ((TextView)V.findViewById(R.id.rpd)).setText(Objects.get(i).Days.replace(","," "));
        }
        else
        {
            ((TextView)V.findViewById(R.id.rpd)).setText("Once");
        }

        ((Switch)V.findViewById(R.id.switchw)).setChecked(Objects.get(i).state);
        ((ToggleButton)V.findViewById(R.id.repeatbtn)).setChecked(Objects.get(i).repeat);
        ((Switch)V.findViewById(R.id.wakecheck)).setChecked(Objects.get(i).wakeCheck);



        ((Button)V.findViewById(R.id.time)).setText(String.format("%02d:%02d",Objects.get(i).hour,Objects.get(i).min));
        ((Button)V.findViewById(R.id.time)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Edit.getVisibility() == View.VISIBLE)
                {
                    TimePickerDialog A = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int min) {

                            boolean check = false;
                            SharedPreferences S = context.getSharedPreferences("AlarmsData",Context.MODE_PRIVATE);

                            for (int j = 0; j < S.getInt("Total",0 );j++)
                            {
                                if (S.getInt("Hour" + i,0) == hour && S.getInt("Minute" + i,0) == min)
                                {
                                    check = true;
                                    Toast.makeText(context,"Alarm At " + String.format("%02d:%02d",hour,min) + " is Already Available",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                            if (check == false) {

                                Alarm Az = Objects.get(i);
                                Az.setHour(hour, context);
                                Az.setMin(min, context);
                                Objects.set(i, Az);
                                ((Button) V.findViewById(R.id.time)).setText(String.format("%02d:%02d", hour, min));

                                if (Az.state == true) {
                                    Toast.makeText(context,"Alarm is changed to " + String.format("%02d:%02d",hour,min),Toast.LENGTH_SHORT).show();
                                    //AlarmManager
                                }

                            }
                        }
                    },Objects.get(i).hour, Objects.get(i).min,false);
                    A.show();
                }
                else
                {
                    Edit.setVisibility(View.VISIBLE);
                }
            }
        });

        ((Switch) V.findViewById(R.id.switchw)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Alarm A = Objects.get(i);
                A.setState(b,context);
                Objects.set(i,A);
                //AlarmMgr
            }
        });

        ((ToggleButton) V.findViewById((R.id.repeatbtn))).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Alarm A = Objects.get(i);
                A.setRepeat(b,context);
                Objects.set(i,A);
                if (b == true)
                {
                    V.findViewById(R.id.repeatobjs).setVisibility(View.VISIBLE);
                }
                else
                {
                    V.findViewById(R.id.repeatobjs).setVisibility(View.GONE);
                }

                if (Objects.get(i).repeat)
                {
                    ((TextView)V.findViewById(R.id.rpd)).setText(Objects.get(i).Days.replace(","," "));
                }
                else
                {
                    ((TextView)V.findViewById(R.id.rpd)).setText("Once");
                }
            }
        });

        ((Button) V.findViewById(R.id.ringerbtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ((Switch) V.findViewById((R.id.wakecheck))).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Alarm A = Objects.get(i);
                A.setWakeCheck(b,context);
                Objects.set(i,A);
            }
        });


        ((ImageButton)V.findViewById(R.id.cbtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Edit.setVisibility(View.GONE);
            }
        });

        CompoundButton.OnCheckedChangeListener DaysListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
             String is = compoundButton.getText().toString();
             Alarm A = Objects.get(i);
                Log.d("work",is);
                Log.d("work",A.Days);
             if (b == false)
             {
                 A.setDays(A.Days.replace("," + is + ",",","),context);
                 compoundButton.setBackgroundColor(Color.TRANSPARENT);
             }
             else
             {
                 A.setDays(A.Days + is + ",",context);
                 compoundButton.setBackgroundColor(context.getColor(R.color.colorAccent));
             }

                if (Objects.get(i).repeat)
                {
                    ((TextView)V.findViewById(R.id.rpd)).setText(Objects.get(i).Days.replace(","," "));
                }
                else
                {
                    ((TextView)V.findViewById(R.id.rpd)).setText("Once");
                }
            }
        };

        for (int j = 1; j < 8;j++ )
        {
            ((ToggleButton)V.findViewById(context.getResources().getIdentifier("w" + j,"id",context.getPackageName()))).setOnCheckedChangeListener(DaysListener);
        }

        return V;
    }
}


class Alarm
{
    int id;
    int hour;
    int min;
    boolean state;
    boolean repeat;
    boolean wakeCheck;
    String Ringer;
    String Days;

    public Alarm(int id ,int hour, int min, boolean state, boolean repeat, boolean wakeCheck, String ringer, String days) {
        this.id = id;
        this.hour = hour;
        this.min = min;
        this.state = state;
        this.repeat = repeat;
        this.wakeCheck = wakeCheck;
        this.Ringer = ringer;
        this.Days = days;
    }

    public void setHour(int hour,Context context) {
        this.hour = hour;
        SharedPreferences s = context.getSharedPreferences("AlarmsData",Context.MODE_PRIVATE);
        SharedPreferences.Editor E = s.edit();
        E.putInt("Hour" + id,hour);
        E.commit();
    }

    public void setMin(int min,Context context) {
        this.min = min;
        SharedPreferences s = context.getSharedPreferences("AlarmsData",Context.MODE_PRIVATE);
        SharedPreferences.Editor E = s.edit();
        E.putInt("Minute" + id,min);
        E.commit();
    }

    public void setState(boolean state,Context context) {
        this.state = state;
        SharedPreferences s = context.getSharedPreferences("AlarmsData",Context.MODE_PRIVATE);
        SharedPreferences.Editor E = s.edit();
        E.putBoolean("state" + id,state);
        E.commit();
    }

    public void setRepeat(boolean repeat,Context context) {
        this.repeat = repeat;
        SharedPreferences s = context.getSharedPreferences("AlarmsData",Context.MODE_PRIVATE);
        SharedPreferences.Editor E = s.edit();
        E.putBoolean("repeat" + id,repeat);
        E.commit();
    }

    public void setWakeCheck(boolean wakeCheck,Context context) {
        this.wakeCheck = wakeCheck;
        SharedPreferences s = context.getSharedPreferences("AlarmsData",Context.MODE_PRIVATE);
        SharedPreferences.Editor E = s.edit();
        E.putBoolean("wakecheck" + id,wakeCheck);
        E.commit();
    }

    public void setRinger(String ringer,Context context) {
        Ringer = ringer;
        SharedPreferences s = context.getSharedPreferences("AlarmsData",Context.MODE_PRIVATE);
        SharedPreferences.Editor E = s.edit();
        E.putString("ringer" + id,ringer);
        E.commit();
    }

    public void setDays(String days,Context context) {
        Days = days;
        SharedPreferences s = context.getSharedPreferences("AlarmsData",Context.MODE_PRIVATE);
        SharedPreferences.Editor E = s.edit();
        E.putString("days" + id,days);
        E.commit();
    }
}
