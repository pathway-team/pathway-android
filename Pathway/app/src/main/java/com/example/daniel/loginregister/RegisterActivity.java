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
import java.net.*;

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
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class RegisterActivity extends AppCompatActivity {
    Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Button bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("hello");

                boolean result = false;
                httpHandler handler = new httpHandler(getApplicationContext());
                handler.execute();

            }
        });
    }

    public int ByPost(){
        int responsecode = -1;
        EditText etUsername = (EditText) findViewById(R.id.etUsername);
        EditText etAge = (EditText) findViewById(R.id.etAge);
        EditText etEmail = (EditText) findViewById(R.id.etEmail1);
        EditText etWeight = (EditText) findViewById(R.id.etWeight);
        EditText etSex = (EditText) findViewById(R.id.etSex);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);
        String username = etUsername.getText()+"";
        String age = etAge.getText()+"";
        String email = etEmail.getText()+"";
        String weight = etWeight.getText()+"";
        String sex = etSex.getText()+"";
        String password = etPassword.getText()+"";
        if(username.length() == 0 || age.length() == 0 || email.length() == 0 || weight.length() == 0 || sex.length() == 0 || password.length() == 0){
            Toast.makeText(c,"Please fill all fields to register", Toast.LENGTH_SHORT).show();
        }
        try {
            System.out.println("you are here");
            URL url = new URL("http://138.197.103.255:8000/users/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("age", age);
            jsonObject.put("gender", sex);
            jsonObject.put("country", weight);

            try (DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream())){
                String j = jsonObject.toString();
                dataOutputStream.writeBytes(j);
                dataOutputStream.flush();
            } catch(Exception e){
                e.printStackTrace();
            }

            conn.connect();
            //new httpPost().execute(String.valueOf(jsonObject));

            responsecode = conn.getResponseCode();
            Log.d("code", Integer.toString(responsecode));
            if(responsecode == HttpURLConnection.HTTP_OK){
                Log.d("hey", "you are here");
                //login user and call nav activity and set up main hub.
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        return responsecode;
    }

    class httpHandler extends AsyncTask<Void, Void, Boolean>{
        Context context;

        public int responsecode;
        httpHandler(Context c){
            context = c;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            responsecode = ByPost();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success){
            super.onPostExecute(success);
        }

        @Override
        protected void onCancelled(){

        }
    }

}
