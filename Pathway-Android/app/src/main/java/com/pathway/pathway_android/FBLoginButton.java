package com.pathway.pathway_android;
import com.pathway.pathway_android.Achievements;
import com.pathway.pathway_android.R;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class FBLoginButton extends AppCompatActivity {

    //creates button and callbackmanager so that facebook API and app can exchange information
    LoginButton loginButton;
    CallbackManager callbackManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loginButton = (LoginButton)findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

        //sets permissions so that data from user's profile can be accessed
        loginButton.setReadPermissions("email", "public_profile", "user_friends");

        final Intent i = new Intent(FBLoginButton.this, Achievements.class);


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                startActivity(i);


                Toast.makeText(getApplicationContext(), "Logging In...", Toast.LENGTH_SHORT).show();

                String userID = loginResult.getAccessToken().getUserId();

                /*

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {



                       displayUserInfo(object);



                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, last_name, email, id");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
    */
            }

            @Override
            public void onCancel() {

                Toast.makeText(getApplicationContext(), "Canceling Login...", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {

            }


        });



    }

    public String getUserID(){
       return Profile.getCurrentProfile().getId();
    }

    public String getFirstName(){
        return Profile.getCurrentProfile().getFirstName();
    }

    public String getLastName(){
        return Profile.getCurrentProfile().getLastName();
    }

    public AccessToken getCurrentAccessToken(){
        return AccessToken.getCurrentAccessToken();
    }





    /*

    public void displayUserInfo(JSONObject object){

        String first_name, last_name, email, id;
        first_name = "";
        last_name = "";
        email = "";
        id = "";


        try {
            first_name = object.getString("first_name");
            last_name = object.getString("last_name");
            email = object.getString("email");
            id = object.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView tv_name, tv_email, tv_id;
        tv_name = (TextView)findViewById(R.id.TV_name);
        tv_email = (TextView)findViewById(R.id.TV_email);
        tv_id = (TextView)findViewById(R.id.TV_id);


        tv_name.setText(first_name + " " + last_name);
        tv_email.setText("Email: " + email);
        tv_id.setText("ID: " + id);

    }
    */



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }




}
