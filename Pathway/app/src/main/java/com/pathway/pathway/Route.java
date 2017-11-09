package com.pathway.pathway;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny on 11/5/2017.
 */

public class Route extends JSONObject {

    private String featType = "LineString";
    private Double[] bbox = new Double[4];  //[west, south, east, north]
    private ArrayList<Double[]> coords;     //longitude, latitude, elevation
    //index i of coords corresponds to index i of timestamp. set and get together.
    private ArrayList<Integer> timestamps;  //relative time in seconds.
    private String id;
    private String name;
    private String activity;
    private String diffRtng;



    Route() {
        super();
        //this.put("type", featType);
        //this.put("bbox", "[0.0,0.0,0.0,0.0]");
        //this.put("coordinates", "[[0,0,0]]");
        //this.put("timestamp","[[0]]");



    }

    Route(String routeData) throws JSONException {
        super(routeData);
        //this.
    }


    public void addPoint(LatLng point) {
        coords.add(new Double[] {point.longitude, point.latitude, 0.0});
    }

    public List<LatLng> getPoints() {
        List<LatLng> temp = new ArrayList();
        for (int i = 0; i < coords.size(); i++) {
            temp.add(new LatLng(coords.get(i)[0], coords.get(i)[1]));
        }
        return temp;
    }

    //public String getJSONString() {
    //    return this.g
    //}

}
