package com.pathway.pathway;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.List;

/**
 * Created by Johnny on 11/18/2017.
 */

public final class RouteCompare {

    private static List<LatLng> in1;
    private static List<LatLng> in2;
    private static Double[][] memArray;
    private static double difference = 30.48; //100 feet, expressed in meters.

    private RouteCompare() {

    }
    

    public static boolean basicCompare(List<LatLng> L1, List<LatLng> L2, int counter) {
        boolean match = false;
        if (counter + 1 >= 4) {
            match = true;
        }
        else if (L1.size() == 0 || L2.size() == 0) {
            return false;
        }
        else if (L1.size() == 1 && L2.size() == 1) {
            if (SphericalUtil.computeDistanceBetween(L1.get(0), L2.get(1)) <= difference) {
                match = true;
            }
        }
        else if (SphericalUtil.computeDistanceBetween(L1.get(0), L2.get(1)) <= difference &&
                SphericalUtil.computeDistanceBetween(L1.get(L1.size()-1), L2.get(L2.size()-1)) <= difference) {
            counter++;
            match = (
                    basicCompare(L1.subList(0, (int)(L1.size()/2.0)),
                            L2.subList(0, (int)(L2.size()/2.0)), counter)
                    &&
                    basicCompare(L1.subList((int)(L1.size()/2.0), L1.size()),
                            L2.subList((int)(L2.size()/2.0), L2.size()), counter)
                    );
            }
        return match;
    }

    public static boolean hausdorffCompare(List<LatLng> L1, List<LatLng> L2) {
        boolean match = false;
        double hDist = 0.0;
        for (LatLng one : L1) {
            double shortest = Double.MAX_VALUE;
            for (LatLng two : L2) {
                double sDist = SphericalUtil.computeDistanceBetween(one, two);
                if (sDist < shortest) {
                    shortest = sDist;
                }
            }
            if (shortest > hDist) {
                hDist = shortest;
            }
        }
        if (hDist < difference) {
            match = true;
        }
        return match;
    }

    public static boolean frechetCompare(List<LatLng> L1, List<LatLng> L2) {
        boolean match = false;
        in1 = L1;
        in2 = L2;

        memArray = new Double[L1.size()][L2.size()];
        for (int i = 0; i < memArray.length; i++) {
            for (int j = 0; j < memArray[i].length; j++) {
                memArray[i][j] = -1.0;
            }
        }

        double result = discreteFrechetDistance(L1.size()-1, L2.size()-1);

        if (result <= difference) {
            match = true;
        }
        return match;
    }

    public static double discreteFrechetDistance(int i, int j) {
        if (memArray[i][j] > -1.0) {
            return memArray[i][j];
        }
        else if (i == 0 && j == 0) {
            memArray[i][j] = SphericalUtil.computeDistanceBetween(in1.get(i), in2.get(j));
        }
        else if (i > 0 && j == 0) {
            memArray[i][j] = Math.max(discreteFrechetDistance(i - 1, 0),
                    SphericalUtil.computeDistanceBetween(
                            in1.get(i),
                            in2.get(j))
            );
        }
        else if (i == 0 && j > 0) {
            memArray[i][j] = Math.max(discreteFrechetDistance(0, j - 1),
                    SphericalUtil.computeDistanceBetween(
                            in1.get(i),
                            in2.get(j))
            );
        }
        else if (i > 0 && j > 0) {
            memArray[i][j] = Math.max(
                    Math.min(
                            discreteFrechetDistance(i - 1, j),
                            Math.min(
                                    discreteFrechetDistance(i - 1, j - 1),
                                    discreteFrechetDistance(i, j - 1))
                                    ),
                    SphericalUtil.computeDistanceBetween(in1.get(i), in2.get(j)));
        }
        else
            memArray[i][j] = Double.MAX_VALUE;

        return memArray[i][j];
    }








}
