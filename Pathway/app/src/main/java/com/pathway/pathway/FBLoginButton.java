package com.pathway.pathway;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FBLoginButton extends AppCompatActivity {

    //creates button and callbackmanager so that facebook API and app can exchange information
    LoginButton loginButton;
    CallbackManager callbackManager;
    public static boolean isFBLogin = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_fb);


        loginButton = (LoginButton)findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

        //sets permissions so that data from user's profile can be accessed
        loginButton.setReadPermissions("email", "public_profile", "user_friends");

        final Intent i = new Intent(FBLoginButton.this, MainActivity.class);


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                isFBLogin = true;




                GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                        loginResult.getAccessToken(),
                        //AccessToken.getCurrentAccessToken(),
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                    Intent intent = new Intent(FBLoginButton.this,FriendsList.class);
                                try {
                                        JSONArray rawName = response.getJSONObject().getJSONArray("data");
                                        intent.putExtra("jsondata", rawName.toString());
                                        //startActivity(intent);
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                            }
                        }
                ).executeAsync();

                startActivity(i);

                Toast.makeText(getApplicationContext(), "Logging In...", Toast.LENGTH_SHORT).show();

                String userID = loginResult.getAccessToken().getUserId();


                /*
                GraphRequest graphRequest = GraphRequest.newPostRequest(AccessToken.getCurrentAccessToken(), "/user_id/notifications",
                        null,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse graphResponse) {
                                Log.d("", "------ graphResponse = " + graphResponse);
                            }
                        }
                );
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
