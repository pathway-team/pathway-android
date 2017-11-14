package com.pathway.pathway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * Created by Daniel Cregan on 11/12/2017.
 */

public class pop extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.6));
        final Intent intent = getIntent();
        final TextView tv = (TextView) findViewById(R.id.textView12);
        final TextView tv1 = (TextView) findViewById(R.id.textView13);
        final TextView tv2 = (TextView) findViewById(R.id.textView14);
        final TextView tv3 = (TextView) findViewById(R.id.textView15);
        final String text = intent.getStringExtra("text");
        final String text1 = intent.getStringExtra("text1");
        final String text2 = intent.getStringExtra("text2");
        final String text3 = intent.getStringExtra("text3");
        tv.setText(text);
        tv1.setText(text1);
        tv2.setText(text2);
        tv3.setText(text3);
    }
}
