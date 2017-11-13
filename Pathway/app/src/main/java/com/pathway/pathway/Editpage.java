package com.pathway.pathway;

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

/*
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
*/
import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.pathway.pathway.R.id.tvAge;
import static com.pathway.pathway.R.id.tvSex;
import static com.pathway.pathway.R.id.tvUsername;
import static com.pathway.pathway.R.id.tvWeight;

public class Editpage extends AppCompatActivity {
    EditText etEmail1;
    EditText etUsername1;
    EditText etAge1;
    EditText etSex1;
    EditText etWeight1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpage);

        EditText etEmail1 = (EditText) findViewById(R.id.etEmail1);
        EditText etUsername1 = (EditText) findViewById(R.id.etUsername1);
        EditText etAge1 = (EditText) findViewById(R.id.etAge1);
        EditText etSex1 = (EditText) findViewById(R.id.etSex1);
        EditText etWeight1 = (EditText) findViewById(R.id.etWeight1);
        Button bSave = (Button) findViewById(R.id.bSave);

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

                boolean result = false;
                httpHandler handler = new httpHandler(getApplicationContext());
                handler.execute();

                Editpage.this.startActivity(editIntent);
            }
        });

    }

    public int ByPut(){
        int responsecode = -1;
        try {
            URL url = new URL("http://138.197.103.225:8000/users/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setReadTimeout(10000);
            //conn.setConnectTimeout(15000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("PUT");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", etUsername1.getText().toString());
            jsonObject.put("age", etAge1.getText());
            jsonObject.put("gender", etSex1.getText().toString());
            jsonObject.put("weight", etWeight1.getText().toString());

            try(OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream())){
                String j = jsonObject.toString();
                outputStreamWriter.write(j);
                outputStreamWriter.flush();
                outputStreamWriter.close();
            } catch(Exception e){
                e.printStackTrace();
            }

            conn.connect();

            responsecode = conn.getResponseCode();

        } catch (Exception e){
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
            responsecode = ByPut();

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
