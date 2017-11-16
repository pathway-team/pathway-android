package com.pathway.pathway;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
public class FriendsList extends AppCompatActivity {
    public FriendsList() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_friends_list);
       Intent intent = getIntent();

                String jsondata = intent.getStringExtra("jsondata"); // receiving data from  FBLoginButton.java
        JSONArray friendslist;
                ArrayList<String> friends = new ArrayList<String>();
        try {
                friendslist = new JSONArray(jsondata);
            for (int l=0; l < friendslist.length(); l++) {
                    friends.add(friendslist.getJSONObject(l).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // adapter which populate the friends in listview
                ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, friends);
        ListView listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(FriendsList.this,MainActivity.class);
                startActivity(intent3);
            }
        });
    }


        
    }





