package com.example.android.appusagestatistics;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.android.appusagestatistics.cab.CustomUsageStats;
import com.example.android.appusagestatistics.cab.UsageListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TotalTimeCabApps extends Fragment {

    private static final String TAG = TotalTimeCabApps.class.getSimpleName();
    public CustomUsageStats customUsageStats;
    public List<CustomUsageStats> customUsageStatsList;

    UsageStatsManager mUsageStatsManager;
    UsageListAdapter mUsageListAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    Button mOpenUsageSettingButton;
    Spinner mSpinner;

    public static TotalTimeCabApps newInstance() {
        TotalTimeCabApps fragment = new TotalTimeCabApps();
        return fragment;
    }

    public TotalTimeCabApps() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsageStatsManager = (UsageStatsManager) getActivity()
                .getSystemService(Context.USAGE_STATS_SERVICE); // "usagestats"
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_usage_statistics, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        mUsageListAdapter = new UsageListAdapter();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_app_usage);
        mLayoutManager = mRecyclerView.getLayoutManager();
        mRecyclerView.scrollToPosition(0);
        mRecyclerView.setAdapter(mUsageListAdapter);
        mOpenUsageSettingButton = (Button) rootView.findViewById(R.id.button_open_usage_setting);
        mSpinner = (Spinner) rootView.findViewById(R.id.spinner_time_span);
        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.action_list, android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            String[] strings = getResources().getStringArray(R.array.action_list);

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StatsUsageInterval statsUsageInterval = StatsUsageInterval
                        .getValue(strings[position]);
                if (statsUsageInterval != null) {
                    List<UsageStats> usageStatsList =
                            getUsageStatistics(statsUsageInterval.mInterval);
                    Collections.sort(usageStatsList, new LastTimeLaunchedComparatorDesc());
                    updateAppsList(usageStatsList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public List<UsageStats> getUsageStatistics(int intervalType) {
        // Get the app statistics since one year ago from the current time.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(intervalType, cal.getTimeInMillis(),
                        System.currentTimeMillis());

        if (queryUsageStats.size() == 0) {
            Log.i(TAG, "The user may not allow the access to apps usage. ");
            Toast.makeText(getActivity(),
                    getString(R.string.explanation_access_to_appusage_is_not_enabled),
                    Toast.LENGTH_LONG).show();
            mOpenUsageSettingButton.setVisibility(View.VISIBLE);
            mOpenUsageSettingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                }
            });
        }
        return queryUsageStats;
    }

    void updateAppsList(List<UsageStats> usageStatsList) {
        String temp = "";
        customUsageStatsList = new ArrayList<>();
        for (int i = 0; i < usageStatsList.size(); i++) {
            customUsageStats = new CustomUsageStats();
            customUsageStats.usageStats = usageStatsList.get(i);
            temp = customUsageStats.usageStats.getPackageName();
            if (temp.equals("com.ubercab")) {
                upList();
            }
            if (temp.equals("com.winit.merucab")) {
                upList();
            }
            if (temp.equals("com.olacabs.customer")) {
                upList();
            }
        }
        mUsageListAdapter.setCustomUsageStatsList(customUsageStatsList);
        mUsageListAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }

    public void upList() {
        try {
            long a = customUsageStats.usageStats.getTotalTimeInForeground() / 60000;
            Drawable appIcon = getActivity().getPackageManager()
                    .getApplicationIcon(customUsageStats.usageStats.getPackageName());
            customUsageStats.appIcon = appIcon;
            customUsageStats.totalTime = "Total Time: " + a + " Minutes";
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, String.format("App Icon is not found for %s",
                    customUsageStats.usageStats.getPackageName()));
            customUsageStats.appIcon = getActivity()
                    .getDrawable(R.drawable.ic_default_app_launcher);
        }
        customUsageStatsList.add(customUsageStats);
    }

    private static class LastTimeLaunchedComparatorDesc implements Comparator<UsageStats> {

        @Override
        public int compare(UsageStats left, UsageStats right) {
            Log.e("TIME", left.getTotalTimeInForeground() + "");
            return Long.compare(right.getLastTimeUsed(), left.getLastTimeUsed());
        }
    }

    static enum StatsUsageInterval {
        DAILY("Daily", UsageStatsManager.INTERVAL_DAILY),
        WEEKLY("Weekly", UsageStatsManager.INTERVAL_WEEKLY),
        MONTHLY("Monthly", UsageStatsManager.INTERVAL_MONTHLY),
        YEARLY("Yearly", UsageStatsManager.INTERVAL_YEARLY);

        private int mInterval;
        private String mStringRepresentation;

        StatsUsageInterval(String stringRepresentation, int interval) {
            mStringRepresentation = stringRepresentation;
            mInterval = interval;
        }

        static StatsUsageInterval getValue(String stringRepresentation) {
            for (StatsUsageInterval statsUsageInterval : values()) {
                if (statsUsageInterval.mStringRepresentation.equals(stringRepresentation)) {
                    return statsUsageInterval;
                }
            }
            return null;
        }
    }
}
