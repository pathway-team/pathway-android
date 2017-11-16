package com.pathway.pathway;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by eewest on 11/13/17.
 */

public class UserReport extends JSONObject{
    private double totalDistance;
    private double totalTime;
    private int numRuns;
    private int numRoutes;

    public UserReport(){
        setTotalDistance(0);
        setTotalTime(0);
        setNumRuns(0);
        setNumRoutes(0);

        try {
            this.put("totalDistance", totalDistance);
            this.put("totalTime", totalTime);
            this.put("numRuns", numRuns);
            this.put("numRoutes", numRoutes);
        }catch(JSONException e){
            Log.d("PathwayError", e.getMessage());
        }
    }

    public UserReport(String json) throws JSONException{
        super(json);

        try {
            totalDistance = this.getDouble("totalDistance");
            totalTime = this.getDouble("totalTime");
            numRuns = this.getInt("numRuns");
            this.getInt("numRoutes");
        }catch(JSONException e){
            Log.d("PathwayError", e.getMessage());
        }
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        try {
            this.put("totalDistance", totalDistance);
            this.totalDistance = totalDistance;
        }catch(JSONException e) {
            Log.d("PathwayError", e.getMessage());
        }
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        try {
            this.put("totalTime", totalTime);
            this.totalTime = totalTime;
        }catch(JSONException e){
            Log.d("PathwayError", e.getMessage());
        }

    }

    public int getNumRuns() {
        return numRuns;
    }

    public void setNumRuns(int numRuns) {
        try {
            this.put("numRuns", numRuns);
            this.numRuns = numRuns;
        }catch(JSONException e){
            Log.d("PathwayError", e.getMessage());
        }

    }

    public int getNumRoutes() {
        return numRoutes;
    }

    public void setNumRoutes(int numRoutes) {
        try{
            this.put("numRoutes", numRoutes);
            this.numRoutes = numRoutes;
        }catch(JSONException e){
            Log.d("PathwayError", e.getMessage());
        }

    }
}
