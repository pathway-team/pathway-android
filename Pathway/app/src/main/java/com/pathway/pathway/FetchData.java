package com.pathway.pathway;

/**
 * Created by Johnny on 11/10/2017.
 */



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

    public class FetchData extends AsyncTask<String, Void, String> {

        private static final String OPEN_DATABASE =
                "http://192.168.0.11:8000/%s/";

        private String result;

        FetchData(String input) {
            this.result = input;
        }

        public String getJSONString() {
            this.execute();
            return this.result;
        }


        @Override
        protected String doInBackground(String... strings) {
            //result = "UNDEFINED";
            try {
                URL url = new URL(String.format(OPEN_DATABASE, "routes"));
                HttpURLConnection connection =
                        (HttpURLConnection)url.openConnection();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String tmp;
                while ((tmp = reader.readLine()) != null) {
                    sb.append(tmp);
                }

                //result = sb.toString();

                JSONArray feature = new JSONArray(sb.toString());
                JSONObject route = new JSONObject(feature.getJSONObject(0).getString("data"));
                result = String.valueOf(new JSONObject(feature.getJSONObject(0).getString("data")));
                //result = String.valueOf(route.getString("data"));

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String temp) {
            result = temp;
        }

    }

