package com.vssve.valarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class WakeChecker extends View {

    int Radius = 20;

    int setcolor = 0;

    ArrayList<Point> CompletedPoints;

    ArrayList<Point> RedDots;
    ArrayList<Point> BlueDots;
    ArrayList<Point> GreenDots;

    Paint Red, Blue, Green;

    int Height, Width;

    boolean first = false;

    int id;

    public WakeChecker(Context context, AttributeSet attrs) {
        super(context,attrs);
        RedDots = new ArrayList<>();
        BlueDots = new ArrayList<>();
        GreenDots = new ArrayList<>();

        CompletedPoints = new ArrayList<>();

        Red = new Paint();
        Red.setColor(Color.RED);
        Red.setStyle(Paint.Style.FILL);

        Blue = new Paint();
        Blue.setColor(Color.BLUE);
        Blue.setStyle(Paint.Style.FILL);

        Green = new Paint();
        Green.setColor(Color.GREEN);
        Green.setStyle(Paint.Style.FILL);

        Radius = (int) (20 * getResources().getDisplayMetrics().density);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!first)
        {
            Height = getHeight();
            Width = getWidth();
            first = true;

            for (int i = 0; i < 5; )
            {
                Random r = new Random();
                int x = r.nextInt(Width - (2 *Radius)) + Radius;
                int y = r.nextInt(Height -(2 * Radius)) + Radius;
                Point p = new Point(x,y);
                boolean fail = false;

                for (int j = 0; j < RedDots.size(); j++)
                {
                    if (getDistance(p,RedDots.get(j)) < 2*Radius)
                    {
                        fail = true;
                        break;
                    }
                }
                for (int j = 0; j < GreenDots.size(); j++)
                {
                    if (getDistance(p,GreenDots.get(j)) < 2*Radius)
                    {
                        fail = true;
                        break;
                    }
                }
                for (int j = 0; j < BlueDots.size(); j++)
                {
                    if (getDistance(p,BlueDots.get(j)) < 2*Radius)
                    {
                        fail = true;
                        break;
                    }
                }
                if (!fail)
                {
                    RedDots.add(p);
                    i++;
                }
            }
            for (int i = 0; i < 5; )
            {
                Random r = new Random();
                int x = r.nextInt(Width - (2 *Radius)) + Radius;
                int y = r.nextInt(Height -(2 * Radius)) + Radius;
                Point p = new Point(x,y);
                boolean fail = false;
                for (int j = 0; j < RedDots.size(); j++)
                {
                    if (getDistance(p,RedDots.get(j)) < 2*Radius)
                    {
                        fail = true;
                        break;
                    }
                }
                for (int j = 0; j < GreenDots.size(); j++)
                {
                    if (getDistance(p,GreenDots.get(j)) < 2*Radius)
                    {
                        fail = true;
                        break;
                    }
                }
                for (int j = 0; j < BlueDots.size(); j++)
                {
                    if (getDistance(p,BlueDots.get(j)) < 2*Radius)
                    {
                        fail = true;
                        break;
                    }
                }
                if (!fail)
                {
                    BlueDots.add(p);
                    i++;
                }
            }
            for (int i = 0; i < 5; )
            {
                Random r = new Random();
                int x = r.nextInt(Width - (2 *Radius)) + Radius;
                int y = r.nextInt(Height -(2 * Radius)) + Radius;
                Point p = new Point(x,y);
                boolean fail = false;
                for (int j = 0; j < RedDots.size(); j++)
                {
                    if (getDistance(p,RedDots.get(j)) < 2*Radius)
                    {
                        fail = true;
                        break;
                    }
                }
                for (int j = 0; j < GreenDots.size(); j++)
                {
                    if (getDistance(p,GreenDots.get(j)) < 2*Radius)
                    {
                        fail = true;
                        break;
                    }
                }
                for (int j = 0; j < BlueDots.size(); j++)
                {
                    if (getDistance(p,BlueDots.get(j)) < 2*Radius)
                    {
                        fail = true;
                        break;
                    }
                }
                if (!fail)
                {
                    GreenDots.add(p);
                    i++;
                }
            }
        }

        for (int i = 0; i < RedDots.size(); i++)
        {
            canvas.drawCircle(RedDots.get(i).x,RedDots.get(i).y,Radius,Red);
        }
        for (int i = 0; i < GreenDots.size(); i++)
        {
            canvas.drawCircle(GreenDots.get(i).x,GreenDots.get(i).y,Radius,Green);
        }
        for (int i = 0; i < BlueDots.size(); i++)
        {
            canvas.drawCircle(BlueDots.get(i).x,BlueDots.get(i).y,Radius,Blue);
        }

    }

    int getDistance(Point p, Point q)
    {
        return (int) Math.sqrt(Math.pow(p.x - q.x,2) + Math.pow(p.y - q.y,2));
    }

    void setSetcolor(int setcolor)
    {
        this.setcolor = setcolor;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                Point p = new Point((int) event.getX(), (int) event.getY());
                Point Q = null;
                int pc = -1;
                for (int i = 0; i < RedDots.size();i++)
                {
                    if (getDistance(p,RedDots.get(i)) < Radius)
                    {
                        pc = 0;
                        Q = RedDots.get(i);
                        break;
                    }
                }
                for (int i = 0; i < GreenDots.size();i++)
                {
                    if (getDistance(p,GreenDots.get(i)) < Radius)
                    {
                        pc = 2;
                        Q = GreenDots.get(i);
                        break;
                    }
                }
                for (int i = 0; i < BlueDots.size();i++)
                {
                    if (getDistance(p,BlueDots.get(i)) < Radius)
                    {
                        pc = 1;
                        Q = BlueDots.get(i);
                        break;
                    }
                }

                if (pc == setcolor)
                {
                    CompletedPoints.add(Q);
                    if (setcolor == 0)
                    {
                        RedDots.remove(Q);
                        if (RedDots.size() == 0)
                        {
                            CloseAlarm();
                        }
                    }
                    else if (setcolor == 1)
                    {
                        BlueDots.remove(Q);
                        if (BlueDots.size() == 0)
                        {
                            CloseAlarm();
                        }
                    }
                    else
                    {
                        GreenDots.remove(Q);
                        if (GreenDots.size() == 0)
                        {
                            CloseAlarm();
                        }
                    }
                }
                else if (pc == -1)
                {
                    Toast.makeText(getContext(),"Try Again", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(),"Start From First Buddy", Toast.LENGTH_SHORT).show();
                    if (setcolor == 0)
                    {
                        for (int i = 0; i < CompletedPoints.size();i++)
                        {
                            RedDots.add(CompletedPoints.get(i));
                        }
                    }
                    else if (setcolor == 1)
                    {
                        for (int i = 0; i < CompletedPoints.size();i++)
                        {
                            BlueDots.add(CompletedPoints.get(i));
                        }
                    }
                    else
                    {
                        for (int i = 0; i < CompletedPoints.size();i++)
                        {
                            GreenDots.add(CompletedPoints.get(i));
                        }
                    }
                    CompletedPoints = new ArrayList<>();
                }
                invalidate();

                return false;

            default:
                return super.onTouchEvent(event);
        }


    }

    public void CloseAlarm()
    {
        SharedPreferences S = getContext().getSharedPreferences("AlarmsData",MODE_PRIVATE);
        SharedPreferences.Editor  E = S.edit();

        int hour = S.getInt("Hour" + id,0);
        int min = S.getInt("Minute" + id, 0);
        boolean repeat = S.getBoolean("repeat" + id, false);
        String Days = S.getString("days" + id, ",");

        if (repeat) {
            Intent intent = new Intent(getContext(), Reciever.class);
            intent.putExtra("id", id);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, intent, 0);

            AlarmManager Al = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            Al.cancel(pendingIntent);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR, hour);
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
        ((AlarmNotification) getContext()).finish();
    }
}
