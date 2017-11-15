package com.pathway.pathway;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
    TextView tvTtlDist;
    TextView tvTtlRnTm;
    TextView tvRoutesClrd;
    TextView tvRoutesRn;
    TextView tvCaloriesBrnd;
    TextView tvPhone;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        bundle = savedInstanceState;
        tvEmail = (TextView) findViewById(R.id.etEmail1);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvAge = (TextView) findViewById(R.id.tvAge);
        tvSex = (TextView) findViewById(R.id.tvSex);
        tvWeight = (TextView) findViewById(R.id.tvWeight);
        tvTtlDist = (TextView) findViewById(R.id.tvTtlDist);
        tvTtlRnTm = (TextView) findViewById(R.id.tvTtlRnTm);
        tvRoutesClrd = (TextView) findViewById(R.id.tvRoutesClrd);
        tvRoutesRn = (TextView) findViewById(R.id.tvRoutesRn);
        tvCaloriesBrnd = (TextView) findViewById(R.id.tvCaloriesBrned);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        Button bEdit = (Button) findViewById(R.id.bEdit);

        boolean result = false;
        httpHandler handler = new httpHandler(getApplicationContext());
        handler.execute();

        DeviceDBHandler db = new DeviceDBHandler(getApplicationContext());
        try {
            JSONObject object = new JSONObject(String.valueOf(db.getUserReports()));
            String TtlDist = object.getString("Total_Distance");
            String TtlRnTm = object.getString("Total_Runtime");
            String RoutesClrd = object.getString("Number_Routes");
            String RoutesRn = object.getString("Number_Runs");
            //String CaloriesBrnd = object.getString("calories_burned");
            tvTtlDist.append(TtlDist);
            tvTtlRnTm.append(TtlRnTm);
            tvRoutesClrd.append(RoutesClrd);
            tvRoutesRn.append(RoutesRn);
            //tvCaloriesBrnd.append(CaloriesBrnd);
        } catch (Exception e){
            e.printStackTrace();
        }
        final Intent intent = getIntent();
        final String sex = intent.getStringExtra("sex");
        final String email = intent.getStringExtra("email");
        final String username = intent.getStringExtra("username");
        final String age = intent.getStringExtra("age");
        final String weight = intent.getStringExtra("weight");
        final String phone = intent.getStringExtra("phone");

        tvEmail.setText(email);
        tvUsername.setText(username);
        tvAge.setText(age);
        tvSex.setText(sex);
        tvWeight.setText(weight);
        tvPhone.setText(phone);

        bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(UserPage.this, Editpage.class);
                editIntent.putExtra("email", email);
                editIntent.putExtra("username", username);
                editIntent.putExtra("age", age);
                editIntent.putExtra("sex", sex);
                editIntent.putExtra("weigth", weight);
                editIntent.putExtra("phone", phone);
                UserPage.this.startActivity(editIntent);
            }
        });
    }

    public int ByGet(){
        int responsecode = -1;
        try{
            String username1 = bundle.getString("username");
            String httpurl = String.format("http://138.197.103.225:8000/users/%s", username1);
            URL url = new URL(httpurl);
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
                String username = jsonObject.getString("username");
                String email = jsonObject.getString("email");
                String age = jsonObject.getString("age");
                String sex = jsonObject.getString("sex");
                String weight = jsonObject.getString("weight");
                String phone = jsonObject.getString("phone");
                tvEmail.setText(email);
                tvUsername.setText(username);
                tvAge.setText(age);
                tvSex.setText(sex);
                tvWeight.setText(weight);
                tvPhone.setText(phone);
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
