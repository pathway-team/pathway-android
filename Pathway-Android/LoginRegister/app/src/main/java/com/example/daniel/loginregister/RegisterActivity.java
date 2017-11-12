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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etAge = (EditText) findViewById(R.id.etAge);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail1);
        final EditText etWeight = (EditText) findViewById(R.id.etWeight);
        final EditText etSex = (EditText) findViewById(R.id.etSex);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("hello");
                String username = etUsername.getText()+"";
                String age = etAge.getText()+"";
                String email = etEmail.getText()+"";
                String weight = etWeight.getText()+"";
                String sex = etSex.getText()+"";
                String password = etPassword.getText()+"";
                if(username.length() == 0 || age.length() == 0 || email.length() == 0 || weight.length() == 0 || sex.length() == 0 || password.length() == 0){
                    Toast.makeText(c,"Please fill all fields to register", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    System.out.println("you are here");
                    URL url = new URL("http://138.197.103.255:8000/users/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //conn.setReadTimeout(10000);
                    //conn.setConnectTimeout(15000);
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

                    DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
                    dataOutputStream.writeBytes(jsonObject.toString());
                    dataOutputStream.flush();
                    dataOutputStream.close();


                    conn.connect();

                    int responsecode = conn.getResponseCode();
                    if(responsecode == HttpURLConnection.HTTP_OK){
                        Log.d("hey", "you are here");
                        //login user and call nav activity and set up main hub.
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
