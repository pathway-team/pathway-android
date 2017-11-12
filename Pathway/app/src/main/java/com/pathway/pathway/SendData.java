package com.pathway.pathway;

/**
 * Created by Johnny on 11/10/2017.
 */

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendData extends AsyncTask<String, Void, Integer> {

    public interface SendDataCallbackInterface {
        // method called when server's data get fetched
        public void fetchDataCallback(String result);
    }

    private static final String OPEN_DATABASE = "http://138.197.103.225:8000/%s/";


    HttpURLConnection urlConnect;
    String url;
    SendDataCallbackInterface fdCBInterface;
    Route inRoute;

    private String result;

    SendData(String inURL, Route input, SendDataCallbackInterface cbInterface) {
        this.url = inURL;
        this.inRoute = input;
        this.fdCBInterface = cbInterface;
    }



    @Override
    protected Integer doInBackground(String... params) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(String.format(OPEN_DATABASE, "routes"));
            //URL url = new URL(this.url);
            urlConnect = (HttpURLConnection)url.openConnection();
            urlConnect.setRequestProperty("Content-Type", "application/json");
            urlConnect.setRequestProperty("Accept", "application/json");
            urlConnect.setRequestMethod("POST");
            urlConnect.setDoInput(true);
            urlConnect.setDoOutput(true);






            urlConnect = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnect.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            //BufferedReader reader = new BufferedReader(
            //        new InputStreamReader(urlConnect.getInputStream()));

            String tmp;
            while ((tmp = reader.readLine()) != null) {
                sb.append(tmp);
            }

            //result = sb.toString();

            JSONArray feature = new JSONArray(sb.toString());
            JSONObject route = new JSONObject(feature.getJSONObject(1).getString("data"));
            result = String.valueOf(new JSONObject(feature.getJSONObject(1).getString("data")));
            //result = String.valueOf(route.getString("data"));

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

