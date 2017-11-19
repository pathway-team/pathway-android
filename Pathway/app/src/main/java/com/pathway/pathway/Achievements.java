package com.pathway.pathway;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Achievements extends AppCompatActivity{

    TextView achieve_name1;
    TextView achieve_name2;
    TextView achieve_name3;
    TextView achieve_name4;



    TextView achieve_desc1;
    TextView achieve_desc2;
    TextView achieve_desc3;
    TextView achieve_desc4;



    ImageView achieve_photo1;
    ImageView achieve_photo2;
    ImageView achieve_photo3;
    ImageView achieve_photo4;


    

    DeviceDBHandler obj;
    UserReport userReport;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_layout);

        achieve_name1 = (TextView) findViewById(R.id.achieve_name1);
        achieve_name2 =  (TextView) findViewById(R.id.achieve_name2);
        achieve_name3 =  (TextView) findViewById(R.id.achieve_name3);
        achieve_name4 =  (TextView) findViewById(R.id.achieve_name4);



        achieve_desc1 =  (TextView) findViewById(R.id.achieve_desc1);
        achieve_desc2 =  (TextView) findViewById(R.id.achieve_desc2);
        achieve_desc3 =  (TextView) findViewById(R.id.achieve_desc3);
        achieve_desc4 =  (TextView) findViewById(R.id.achieve_desc4);



        achieve_photo1 = (ImageView) findViewById(R.id.achieve_photo1);
        achieve_photo2 = (ImageView) findViewById(R.id.achieve_photo2);
        achieve_photo3 = (ImageView) findViewById(R.id.achieve_photo3);
        achieve_photo4 = (ImageView) findViewById(R.id.achieve_photo4);




  /*
       obj = new DeviceDBHandler(getApplicationContext());


        try {
            UserReport userReport = new UserReport(obj.getLatestUserReport());


        } catch (JSONException e) {
            e.printStackTrace();
        }



        if(userReport.getNumRoutes() > 0){
            obj.updateAchievement("BabySteps", 1);
            achieve_photo1.setImageResource(R.mipmap.achievement_unlocked);
        }

        if(userReport.getTotalDistance() >= 16093){
            obj.updateAchievement("GoodonMileage", 1);
            achieve_photo1.setImageResource(R.mipmap.achievement_unlocked);
        }

        if(userReport.getTotalTime() >= 86400){
            obj.updateAchievement("DaynNight", 1);
            achieve_photo1.setImageResource(R.mipmap.achievement_unlocked);
        }
      */





    }












}

