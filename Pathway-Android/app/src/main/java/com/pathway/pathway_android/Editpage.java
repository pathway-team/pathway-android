package com.pathway.pathway_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pathway.pathway_android.R;
import com.pathway.pathway_android.UserPage;

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
        final int age = intent.getIntExtra("age", -1);
        final int weight = intent.getIntExtra("weight", -1);

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
                Editpage.this.startActivity(editIntent);
            }
        });

    }
}