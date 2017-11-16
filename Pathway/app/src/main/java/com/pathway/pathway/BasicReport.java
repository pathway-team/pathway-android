package com.pathway.pathway;

import android.util.Log;

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
    private int pid;
    private int rid;

    public BasicReport(){

        setSpeed_y(new ArrayList<Double>());
        setTime_x(new ArrayList<Integer>());
        setMaxSpeed(0);
        setAvgSpeed(0);
        setTotalTimeSec(0);
        setPid(-1);
        setRid(-1);

        try {
            this.put("pid", getPid());
            this.put("rid", getRid());
            this.put("speed_y", new JSONArray(getSpeed_y().toArray()));
            this.put("time_x", new JSONArray(getTime_x().toArray()));
            this.put("maxSpeed", getMaxSpeed());
            this.put("avgSpeed", getAvgSpeed());
            this.put("totalTimeSec", getTotalTimeSec());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public BasicReport(String jsonString) throws JSONException {
        super(jsonString);
        setPid(this.getInt("pid"));
        this.getInt("rid");
        setSpeed_y(this.stringToDouArray(this.getString("speed_y")));
        setTime_x(this.stringToIntArray(this.getString("time_x")));
        setMaxSpeed(this.getDouble("maxSpeed"));
        setAvgSpeed(this.getDouble("avgSpeed"));
        setTotalTimeSec(this.getInt("totalTimeSec"));
    }

    public ArrayList<Double> getSpeed_y() {
        return speed_y;
    }

    public void setSpeed_y(ArrayList<Double> speed_y) {
        try{
            this.put("speed_y", new JSONArray(speed_y.toArray()));
            this.speed_y = speed_y;
        }catch (JSONException e){
            Log.d("JSONException", e.getMessage());
        }

    }

    public ArrayList<Integer> getTime_x() {
        return time_x;
    }

    public void setTime_x(ArrayList<Integer> time_x) {
        try{
            this.put("time_x", new JSONArray(time_x.toArray()));
            this.time_x = time_x;
        }catch (JSONException e){
            Log.d("JSONException", e.getMessage());
        }

    }


    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        try{
            this.put("maxSpeed", maxSpeed);
            this.maxSpeed = maxSpeed;
        }catch (JSONException e){
            Log.d("JSONException", e.getMessage());
        }

    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        try{
            this.put("avgSpeed", avgSpeed);
            this.avgSpeed = avgSpeed;
        }catch (JSONException e){
            Log.d("JSONException", e.getMessage());
        }

    }

    public int getTotalTimeSec() {
        return totalTimeSec;
    }

    public void setTotalTimeSec(int totalTimeSec) {

        try{
            this.put("totalTimeSec", totalTimeSec);
            this.totalTimeSec = totalTimeSec;
        }catch (JSONException e){
            Log.d("JSONException", e.getMessage());
        }
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {

        try{
            this.put("pid", pid);
            this.pid = pid;
        }catch (JSONException e){
            Log.d("JSONException", e.getMessage());
        }
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        try{
            this.put("rid", rid);
            this.rid = rid;
        }catch (JSONException e){
            Log.d("JSONException", e.getMessage());
        }

    }

    public ArrayList<Integer> stringToIntArray(String input) {
        String[] items = input.substring(1, input.length() - 1).split(",");
        ArrayList<Integer> list = new ArrayList<>(items.length);
        for (int i = 0; i < items.length; i++) {
            list.add(Integer.parseInt(items[i]));
        }
        return list;
    }

    public ArrayList<Double> stringToDouArray(String input) {
        String[] items = input.substring(1, input.length() - 1).split(",");
        ArrayList<Double> list = new ArrayList<>(items.length);
        for (int i = 0; i < items.length; i++) {
            list.add(Double.parseDouble(items[i]));
        }
        return list;
    }

}
