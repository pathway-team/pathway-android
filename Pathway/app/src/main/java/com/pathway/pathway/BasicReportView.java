package com.pathway.pathway;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class BasicReportView extends AppCompatActivity {
    ListView lsReport;
    BasicReport report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_report_view);
        int rid = getIntent().getExtras().getInt("rid");
        int pid = getIntent().getExtras().getInt("pid");
        String test = new DeviceDBHandler(getApplicationContext()).getRoute(pid);
        List<String> testReports = new DeviceDBHandler(getApplicationContext()).getRouteReports();
        List<String> listReport = new DeviceDBHandler(getApplicationContext()).getRouteReports(pid);
        try {
             report = new BasicReport(listReport.get(listReport.size() - 1));

            //format time seconds into minutes and seconds
            int timeMin = report.getTotalTimeSec() / 60;
            int timeSec = report.getTotalTimeSec() % 60;

            List<String> temp = new ArrayList<>();
            temp.add(String.format("%13s   %2d : %2d", "Time", timeMin, timeSec));
            temp.add(String.format("%13s   %5d mph", "Max Speed", report.getMaxSpeed()));
            temp.add(String.format("%13s   %5d mph", "Average Spd", report.getAvgSpeed()));
            temp.add("View Graph:Spd/Time");


            lsReport = (ListView) findViewById(R.id.listReport);
            lsReport.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, temp));

            lsReport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //check if last item in list view
                    if(i == adapterView.getCount()-1){
                        //if so pass on to ReportPlotView
                        showPlot();
                    }
                }
            });

        }catch(JSONException e){
            Log.d("JSONException", e.getMessage());
        }catch(IndexOutOfBoundsException e){
            Log.d("IOBException", e.getMessage());
        }

    }

    public void showPlot(){
        double[] temp = new double[report.getSpeed_y().size()];
        for (int idx = 0; idx < report.getSpeed_y().size(); idx++){
            temp[idx] = report.getSpeed_y().get(idx);
        }

        Intent i = new Intent(this, ReportPlotView.class);
        i.getExtras().putDoubleArray("speedy", temp);
        i.getExtras().putIntegerArrayList("timex", report.getTime_x());
        startActivity(i);
    }

}
