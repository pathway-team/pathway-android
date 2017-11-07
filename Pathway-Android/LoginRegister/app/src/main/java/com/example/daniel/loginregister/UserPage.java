package com.example.daniel.loginregister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        final TextView tvEmail = (TextView) findViewById(R.id.etEmail1);
        final TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
        final TextView tvAge = (TextView) findViewById(R.id.tvAge);
        final TextView tvSex = (TextView) findViewById(R.id.tvSex);
        final TextView tvWeight = (TextView) findViewById(R.id.tvWeight);
        final Button bEdit = (Button) findViewById(R.id.bEdit);

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

        bEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
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
}
