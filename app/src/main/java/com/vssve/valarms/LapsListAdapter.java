package com.vssve.valarms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LapsListAdapter extends BaseAdapter {

    ArrayList<Long> LapsList;
    Context context;

    public LapsListAdapter(ArrayList<Long> lapsList, Context context) {
        LapsList = lapsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return LapsList.size();
    }

    @Override
    public Object getItem(int i) {
        return LapsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View V = LayoutInflater.from(context).inflate(R.layout.lapsitem,viewGroup,false);
        long ms = LapsList.get(i);
        long sec = ms/1000;
        long min = sec/60;
        sec = sec %60;
        ms = ms %1000;
        ms = ms /10;

        long pms = 0,psec = 0,pmin = 0,lon = 0;

        if (i == 1)
        {
            pms = LapsList.get(0);
            lon = pms - (LapsList.get(1) - LapsList.get(0));
            pms = Math.abs(lon);
            psec = pms/1000;
            pmin = psec/60;
            psec = psec % 60;
            pms = pms % 1000;
        }
        else if (i > 1)
        {
            pms = LapsList.get(i - 1) - LapsList.get(i - 2);
            lon = pms - (LapsList.get(i) - LapsList.get(i-1));
            pms = Math.abs(lon);
            psec = pms/1000;
            pmin = psec/60;
            psec = psec % 60;
            pms = pms % 1000;
        }

        ((TextView)V.findViewById(R.id.laptime)).setText( (lon == 0?"" :( (lon > 0 ? "+" : "-") + String.format("%02d:%02d.%02d",pmin,psec,pms) + "\n" ) )+ String.format("%02d:%02d.%02d",min,sec,ms));
        ((TextView)V.findViewById(R.id.no)).setText("" + (i + 1));
        return V;
    }
}
