package com.example.daniel.loginregister;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.daniel.loginregister.R.id.tvAge;
import static com.example.daniel.loginregister.R.id.tvSex;
import static com.example.daniel.loginregister.R.id.tvUsername;
import static com.example.daniel.loginregister.R.id.tvWeight;

public class Editpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpage);

        final EditText etEmail1 = (EditText) findViewById(R.id.etEmail1);
        final EditText etUsername1 = (EditText) findViewById(R.id.etUsername1);
        final TextView etAge1 = (EditText) findViewById(R.id.etAge1);
        final EditText etSex1 = (EditText) findViewById(R.id.etSex1);
        final EditText etWeight1 = (EditText) findViewById(R.id.etWeight1);
        final Button bSave = (Button) findViewById(R.id.bSave);

        final Intent intent = getIntent();
        final String sex = intent.getStringExtra("sex");
        final String email = intent.getStringExtra("email");
        final String username = intent.getStringExtra("username");
        final String age = intent.getStringExtra("age");
        final String weight = intent.getStringExtra("weight");

        etEmail1.setText(email);
        etUsername1.setText(username);
        etAge1.setText(age);
        etSex1.setText(sex);
        etWeight1.setText(weight);

        bSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent editIntent = new Intent(Editpage.this, UserPage.class);
                editIntent.putExtra("email", email);
                editIntent.putExtra("username", username);
                editIntent.putExtra("age", age);
                editIntent.putExtra("sex", sex);
                editIntent.putExtra("weigth", weight);

               try{
                   URL url = new URL("https://www.youtube.com");
                   HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                   conn.setReadTimeout(10000);
                   conn.setConnectTimeout(15000);
                   conn.setRequestMethod("PUT");
                   conn.setDoOutput(true);
                   conn.setDoInput(true);
                   Uri.Builder builder = new Uri.Builder()
                           .appendQueryParameter("username", username)
                           .appendQueryParameter("email", email)
                           .appendQueryParameter("age", age)
                           .appendQueryParameter("sex", sex)
                           .appendQueryParameter("weight", weight);
               } catch(Exception e){
                   e.printStackTrace();
               }

                Editpage.this.startActivity(editIntent);
            }
        });

    }
}
