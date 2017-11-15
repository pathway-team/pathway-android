package com.pathway.pathway;

/**
 * Created by Johnny on 11/10/2017.
 */

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Base64;
import android.util.Log;

public class SendData extends AsyncTask<String, Void, Integer> {

    public interface SendDataCallbackInterface {
        // method called when server's data get fetched
        public void sendDataCallback(Integer result);
    }

    private static final String OPEN_DATABASE = "http://138.197.103.225:8000/routes/";


    HttpURLConnection urlConnect;
    String url;
    SendDataCallbackInterface sdCBInterface;
    //JSONObject inRoute;
    Route inRoute;

    SendData(String inURL, Route input, SendDataCallbackInterface cbInterface) {
        this.url = inURL;
        this.inRoute = input;
        this.sdCBInterface = cbInterface;
    }



    @Override
    protected Integer doInBackground(String... params) {
        int responseCode = -1;
        try {
            String userPass = "jebragg:avalon11";
            String encoding = Base64.encodeToString(userPass.getBytes(), Base64.DEFAULT);


            //URL url = new URL(String.format(OPEN_DATABASE, "routes"));
            URL url = new URL(this.url);
            urlConnect = (HttpURLConnection)url.openConnection();
            urlConnect.setRequestProperty("Content-Type", "application/json");
            urlConnect.setRequestProperty("Accept", "application/json");
            urlConnect.setRequestProperty("Authorization", "Basic " + encoding);
            urlConnect.setRequestMethod("POST");
            urlConnect.setDoInput(true);
            urlConnect.setDoOutput(true);

            JSONObject testJson = new JSONObject();

            try {
                testJson.put("min_lat", inRoute.getBounds()[1]);
                testJson.put("min_long", inRoute.getBounds()[0]);
                testJson.put("max_lat", inRoute.getBounds()[3]);
                testJson.put("max_long", inRoute.getBounds()[2]);
                testJson.put("user", "http://web/users/jebragg/");
                testJson.put("routeid", inRoute.getRID());
                testJson.put("parentid", inRoute.getPID());
                testJson.put("data", inRoute.toString());
                testJson.put("atype", inRoute.getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }


            try(DataOutputStream dataOutputStream = new DataOutputStream(urlConnect.getOutputStream())) {
                String j = testJson.toString();
                dataOutputStream.writeBytes(j);
                dataOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

            urlConnect.connect();
            String result = urlConnect.getResponseMessage();
            responseCode = urlConnect.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnect.disconnect();
        }
        return responseCode;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        this.sdCBInterface.sendDataCallback(result);
    }

}