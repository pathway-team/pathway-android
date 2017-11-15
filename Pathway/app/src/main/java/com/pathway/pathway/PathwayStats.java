package com.pathway.pathway;

import android.content.Context;

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

        for(int idx = 0; idx < points.size() - 1; idx++){
            double dist = calcDist(points.get(idx).latitude,points.get(idx+1).latitude,
                    points.get(idx).longitude, points.get(idx+1).longitude, 0, 0);
            speedList.add(dist/3);//3 is a place holder until more functionality is added to route class
        }

        //temporary: simulate timestamps from route class
        ArrayList<Integer> timestamps = new ArrayList<>(speedList.size());
        for(int idx = 0; idx < timestamps.size() - 1; idx++){
            timestamps.add(3);
        }

        report.setSpeed_y(speedList);
        report.setTime_x(timestamps);
        report.setMaxSpeed(findMax(speedList));
        report.setAvgSpeed(calcAvg(speedList));
        report.setRid(r.getRID());
        report.setPid(r.getPID());
        report.setTotalTimeSec(timestamps.size()*3);//temporary until timestamps is setup in route

        return report;
    }

    /**
     *
     * @param c
     * @return
     */
    public static UserReport generateUserReport(Context c){
        DeviceDBHandler handler = new DeviceDBHandler(c);
        List<String> routes = handler.getUserRoutes();

        double totalDistance = 0;
        int totalTime = 0;
        int numRuns = routes.size();
        int numRoutes = handler.getParentRoutes().size();

        for(int idx = 0; idx < routes.size(); idx++){
            try {
                Route r = new Route(routes.get(idx));
                totalDistance += r.getDistance();
                totalTime += r.getTimestamps().get(r.getTimestamps().size());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //add calculated results to the UserReport
        UserReport r = new UserReport();
        r.setNumRoutes(numRoutes);
        r.setNumRuns(numRuns);
        r.setTotalDistance(totalDistance);
        r.setTotalTime(totalTime);

        //add UserReport r to database
        handler.addUserReport(r);

        //return the UserReport r to the caller
        return r;

    }

    private static double calcDist(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {
        // earths radius
        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        //convert distance from meters to miles
        return Math.sqrt(distance) * 0.000621371192;
    }

    private static double findMax(ArrayList<Double> list){
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
