package com.example.daniel.loginregister;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.R.attr.data;


public class LoginActivity extends AppCompatActivity {
    Context c;
    EditText etUsername;
    EditText etPassword;
    Button bLogin;
    TextView registerLink;
    TextView FacebookLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        c = this;
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        registerLink = (TextView) findViewById(R.id.tvRegisterHere);
        FacebookLogin = (TextView) findViewById(R.id.tvFacebook);

        bLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String username = etUsername.getText()+"";
                String password = etPassword.getText()+"";
                if(username.length() == 0 || password.length() == 0){
                    Toast.makeText(c, "Please fill in user name and password", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    URL url = new URL("https://www.youtube.com");
                    HttpURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("username", username)
                            .appendQueryParameter("password", password);
                    String query = builder.build().getEncodedQuery();
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();
                    conn.connect();

                    int responsecode = conn.getResponseCode();
                    if(responsecode == HttpURLConnection.HTTP_OK){
                        //call nav activity and set up main hub.
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        FacebookLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent FacebookLogin = new Intent(LoginActivity.this, FacebookL.class);
                LoginActivity.this.startActivity(FacebookLogin);
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
    }
}

