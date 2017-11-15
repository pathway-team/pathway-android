package com.pathway.pathway;

import android.app.PendingIntent;
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
import org.json.JSONObject;

public class Achievements extends AppCompatActivity{

    TextView achieve_name1;
    TextView achieve_name2;
    TextView achieve_name3;
    TextView achieve_name4;
    TextView achieve_name5;


    TextView achieve_desc1;
    TextView achieve_desc2;
    TextView achieve_desc3;
    TextView achieve_desc4;
    TextView achieve_desc5;


    ImageView achieve_photo1;
    ImageView achieve_photo2;
    ImageView achieve_photo3;
    ImageView achieve_photo4;
    ImageView achieve_photo5;

    Button achieve_button;




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


        achieve_button = (Button) findViewById(R.id.achieve_button);





    }

    class Achievements2 extends JSONObject {



    }








    public void changePicture(View view) {

        achieve_photo1.setImageResource(R.mipmap.achievement_unlocked);
        achieve_photo2.setImageResource(R.mipmap.achievement_unlocked);
        achieve_photo3.setImageResource(R.mipmap.achievement_unlocked);
        achieve_photo4.setImageResource(R.mipmap.achievement_unlocked);
        achieve_photo5.setImageResource(R.mipmap.achievement_unlocked);


    }





}

