package com.example.daniel.loginregister;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class RegisterActivity extends AppCompatActivity {
    Context c = this;
    EditText etUsername;
    EditText etAge;
    EditText etEmail;
    EditText etWeight;
    EditText etSex;
    EditText etPassword;
    Button bRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etAge = (EditText) findViewById(R.id.etAge);
        etEmail = (EditText) findViewById(R.id.etEmail1);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etSex = (EditText) findViewById(R.id.etSex);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean result = false;
                httpHandler handler = new httpHandler(getApplicationContext());
                handler.execute();

            }
        });
    }

    public int ByPost(){
        int responsecode = -1;
        try {
            URL url = new URL("http://138.197.103.225:8000/users/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setReadTimeout(10000);
            //conn.setConnectTimeout(15000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", etUsername.getText().toString());
            jsonObject.put("password", "thejoker12");
            jsonObject.put("age", 28);
            jsonObject.put("gender", "M");
            jsonObject.put("country", "USA");

            try (DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream())) {
                String j = jsonObject.toString();
                System.out.println(j);
                dataOutputStream.writeBytes(j);
                dataOutputStream.flush();
            }catch(Exception e){
                e.printStackTrace();
            }


            conn.connect();

            responsecode = conn.getResponseCode();

            if(responsecode == HttpURLConnection.HTTP_OK){
                //login user and call nav activity and set up main hub.
                System.out.println(responsecode);

            }else{
                System.out.println(responsecode);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return responsecode;
    }

    class httpHandler extends AsyncTask<Void, Void, Boolean> {
        Context context;
        public int responsecode;
        httpHandler(Context c) {
            context = c;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            responsecode = ByPost();

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            super.onPostExecute(success);
        }

        @Override
        protected void onCancelled() {

        }
    }
}
