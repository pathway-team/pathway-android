package com.example.daniel.loginregister;

import android.content.Context;
import android.content.Intent;
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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

                Networking n = new Networking();
                //n.execute(url, Networking.NETWORK_STATE_REGISTER);

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

    public class Networking extends AsyncTask{
        public static final int NETWORK_STATE_REGISTER = 1;
        @Override
        protected Object doInBackground(Object[] params){
            getJson((String) params[0], (Integer) params[1]);
            return null;
        }

    }

    private void getJson(String url, int state){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost(url);
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        boolean valid = false;
        switch(state){
            case Networking.NETWORK_STATE_REGISTER:
                //postParameters.add(new BasicNameValuePair("userName", username));
                //postParameters.add(new BasicNameValuePair("password", password));
                valid = true;
                break;
            default:
                break;
        }

        if(valid){
            StringBuffer stringBuffer = new StringBuffer("");
            try{
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParameters);
                request.setEntity(entity);
                HttpResponse response = httpClient.execute(request);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = "";
                String lineSeparator = System.getProperty("line.separator");
                while((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line + lineSeparator);
                }
                bufferedReader.close();

            }
            catch(Exception e){
                e.printStackTrace();
            }
            decodeResultIntoJson(stringBuffer.toString());
        }

        else{

        }

    }

    private void decodeResultIntoJson(String response){

    }


}

