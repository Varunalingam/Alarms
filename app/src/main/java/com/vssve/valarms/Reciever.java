package com.vssve.valarms;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class Reciever extends BroadcastReceiver {

    public static Ringtone AlarmsSound;
    public static int notifyID = 100;

    public static String CHANNEL_ID = "com.vssve.alarms";// The id of the channel.
    public static String name = "Alarms";

    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("id",0);

        SharedPreferences S = context.getSharedPreferences("AlarmsData",Context.MODE_PRIVATE);

        if (S.getBoolean("snooze" + id, false))
        {
            RingAlarm(context,id);
        }
        else
        {
            if (S.getBoolean("state" + id, true))
            {
                if (S.getBoolean("repeat" + id, false))
                {
                    String[] days = new String[]{"S","M","T","W","Th","F","Sa"};
                    if (S.getString("days" + id, ",").contains(days[Calendar.getInstance().getTime().getDay()]))
                    {
                        if (S.getInt("Hour" + id,0) == Calendar.getInstance().getTime().getHours())
                        {
                            RingAlarm(context,id);
                        }
                        else
                        {
                            SetAlarm(context,id);
                        }
                    }
                    else
                    {
                        SetAlarm(context,id);
                    }
                }
                else
                {
                    if (S.getInt("Hour" + id,0) == Calendar.getInstance().getTime().getHours())
                    {
                        RingAlarm(context,id);
                    }
                    else
                    {
                        SetAlarm(context,id);
                    }
                }
            }
        }
    }

    void RingAlarm(final Context context, final int id)
    {
        SharedPreferences S = context.getSharedPreferences("AlarmsData",Context.MODE_PRIVATE);
        final int hour = Calendar.getInstance().getTime().getHours();
        final int min = Calendar.getInstance().getTime().getMinutes();

        AlarmsSound = RingtoneManager.getRingtone(context, Uri.parse(S.getString("ringer" + id, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString())));
        AlarmsSound.play();

        if (!S.getBoolean("wakecheck" + id,false)) {
            Handler A = new Handler();
            A.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences S = context.getSharedPreferences("AlarmsData", Context.MODE_PRIVATE);

                    if (S.getBoolean("snoozed" + id,true))
                    {
                        if (Reciever.AlarmsSound != null && Reciever.AlarmsSound.isPlaying()) {
                            int NotifyID = 1023;
                            NotificationManager mNotificationManager =
                                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.cancel(notifyID);
                            AlarmsSound.stop();

                            SharedPreferences.Editor E = S.edit();
                            E.putBoolean("snooze" + id, false);
                            E.commit();

                            Notification notification;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                notification = new Notification.Builder(context)
                                        .setContentTitle(String.format("%02d:%02d", hour, min))
                                        .setContentText("Alarm Missed")
                                        .setSmallIcon(R.drawable.iconnoti)
                                        .setChannelId(CHANNEL_ID)
                                        .build();
                            } else {
                                notification = new Notification.Builder(context)
                                        .setContentTitle(String.format("%02d:%02d", hour, min))
                                        .setContentText("Alarm Missed")
                                        .setSmallIcon(R.drawable.iconnoti)
                                        .build();
                            }

                            mNotificationManager =
                                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(NotifyID, notification);

                            int hour = S.getInt("Hour" + id,0);
                            int min = S.getInt("Minute" + id, 0);
                            boolean repeat = S.getBoolean("repeat" + id, false);
                            String Days = S.getString("days" + id, ",");

                            if (repeat) {
                                Intent intent = new Intent(context, Reciever.class);
                                intent.putExtra("id", id);

                                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);

                                AlarmManager Al = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
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
                        }
                    }
                    else {
                        boolean check = false;

                        int h = hour;
                        int m = min + 5;

                        if (m > 59) {
                            h += 1;
                            m = m % 60;
                        }

                        for (int j = 0; j < S.getInt("Total", 0); j++) {
                            if (S.getInt("Hour" + j, 0) == h && S.getInt("Minute" + j, 0) == m && S.getBoolean("state" + j, true) == true) {
                                check = true;
                                break;
                            }
                        }

                        if (!check) {
                            if (Reciever.AlarmsSound != null && Reciever.AlarmsSound.isPlaying()) {

                                int NotifyID = 1023;


                                NotificationManager mNotificationManager =
                                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotificationManager.cancel(notifyID);
                                AlarmsSound.stop();
                                Intent intent = new Intent(context, Reciever.class);
                                intent.putExtra("id", id);

                                SharedPreferences.Editor E = S.edit();
                                E.putBoolean("snooze" + id, true);
                                E.commit();

                                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);

                                AlarmManager Al = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.HOUR_OF_DAY, h);
                                cal.set(Calendar.MINUTE, m);
                                cal.set(Calendar.SECOND, 0);
                                Al.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);


                                Notification notification;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    notification = new Notification.Builder(context)
                                            .setContentTitle(String.format("%02d:%02d", hour, min))
                                            .setContentText("Alarm Snoozed")
                                            .setSmallIcon(R.drawable.iconnoti)
                                            .setChannelId(CHANNEL_ID)
                                            .build();
                                } else {
                                    notification = new Notification.Builder(context)
                                            .setContentTitle(String.format("%02d:%02d", hour, min))
                                            .setContentText("Alarm Snoozed")
                                            .setSmallIcon(R.drawable.iconnoti)
                                            .build();
                                }

                                mNotificationManager =
                                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotificationManager.notify(NotifyID, notification);

                            }
                        }

                    }
                }
            }, 59000);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel androidChannel = new NotificationChannel(CHANNEL_ID,
                    name, NotificationManager.IMPORTANCE_HIGH);
            androidChannel.enableLights(true);
            androidChannel.enableVibration(true);
            androidChannel.setLightColor(Color.BLUE);
            androidChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(androidChannel);

        }

        Intent i = new Intent(context, AlarmNotification.class);
        i.putExtra("id",id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,0);

        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context)
                    .setContentTitle("Its Time " + String.format("%02d:%02d",hour,min) )
                    .setContentText("Click to Close The Alarm!")
                    .setSmallIcon(R.drawable.iconnoti)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setFullScreenIntent(pendingIntent,true)
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .build();
        }
        else
        {
            notification = new Notification.Builder(context)
                    .setContentTitle("Its Time " + String.format("%02d:%02d",hour,min) )
                    .setContentText("Click to Close The Alarm")
                    .setSmallIcon(R.drawable.iconnoti)
                    .setContentIntent(pendingIntent)
                    .setFullScreenIntent(pendingIntent,true)
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .build();
        }

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notifyID,notification);
    }

    void SetAlarm(Context context, int i)
    {
        SharedPreferences S = context.getSharedPreferences("AlarmsData",Context.MODE_PRIVATE);
        int hour = S.getInt("Hour" + i, 0);
        int min = S.getInt("Minute" + i, 0);
        boolean repeat = S.getBoolean("repeat" + i,false);
        String Days = S.getString("days" + i, ",");

        Intent intent = new Intent(context,Reciever.class);
        intent.putExtra("id",i);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,i,intent,0);

        AlarmManager Al = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,hour);
        cal.set(Calendar.MINUTE,min);
        cal.set(Calendar.SECOND,0);

        String[] days = new String[]{"S","M","T","W","Th","F","Sa"};

        if (cal.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
        {
            cal.add(Calendar.DATE,1);
        }
        if (repeat) {
            while (!Days.contains("," + days[cal.getTime().getDay()] + ",")) {
                cal.add(Calendar.DATE, 1);
            }
        }
        Log.d("work",cal.getTime().toString());
        Al.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
    }
}
