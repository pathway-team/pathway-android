package com.pathway.pathway;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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
    private List<Integer> timestamps;  //relative time in seconds.
    private Double distance;
    private int rid;
    private int pid;
    private String name;
    private String activity;
    private String diffRtng;



    Route() throws JSONException {
        super();
/*        super("{" +
                "\"type\": \"LineString\"," +
                "\"bbox\": [0.0, 0.0, 1.0, 1.0]," +
                "\"coordinates\": [" +
                "	[0.0, 0.0, 0.0]," +
                "	[1.0, 1.0, 1000.0]" +
                " ]," +
                "\"timestamps\": [0,10]," +
                "\"distance\": 0.0," +
                "\"rid\": 0," +
                "\"pid\": 0," +
                "\"name\": \"My Route\"," +
                "\"atype\": \"W\"," +
                "\"diffRtng\": \"A-1\"" +
                "}");

        this.bbox = this.stringToDoubleArray(this.getString("bbox"));
        this.coords = this.stringToArrayDoubleList(this.getString("coordinates"));
        this.timestamps = this.stringToIntList(this.getString("timestamps"));
        this.distance = Double.parseDouble(this.getString("distance"));
        this.rid = Integer.parseInt(this.getString("rid"));
        this.pid = Integer.parseInt(this.getString("pid"));
        this.name = this.getString("name");
        this.activity = this.getString("atype");
        this.diffRtng = this.getString("diffRtng");*/

        this.bbox = new Double[4];
        this.coords = new ArrayList<>();
        this.timestamps = new ArrayList<>();
        this.distance = 0.0;
        this.rid = 0;
        this.pid = 0;
        this.name = "My Route";
        this.activity = "W";
        this.diffRtng = "A-1";

    }

    Route(String routeData) throws JSONException {
        super(routeData);
        this.bbox = this.stringToDoubleArray(this.getString("bbox"));
        this.coords = this.stringToArrayDoubleList(this.getString("coordinates"));
        this.timestamps = this.stringToIntList(this.getString("timestamps"));
        this.distance = Double.parseDouble(this.getString("distance"));
        this.rid = Integer.parseInt(this.getString("rid"));
        this.pid = Integer.parseInt(this.getString("pid"));
        this.name = this.getString("name");
        this.activity = this.getString("atype");
        this.diffRtng = this.getString("diffRtng");

    }


    public Double[] stringToDoubleArray(String input) {
        String[] items = input.substring(1, input.length() - 1).split(",");
        Double results[] = new Double[items.length];
        for (int i = 0; i < items.length; i++) {
            results[i] =  Double.parseDouble(items[i].trim());
        }
        return results;
    }

    public Integer[] stringToIntArray(String input) {
        String[] items = input.substring(1, input.length() - 1).split(",");
        Integer results[] = new Integer[items.length];
        for (int i = 0; i < items.length; i++) {
            results[i] =  Integer.parseInt(items[i].trim());
        }
        return results;
    }

    public List<Integer> stringToIntList(String input) {
        String[] items = input.substring(1, input.length() - 1).split(",");
        List<Integer> results = new ArrayList();
        for (int i = 0; i < items.length; i++) {
            results.add(Integer.parseInt(items[i].trim()));
        }
        return results;
    }

    public ArrayList<Double[]> stringToArrayDoubleList(String input) {
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

    public void addTime(int sec) {
        timestamps.add(sec);
    }

    public void setName(String input) {
        this.name = input;
    }

    public void setActivity(String input) {
        this.activity = input;
    }

    public void setBbox(Double min_x, Double min_y, Double max_x, Double max_y) {
        this.bbox[0] = min_x;
        this.bbox[1] = min_y;
        this.bbox[2] = max_x;
        this.bbox[3] = max_y;
    }

    public void setBbox(Double bounds[]) {
        if (bounds.length != 4 || (bounds[0] > bounds[2] || (bounds[1] > bounds[3]))) {
            //handle invalid bounding box
        }
        else {
            for (int i = 0; i < 4; i++) {
                this.bbox[i] = bounds[i];
            }
        }
    }

    public void setCoords(ArrayList<Double[]> coordinates){
        this.coords = coordinates;
    }

    public void setDistance(Double input) {
        this.distance = input;
    }

    protected void setRid(int input) {
        this.rid = input;
    }

    protected void setPid(int input) {
        this.pid = input;
    }

    public void calcBBox() {
        Double min_x = null, min_y = null, max_x = null, max_y = null;
        Double temp[] = new Double[3];
        for (int i = 0; i < coords.size(); i++) {
            temp = coords.get(i);
            if ((min_x == null)) {
                min_x = temp[0];
                min_y = temp[1];
                max_x = temp[0];
                max_y = temp[1];
            }
            if (min_x > temp[0]) {
                min_x = temp[0];
            }
            if (min_y > temp[1]) {
                min_y = temp[1];
            }
            if (max_x < temp[0]) {
                max_x = temp[0];
            }
            if (max_y < temp[1]) {
                max_y = temp[1];
            }
        }
        this.setBbox(min_x, min_y, max_x, max_y);
    }

    public List<LatLng> getDrawPoints() {
        List<LatLng> temp = new ArrayList();
        for (int i = 0; i < coords.size(); i++) {
            temp.add(new LatLng(coords.get(i)[1], coords.get(i)[0]));
        }
        return temp;
    }

    public Double[] getBounds() {
        return this.bbox;
    }

    public ArrayList<Double[]> getCoordinates() {
        return this.coords;
    }

    public List<Integer> getTimestamps() {
        return this.timestamps;
    }

    public Double getDistance() {
        return this.distance;
    }

    public Integer getRID() {
        return this.rid;
    }

    public Integer getPID() {
        return this.pid;
    }

    public String getName() {
        return this.name;
    }

    public String getActivity() {
        return this.activity;
    }

    public String getDifficulty() {
        return this.diffRtng;
    }

    public void calcDistance() {
        this.distance = 0.0;
        for (int i = 0; i < this.coords.size() - 1; i++) {
            this.distance += SphericalUtil.computeDistanceBetween(
                    new LatLng(this.coords.get(i)[1], this.coords.get(i)[0]),
                    new LatLng(this.coords.get(i+1)[1], this.coords.get(i+1)[0])
            );
        }

    }

    public void buildJSON() {
        StringBuilder sb = new StringBuilder("[");
        this.calcBBox();
        this.calcDistance();
        this.calcDifficulty();

        try {
            this.remove("bbox");
            this.put("bbox", Arrays.toString(this.bbox));
            for (Double[] cd : this.coords) {
                sb.append(Arrays.toString(cd).replace(" ", ""));
            }
            sb.append("]");
            this.remove("coordinates");
            this.put("coordinates", sb.toString().replace("][", "],[").replace(" ", ""));
            this.remove("timestamps");
            this.put("timestamps", Arrays.toString(this.timestamps.toArray()).replace(" ", ""));
            this.put("distance", this.distance);
            this.put("rid", this.rid);
            this.put("pid", this.pid);
            this.put("name", this.name);
            this.put("atype", this.activity);
            this.put("diffRtng", this.diffRtng);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void calcDifficulty() {
        String diff = "";
        double min_elev = this.coords.get(0)[2];
        double max_elev = this.coords.get(0)[2];


        if (this.distance <= 762) {
            diff = "A";
        }
        else if (this.distance <= 1524) {
            diff = "B";
        }
        else if (this.distance <= 2286) {
            diff = "C";
        }
        else if (this.distance <= 3048) {
            diff = "D";
        }
        else if (this.distance <= 3810) {
            diff = "E";
        }
        else if (this.distance <= 4572) {
            diff = "F";
        }

        for (int i = 0; i < this.coords.size(); i++ ) {
            if (this.coords.get(i)[2] < min_elev) {
                min_elev = this.coords.get(i)[2];
            }
            if (this.coords.get(i)[2] > max_elev) {
                max_elev = this.coords.get(i)[2];
            }
        }

        if (max_elev - min_elev <= 60) {
            diff = diff + "-1";
        }
        else if (this.distance <= 120) {
            diff = diff + "-2";
        }
        else if (this.distance <= 180) {
            diff = diff + "-3";
        }
        else if (this.distance <= 240) {
            diff = diff + "-4";
        }
        else {
            diff = diff + "-5";
        }
    }

    public boolean compareBasic(Route input, int counter, double difference) {
        boolean match = false;
        if (counter + 1 > 4) {
            match = true;
        }
        else if (this.length() == 0 || input.length() == 0) {
            return false;
        }
        else if (this.length() == 1 && input.length() == 1){
            if (SphericalUtil.computeDistanceBetween(this.getDrawPoints().get(0),
                    input.getDrawPoints().get(0)) <= difference) {
                match = true;
            }
        }
        else if (true) {

        }
        return false;
    }

    public boolean compareHausdorff(Route input) {
        return false;
    }

    public boolean compareFrechet(Route input) {
        return false;
    }
}
