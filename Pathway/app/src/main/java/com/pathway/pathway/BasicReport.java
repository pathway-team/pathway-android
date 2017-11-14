package com.pathway.pathway;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by eewest on 11/13/17.
 */

public class BasicReport extends JSONObject{
    //current run
    private ArrayList<Double> speed_y;
    private ArrayList<Integer> time_x;
    private double maxSpeed;
    private double avgSpeed;
    private int totalTimeSec;

    public BasicReport(){
        int routeTbl_ID;
        setSpeed_y(new ArrayList<Double>());
        setTime_x(new ArrayList<Integer>());
        setMaxSpeed(0);
        setAvgSpeed(0);
        setTotalTimeSec(0);
        routeTbl_ID = -1;

        try {
            this.put("routeTbl_ID",routeTbl_ID);
            this.put("speed_y", new JSONArray(getSpeed_y().toArray()));
            this.put("time_x", new JSONArray(getTime_x().toArray()));
            this.put("maxSpeed", getMaxSpeed());
            this.put("avgSpeed", getAvgSpeed());
            this.put("totalTimeSec", getTotalTimeSec());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Double> getSpeed_y() {
        return speed_y;
    }

    public void setSpeed_y(ArrayList<Double> speed_y) {
        this.speed_y = speed_y;
    }

    public ArrayList<Integer> getTime_x() {
        return time_x;
    }

    public void setTime_x(ArrayList<Integer> time_x) {
        this.time_x = time_x;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public int getTotalTimeSec() {
        return totalTimeSec;
    }

    public void setTotalTimeSec(int totalTimeSec) {
        this.totalTimeSec = totalTimeSec;
    }
}
