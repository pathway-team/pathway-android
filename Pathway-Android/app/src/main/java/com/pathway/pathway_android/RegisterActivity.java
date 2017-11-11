package com.pathway.pathway_android;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.pathway.pathway_android.R;

public class RegisterActivity extends AppCompatActivity {
    Context c = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etAge = (EditText) findViewById(R.id.etAge);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail1);
        final EditText etWeight = (EditText) findViewById(R.id.etWeight);
        final EditText etSex = (EditText) findViewById(R.id.etSex);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String username = etUsername.getText()+"";
                String age = etAge.getText()+"";
                String email = etEmail.getText()+"";
                String weight = etWeight.getText()+"";
                String sex = etSex.getText()+"";
                String password = etPassword.getText()+"";
                if(username.length() == 0 || age.length() == 0 || email.length() == 0 || weight.length() == 0 || sex.length() == 0 || password.length() == 0){
                    Toast.makeText(c,"Please fill all fields to register", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}