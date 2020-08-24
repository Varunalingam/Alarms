package com.vssve.valarms;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.sql.Struct;
import java.util.Calendar;

public class AlarmsFragment extends Fragment {

    ImageButton AddItems;
    ListView alarms;
    TextView NoAlarmsAvailable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View FragmentView = inflater.inflate(R.layout.alarms_fragment,container,false);

        AddItems = FragmentView.findViewById(R.id.addalarm);
        alarms = FragmentView.findViewById(R.id.listview);
        NoAlarmsAvailable = FragmentView.findViewById(R.id.textView2);

        final SharedPreferences S = getContext().getSharedPreferences("AlarmsData", Context.MODE_PRIVATE);

        if (S.getInt("Total",0 ) > 0)
        {
            alarms.setAdapter(new AlarmsListAdapter(getContext()));
            NoAlarmsAvailable.setVisibility(View.GONE);
        }
        else
        {
            NoAlarmsAvailable.setVisibility(View.VISIBLE);
        }

        AddItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog A = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        int hour = i;
                        int min = i1;

                        boolean check = false;

                        for (int j = 0; j < S.getInt("Total",0 );j++)
                        {
                            if (S.getInt("Hour" + i,0) == hour && S.getInt("Minute" + i,0) == min)
                            {
                                check = true;
                                Toast.makeText(getContext(),"Alarm At " + String.format("%02d:%02d",hour,min) + " is Already set",Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }

                        if (check == false)
                        {
                            NoAlarmsAvailable.setVisibility(View.GONE);
                            int j = S.getInt("Total",0 );
                            SharedPreferences.Editor E = S.edit();
                            E.putInt("Hour"+j,hour);
                            E.putInt("Minute"+j,min);
                            E.putBoolean("state" + j, true);
                            E.putBoolean("repeat" + j, false);
                            E.putBoolean("wakecheck" + j , false);
                            E.putString("ringer" + j, "def");
                            E.putString("days" + j, ",");
                            E.putInt("Total",j + 1);
                            E.commit();
                            alarms.setAdapter(new AlarmsListAdapter(getContext()));
                            Toast.makeText(getContext(),"Alarm at " + String.format("%02d:%02d",hour,min) + "is Set",Toast.LENGTH_SHORT ).show();
                            //AlarmMgr
                        }


                    }
                },Calendar.getInstance().getTime().getHours(),Calendar.getInstance().getTime().getMinutes(),false );

                A.show();
            }
        });

        return FragmentView;
    }
}
