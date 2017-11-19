package com.pathway.pathway;

/**
 * Created by Johnny on 11/10/2017.
 */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class FetchData extends AsyncTask<String, Void, String> {

    public interface FetchDataCallbackInterface {
        // method called when server's data get fetched
        public void fetchDataCallback (String result);
    }

    public interface FetchElevationCallbackInterface {
        public void fetchElevCallback (String result);
    }



    private HttpURLConnection urlConnect;
    private String url;
    private FetchDataCallbackInterface fdCBInterface;
    private FetchElevationCallbackInterface elevCBInterface;
    private LatLngBounds bbox;
    private LatLng center;

    private String result = "";

    FetchData(String inURL, FetchDataCallbackInterface cbInterface) {
        this.url = inURL;
        this.fdCBInterface = cbInterface;
    }

    FetchData(String inURL, LatLngBounds bounds, FetchDataCallbackInterface cbInterface) {
        this.url = inURL;
        this.bbox = bounds;
        this.fdCBInterface = cbInterface;
    }

    FetchData(LatLng point, FetchElevationCallbackInterface cbInterface) {
        this.url = "http://maps.googleapis.com/maps/api/elevation/" + "json?locations=";
        this.center = point;
        this.elevCBInterface = cbInterface;
    }



    @Override
    protected String doInBackground(String... params) {
        StringBuilder sb = new StringBuilder();
        try {
            URL curl = new URL(this.url);
            if (this.bbox != null) {
                curl = new URL(String.format("%s?min_lat=%s&min_long=%s&max_lat=%s&max_long=%s",
                        this.url, bbox.southwest.latitude,
                        bbox.southwest.longitude,
                        bbox.northeast.latitude,
                        bbox.northeast.longitude));
            }
            else if (this.center != null) {
                curl = new URL(String.format("%s%s,%s",this.url, String.valueOf(center.latitude),
                        String.valueOf(center.longitude)));
            }

            //urlConnect = (HttpURLConnection)curl.openConnection();


            urlConnect = (HttpURLConnection) curl.openConnection();
            InputStream in = new BufferedInputStream(urlConnect.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String tmp;
            while ((tmp = reader.readLine()) != null) {
                sb.append(tmp);
            }

            result = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnect.disconnect();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (center != null) {
            this.elevCBInterface.fetchElevCallback(result);
        }
        this.fdCBInterface.fetchDataCallback(result);
    }
}