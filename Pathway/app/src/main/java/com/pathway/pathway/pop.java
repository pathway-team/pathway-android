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
        final TextView tv = (TextView) findViewById(R.id.textView);
        final String text = intent.getStringExtra("text");
        tv.setText(text);
    }
}
