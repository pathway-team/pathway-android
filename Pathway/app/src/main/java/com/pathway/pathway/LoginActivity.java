package com.pathway.pathway;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
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
*/
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
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
    static Bundle bundle = new Bundle();

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
        bundle.putString("username", "");
        bundle.putString("password", "");

        bLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean result = false;
                httpHandler handler = new httpHandler(getApplicationContext());
                handler.execute();
                if(isLogin.islogin == true){
                    Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                if(isLogin.islogin == false){
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
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

    public int ByGet(){
        int responsecode = -1;
        try{
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            bundle.putString("username", etUsername.getText().toString());
            bundle.putString("password", etPassword.getText().toString());
            String httpurl = "http://138.197.103.225:8000/users/";
            String str = String.format("%s:%s", username, password);
            String encoding = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
            URL url = new URL(httpurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Basic " + encoding);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            responsecode = conn.getResponseCode();
            if(responsecode == HttpURLConnection.HTTP_OK) {
                isLogin.islogin = true;
            }

            conn.connect();

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
            responsecode = ByGet();

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

