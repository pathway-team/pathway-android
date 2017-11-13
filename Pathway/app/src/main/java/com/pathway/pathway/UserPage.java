package com.pathway.pathway;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserPage extends AppCompatActivity {

    TextView tvEmail;
    TextView tvUsername;
    TextView tvAge;
    TextView tvSex;
    TextView tvWeight;
    TextView tvBstAvgSpd;
    TextView tvBstMedSpd;
    TextView tvMedSpd;
    TextView tvMaxSpd;
    TextView tvPaceSpd;
    TextView tvAvgSpd;
    TextView tvBstMaxSpd;
    TextView tvBstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        tvEmail = (TextView) findViewById(R.id.etEmail1);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvAge = (TextView) findViewById(R.id.tvAge);
        tvSex = (TextView) findViewById(R.id.tvSex);
        tvWeight = (TextView) findViewById(R.id.tvWeight);
        tvBstAvgSpd = (TextView) findViewById(R.id.tvBstAvgSpd);
        tvBstMedSpd = (TextView) findViewById(R.id.tvBstMedSpd);
        tvMedSpd = (TextView) findViewById(R.id.tvMedSpd);
        tvMaxSpd = (TextView) findViewById(R.id.tvMaxSpd);
        tvPaceSpd = (TextView) findViewById(R.id.tvPaceSpd);
        tvAvgSpd = (TextView) findViewById(R.id.tvAvgSpd);
        tvBstMaxSpd = (TextView) findViewById(R.id.tvBstMaxSpd);
        tvBstTime = (TextView) findViewById(R.id.tvBstTime);
        Button bEdit = (Button) findViewById(R.id.bEdit);




        JSONObject object = new JSONObject();
        String BstAvgSpd = null;
        String BstMedSpd = null;
        String MedSpd = null;
        String MaxSpd = null;
        String PaceSpd = null;
        String AvgSpd = null;
        String BstMaxSpd = null;
        String BstTime = null;
        try {
            BstAvgSpd = object.getJSONObject("bestAvgSpd").getString("BstAvgSpd");
            BstMedSpd = object.getJSONObject("bestMedSpd").getString("BstMedSpd");
            MedSpd = object.getJSONObject("medSpeed").getString("MedSpd");
            MaxSpd = object.getJSONObject("maxSpeed").getString("MaxSpd");
            PaceSpd = object.getJSONObject("pace_speed").getString("PaceSpd");
            AvgSpd = object.getJSONObject("avgSpeed").getString("AvgSpd");
            BstMaxSpd = object.getJSONObject("bestMaxSpd").getString("BstMaxSpd");
            BstTime = object.getJSONObject("bestTime").getString("BstTime");
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvBstAvgSpd.append(BstAvgSpd);
        tvBstMedSpd.append(BstMedSpd);
        tvMedSpd.append(MedSpd);
        tvMaxSpd.append(MaxSpd);
        tvPaceSpd.append(PaceSpd);
        tvAvgSpd.append(AvgSpd);
        tvBstMaxSpd.append(BstMaxSpd);
        tvBstTime.append(BstTime);

        final Intent intent = getIntent();
        final String sex = intent.getStringExtra("sex");
        final String email = intent.getStringExtra("email");
        final String username = intent.getStringExtra("username");
        final int age = intent.getIntExtra("age", -1);
        final int weight = intent.getIntExtra("weight", -1);

        tvEmail.setText(email);
        tvUsername.setText(username);
        tvAge.setText(age);
        tvSex.setText(sex);
        tvWeight.setText(weight);

        bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(UserPage.this, Editpage.class);
                editIntent.putExtra("email", email);
                editIntent.putExtra("username", username);
                editIntent.putExtra("age", age);
                editIntent.putExtra("sex", sex);
                editIntent.putExtra("weigth", weight);
                UserPage.this.startActivity(editIntent);
            }
        });
    }

    public int ByGet(){
        int responsecode = -1;
        try{
            URL url = new URL("https://www.youtube.com");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setReadTimeout(10000);
            //conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            responsecode = conn.getResponseCode();
            if(responsecode == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = in.readLine()) != null){
                    sb.append(line+"\n");
                }
                String sb1 = sb.toString();
                in.close();
                JSONObject jsonObject = new JSONObject(sb1);
                String username = jsonObject.getJSONObject("username").getString("username");
                String email = jsonObject.getJSONObject("email").getString("email");
                String age = jsonObject.getJSONObject("age").getString("age");
                String sex = jsonObject.getJSONObject("sex").getString("sex");
                String weight = jsonObject.getJSONObject("weight").getString("weight");
                tvEmail.setText(email);
                tvUsername.setText(username);
                tvAge.setText(age);
                tvSex.setText(sex);
                tvWeight.setText(weight);
            }
            conn.connect();
        } catch(Exception e){
            e.printStackTrace();
        }
        return responsecode;
    }

}
