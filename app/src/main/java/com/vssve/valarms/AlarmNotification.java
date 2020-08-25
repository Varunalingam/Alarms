package com.vssve.valarms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Random;

public class AlarmNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_notification);
        int id = getIntent().getIntExtra("id",0);
        SharedPreferences S = getSharedPreferences("AlarmsData",MODE_PRIVATE);
        SharedPreferences.Editor E = S.edit();
        E.putBoolean("snooze" + id, false);
        if (S.getBoolean("wakecheck" + id, false))
        {
            WakeChecker wcc = findViewById(R.id.wc);
            Random random = new Random();
            int c = Math.abs(random.nextInt()) % 3;
            wcc.setSetcolor(c);
            ((TextView) findViewById(R.id.textView)).setText("Click All the " + new String[] {"Red","Blue","Green"}[c] + " Colored Dots without Clicking the other Dots to close the Alarm");
            wcc.id = id;
        }
        else
        {
            int hour = S.getInt("Hour" + id,0);
            int min = S.getInt("Minute" + id, 0);
            boolean repeat = S.getBoolean("repeat" + id, false);
            String Days = S.getString("days" + id, ",");

            if (repeat) {
                Intent intent = new Intent(this, Reciever.class);
                intent.putExtra("id", id);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);

                AlarmManager Al = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                Al.cancel(pendingIntent);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, min);
                cal.set(Calendar.SECOND, 0);

                String[] days = new String[]{"S", "M", "T", "W", "Th", "F", "Sa"};

                if (cal.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
                    cal.add(Calendar.DATE, 1);
                }
                while (!Days.contains("," + days[cal.getTime().getDay()] + ",")) {
                    cal.add(Calendar.DATE, 1);
                }
                Al.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            }
            else
            {
                E = S.edit();
                E.putBoolean("state" + id, false);
                E.commit();
            }
            Reciever.AlarmsSound.stop();
            Reciever.AlarmsSound = null;
            finish();
        }
    }
}