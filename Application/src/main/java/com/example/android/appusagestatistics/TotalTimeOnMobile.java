package com.example.android.appusagestatistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.RandomAccessFile;

public class TotalTimeOnMobile extends Fragment {
    public long cpu_total_work = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.total_time_mobile, container, false);
        TextView textView = (TextView) v.findViewById(R.id.time_mobile);
        int j = 1;
        String[] entries;
        int cpu_total;
        int cpu_work;

        long[][] cpuUseVal = {{2147483647, 0}, {2147483647, 0}, {2147483647, 0},
                {2147483647, 0}, {2147483647, 0}, {2147483647, 0}, {2147483647, 0}, {2147483647, 0},
                {2147483647, 0}, {2147483647, 0}};
        float percents[] = new float[10];

        for (int i = 0; i <= Runtime.getRuntime().availableProcessors(); i++) {
            try {
                RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
                String load = reader.readLine();

                while (j <= i) {
                    load = reader.readLine();
                    j++;
                }

                j = 1;

                entries = load.split("[ ]+");

                cpu_total = Integer.parseInt(entries[1])
                        + Integer.parseInt(entries[2])
                        + Integer.parseInt(entries[3])
                        + Integer.parseInt(entries[4])
                        + Integer.parseInt(entries[5])
                        + Integer.parseInt(entries[6])
                        + Integer.parseInt(entries[7]);
                cpu_work = Integer.parseInt(entries[1])
                        + Integer.parseInt(entries[2])
                        + Integer.parseInt(entries[3])
                        + Integer.parseInt(entries[6])
                        + Integer.parseInt(entries[7]);
                reader.close();

                if ((cpu_total - cpuUseVal[i][0]) == 0)
                    percents[i] = 0;

                else
                    percents[i] = (float) (cpu_work - cpuUseVal[i][1])
                            / (cpu_total - cpuUseVal[i][0]);

                cpuUseVal[i][0] = cpu_total;
                cpuUseVal[i][1] = cpu_work;
                cpu_total_work += cpuUseVal[i][0];
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        textView.setText("Total Time On Mobile in Minutes: " + cpu_total_work / 36000);
        return v;
    }

    public Boolean checkValue(){
        if(cpu_total_work!=0){
            return Boolean.TRUE;
        }else {
            return Boolean.FALSE;
        }
    }
}
