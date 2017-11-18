package com.pathway.pathway;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eewest on 11/13/17.
 */

public class PathwayStats {

    /**
     *Creates a Basic Report from a route object.
     * @param r
     * @return
     */
    public static BasicReport generateBasicReport(Route r){
        BasicReport report = new BasicReport();
        List<LatLng> points = r.getDrawPoints();
        ArrayList<Double> speedList = new ArrayList<>((points.size()/2)+1);
        ArrayList<Integer> timestamps = new ArrayList<>();

        double totalDistance = 0;

        for(int idx = 0; idx < points.size() - 1; idx++){
            //calc time difference and convert from seconds to hours
            double timeDelta = r.getTimestamps().get(idx+1) - r.getTimestamps().get(idx);
            timeDelta /= 3600;


            double dist = calcDist(points.get(idx).latitude,points.get(idx+1).latitude,
                    points.get(idx).longitude, points.get(idx+1).longitude, r.getCoordinates().get(idx)[2], r.getCoordinates().get(idx+1)[2]);
            totalDistance += dist;
            speedList.add(dist/timeDelta);//3 is a place holder until more functionality is added to route class
            timestamps.add(r.getTimestamps().get(idx));
        }


        report.setSpeed_y(speedList);
        report.setTime_x(timestamps);
        report.setMaxSpeed(findMax(speedList));
        report.setAvgSpeed(calcAvg(speedList));
        try {
            report.setRid(r.getInt("rid"));
            report.setPid(r.getInt("pid"));
        }catch(JSONException e){
            Log.d("JSONException",e.getMessage());
        }
        report.setTotalTimeSec(timestamps.get(timestamps.size() - 1));

        return report;
    }

    /**
     *
     * @param handler
     * @return
     */
    public static UserReport generateUserReport(DeviceDBHandler handler){

        List<String> routes = handler.getUserRoutes();

        double totalDistance = 0;
        int totalTime = 0;
        int numRuns = routes.size();
        int numRoutes = handler.getParentRoutes().size();

        for(int idx = 0; idx < routes.size(); idx++){
            try {
                Route r = new Route(routes.get(idx));
                totalDistance += r.getDistance();
                totalTime += r.getTimestamps().get(r.getTimestamps().size()-1);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //add calculated results to the UserReport
        UserReport r = new UserReport();
        r.setNumRoutes(numRoutes);
        r.setNumRuns(numRuns);
        r.setTotalDistance(totalDistance*0.000621371);
        r.setTotalTime(totalTime);

        //add UserReport r to database
        handler.addUserReport(r);

        //return the UserReport r to the caller
        return r;

    }

    private static double calcDist(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        Location start = new Location("1");
        start.setLatitude(lat1);
        start.setLongitude(lon1);

        Location end = new Location("2");
        end.setLatitude(lat2);
        end.setLongitude(lon2);

        //calc distance in miles
        double distance = start.distanceTo(end) * 0.000621371;

        return distance;
    }

    public static double findMax(ArrayList<Double> list){
        double max = -999;
        for(int idx = 0; idx < list.size(); idx++){
            if(max < list.get(idx)){
                max = list.get(idx);
            }
        }
        return max;
    }

    private static double calcAvg(ArrayList<Double> list){
        double avg = 0;
        for(int idx = 0; idx < list.size(); idx++){
            avg += list.get(idx);
        }
        return avg/list.size();
    }


}
