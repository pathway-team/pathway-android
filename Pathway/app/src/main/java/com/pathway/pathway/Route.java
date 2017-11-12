package com.pathway.pathway;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.google.maps.android.SphericalUtil;

/**
 * Created by Johnny on 11/5/2017.
 */

public class Route extends JSONObject {

    private String featType = "LineString";
    private Double[] bbox = new Double[4];  //[west, south, east, north]
    private ArrayList<Double[]> coords;     //longitude, latitude, elevation
    //index i of coords corresponds to index i of timestamp. set and get together.
    private Integer[] timestamps;  //relative time in seconds.
    private Double distance;
    private int rid;
    private int pid;
    private String name;
    private String activity;
    private String diffRtng;



    Route() throws JSONException {
        super("{" +
                "\"type\": \"LineString\"," +
                "\"bbox\": [0.0, 0.0, 1.0, 1.0]," +
                "\"coordinates\": [" +
                "	[0,0,0]," +
                "	[1,1,0]" +
                " ]," +
                "\"timestamps\": [0,10]," +
                "\"distance\": 0.0," +
                "\"rid\": 0," +
                "\"pid\": 0," +
                "\"name\": \"My Route\"," +
                "\"activity\": \"W\"," +
                "\"diffRtng\": \"A-1\"" +
                "}");

        this.bbox = this.stringToDoubleArray(this.getString("bbox"));
        this.coords = this.stringToArrayList(this.getString("coordinates"));
        this.timestamps = this.stringToIntArray(this.getString("timestamps"));
        this.distance = Double.parseDouble(this.getString("distance"));
        this.rid = Integer.parseInt(this.getString("rid"));
        this.pid = Integer.parseInt(this.getString("pid"));
        this.name = this.getString("name");
        this.activity = this.getString("activity");
        this.diffRtng = this.getString("diffRtng");

    }

    Route(String routeData) throws JSONException {
        super(routeData);
        this.bbox = this.stringToDoubleArray(this.getString("bbox"));
        this.coords = this.stringToArrayList(this.getString("coordinates"));
        this.timestamps = this.stringToIntArray(this.getString("timestamps"));
        this.distance = Double.parseDouble(this.getString("distance"));
        this.rid = Integer.parseInt(this.getString("rid"));
        this.pid = Integer.parseInt(this.getString("pid"));
        this.name = this.getString("name");
        this.activity = this.getString("activity");
        this.diffRtng = this.getString("diffRtng");

    }


    public Double[] stringToDoubleArray(String input) {
        String[] items = input.substring(1, input.length() - 1).split(",");
        Double results[] = new Double[items.length];
        for (int i = 0; i < items.length; i++) {
            results[i] =  Double.parseDouble(items[i]);
        }
        return results;
    }

    public Integer[] stringToIntArray(String input) {
        String[] items = input.substring(1, input.length() - 1).split(",");
        Integer results[] = new Integer[items.length];
        for (int i = 0; i < items.length; i++) {
            results[i] =  Integer.parseInt(items[i]);
        }
        return results;
    }

    public ArrayList<Double[]> stringToArrayList(String input) {
        ArrayList<Double[]> results = new ArrayList<Double[]>();
        String[] items = input.substring(1, input.length() - 1)
                .replaceAll("[\\[\\]]", "").split(",");

        for (int i = 0; i < items.length; i = i + 3) {
            Double temp[] = {Double.parseDouble(items[i]),
                             Double.parseDouble(items[i+1]),
                             Double.parseDouble(items[i+2])};
            results.add(temp);
        }

        return results;
    }


    public void addCoords(LatLng point) {
        coords.add(new Double[] {point.longitude, point.latitude, 0.0});
    }

    public void addCoords(LatLng point, Double elev) {
        coords.add(new Double[] {point.longitude, point.latitude, elev});
    }


    public void setBbox(Double min_x, Double min_y, Double max_x, Double max_y) {

    }

    public void setBbox(Double bounds[]) {
        if (bounds.length != 4) {

        }
    }

    public void setCoords(){

    }

    public void calcBBox() {
       // this.bbox = LatLngBounds.
    }

    public List<LatLng> getDrawPoints() {
        List<LatLng> temp = new ArrayList();
        for (int i = 0; i < coords.size(); i++) {
            temp.add(new LatLng(coords.get(i)[1], coords.get(i)[0]));
        }
        return temp;
    }

}
