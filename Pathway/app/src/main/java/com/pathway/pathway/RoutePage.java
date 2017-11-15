package com.pathway.pathway;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Cregan on 11/12/2017.
 */

public class RoutePage extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_page);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final JSONObject jsonObject = new JSONObject();
        ArrayList<Route> jsonStuff = new ArrayList<>();
        DeviceDBHandler db = new DeviceDBHandler(getApplicationContext());
        List<String> jsonString = db.getUserRoutes();
        for (String element : jsonString){
            try {
                Route r = new Route(element);
                jsonStuff.add(r);
            } catch(Exception e){
                e.printStackTrace();
            }
        }

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("route1");
        arrayList.add("route2");
        arrayList.add("route3");
        arrayList.add("route4");
        arrayList.add("route5");
        arrayList.add("route6");
        arrayList.add("route7");
        arrayList.add("route8");
        arrayList.add("route9");
        arrayList.add("route10");

        recyclerView.setAdapter(new RecyclerAdapter(jsonStuff));
    }
}
