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

    TextView tvUsername;
    TextView tvAge;
    TextView tvSex;
    TextView tvWeight;
    TextView tvTtlDist;
    TextView tvTtlRnTm;
    TextView tvRoutesClrd;
    TextView tvRoutesRn;
    TextView tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvAge = (TextView) findViewById(R.id.tvAge);
        tvSex = (TextView) findViewById(R.id.tvSex);
        tvWeight = (TextView) findViewById(R.id.tvWeight);
        tvTtlDist = (TextView) findViewById(R.id.tvTtlDist);
        tvTtlRnTm = (TextView) findViewById(R.id.tvTtlRnTm);
        tvRoutesClrd = (TextView) findViewById(R.id.tvRoutesClrd);
        tvRoutesRn = (TextView) findViewById(R.id.tvRoutesRn);
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
            tvTtlDist.append(TtlDist);
            tvTtlRnTm.append(TtlRnTm);
            tvRoutesClrd.append(RoutesClrd);
            tvRoutesRn.append(RoutesRn);
        } catch (Exception e){
            e.printStackTrace();
        }
        //final Intent intent = getIntent();
        //final String sex = intent.getStringExtra("sex");
        //final String email = intent.getStringExtra("email");
        //final String username = intent.getStringExtra("username");
        //final String age = intent.getStringExtra("age");
        //final String weight = intent.getStringExtra("weight");
        //final String phone = intent.getStringExtra("phone");

        //tvEmail.setText(email);
        //tvUsername.setText(username);
        //tvAge.setText(age);
        //tvSex.setText(sex);
        //tvWeight.setText(weight);
        //tvPhone.setText(phone);

        bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(UserPage.this, Editpage.class);
                //editIntent.putExtra("email", email);
                editIntent.putExtra("username", tvUsername.getText());
                editIntent.putExtra("age", tvAge.getText());
                editIntent.putExtra("gender", tvSex.getText());
                editIntent.putExtra("weight", tvWeight.getText());
                editIntent.putExtra("phonenumber", tvPhone.getText());
                UserPage.this.startActivity(editIntent);
            }
        });
    }

    class httpHandler extends AsyncTask<Void, Void, Boolean> {
        Context context;
        String username;
        String age;
        String sex;
        String weight;
        String phone;

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
            try {
                String username1 = LoginActivity.bundle.getString("username");
                String httpurl = String.format("http://138.197.103.225:8000/users/%s", username1);
                URL url = new URL(httpurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                responsecode = conn.getResponseCode();
                if (responsecode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    String sb1 = sb.toString();
                    in.close();
                    JSONObject jsonObject = new JSONObject(sb1);
                    username = jsonObject.getString("username");
                    age = jsonObject.getString("age");
                    sex = jsonObject.getString("gender");
                    weight = jsonObject.getString("weight");
                    phone = jsonObject.getString("phonenumber");
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            super.onPostExecute(success);
            tvUsername.setText("" + username);
            tvAge.setText("" + age);
            tvSex.setText("" + sex);
            tvWeight.setText("" + weight);
            tvPhone.setText("" + phone);
        }

        @Override
        protected void onCancelled() {

        }
    }

}
