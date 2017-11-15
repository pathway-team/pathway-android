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

    private static final String OPEN_DATABASE = "http://138.197.103.225:8000/%s/";


    HttpURLConnection urlConnect;
    String url;
    FetchDataCallbackInterface fdCBInterface;
    FetchElevationCallbackInterface elevCBInterface;
    LatLng bbox;

    private String result;

    FetchData(String inURL, FetchDataCallbackInterface cbInterface) {
        this.url = inURL;
        this.fdCBInterface = cbInterface;
    }

    FetchData(String inURL, LatLng bounds, FetchDataCallbackInterface cbInterface) {
        this.url = inURL;
        this.fdCBInterface = cbInterface;
    }



    @Override
    protected String doInBackground(String... params) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(this.url);
            urlConnect = (HttpURLConnection)url.openConnection();


            urlConnect = (HttpURLConnection) url.openConnection();
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
        this.fdCBInterface.fetchDataCallback(result);
    }
}