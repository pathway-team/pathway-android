package com.pathway.pathway;

import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.util.List;

public class BasicReportView extends AppCompatActivity {
    ListView lsReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_report_view);
        int rid = savedInstanceState.getInt("rid");
        int pid = savedInstanceState.getInt("pid");
        List<String> listReport = DeviceDBHandler(getApplicationContext()).getRouteReports(pid);
        try {
            BasicReport report = new BasicReport(listReport.get(listReport.size() - 1));
            
            lsReport = (ListView) findViewById(R.id.listReport);
            lsReport.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ));
        }catch(JSONException e){
            Log.d("JSONException", e.getMessage());
        }catch(IndexOutOfBoundsException e){
            Log.d("IOBException", e.getMessage());
        }

    }
}
