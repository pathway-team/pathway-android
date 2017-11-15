package com.pathway.pathway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import org.json.JSONException;

import java.util.Arrays;
import java.util.List;

public class ReportPlotView extends AppCompatActivity {
    XYPlot plot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_plot_view);
        plot = (XYPlot)findViewById(R.id.plot);
        plot.setTitle("Speed plot");
        DeviceDBHandler db = new DeviceDBHandler(getApplicationContext());
        List<String> reportLists = db.getRouteReports();
        try {
            BasicReport r = new BasicReport(reportLists.get(reportLists.size() - 1));

            XYSeries series1 = new SimpleXYSeries((r.getTime_x()), (r.getSpeed_y()) , "Recent");
        }catch (JSONException e){
            Log.d("JSONException", e.getMessage());
        }
    }
}
