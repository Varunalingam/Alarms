package com.vssve.valarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
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
                            if (S.getInt("Hour" + j,0) == hour && S.getInt("Minute" + j,0) == min)
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
                            E.putString("ringer" + j, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString());

                            int day = Calendar.getInstance().getTime().getDay();

                            String[] days = new String[]{"S","M","T","W","Th","F","Sa"};

                            E.putInt("Total",j + 1);

                            //AlarmMgr
                            Intent intent = new Intent(getContext(),Reciever.class);
                            intent.putExtra("id",j);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),j,intent,0);

                            AlarmManager Al = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.HOUR,hour);
                            cal.set(Calendar.MINUTE,min);
                            cal.set(Calendar.SECOND,0);

                            if (cal.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
                            {
                                cal.add(Calendar.DATE,1);
                                E.putString("days" + j, "," +days[day + 1] + ",");
                            }
                            else
                            {
                                E.putString("days" + j, "," +days[day] + ",");
                            }

                            Al.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);

                            E.commit();
                            alarms.setAdapter(new AlarmsListAdapter(getContext()));

                            Toast.makeText(getContext(),"Alarm at " + String.format("%02d:%02d",hour,min) + " is Set on " + new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"}[cal.getTime().getDay()],Toast.LENGTH_SHORT ).show();
                        }


                    }
                },Calendar.getInstance().getTime().getHours(),Calendar.getInstance().getTime().getMinutes(),false );

                A.show();
            }
        });

        return FragmentView;
    }
}
