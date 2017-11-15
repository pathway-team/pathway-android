package com.pathway.pathway;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Daniel Cregan on 11/12/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    ArrayList<Route> jsonStuff;
    public RecyclerAdapter(ArrayList<Route> jsonStuff){
        this.jsonStuff = jsonStuff;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_child, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.info.setText(jsonStuff.get(position).getString("name"));
            holder.info2.setText(jsonStuff.get(position).getString("diffRtng"));
            holder.info3.setText(jsonStuff.get(position).getString("activity"));
            holder.info4.setText(jsonStuff.get(position).getString("distance"));
            holder.info5.setText(jsonStuff.get(position).getString("timestamps"));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonStuff.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView info;
        TextView info2;
        TextView info3;
        TextView info4;
        TextView info5;
        public ViewHolder(View itemView){
            super(itemView);
            info = (TextView) itemView.findViewById(R.id.route);
            info2 = (TextView) itemView.findViewById(R.id.DifficultyRtng);
            info3 = (TextView) itemView.findViewById(R.id.ActivityTyp);
            info4 = (TextView) itemView.findViewById(R.id.Dist);
            info5 = (TextView) itemView.findViewById(R.id.Tim);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            Intent intent = new Intent(v.getContext(), pop.class);
            intent.putExtra("text", info.getText());
            intent.putExtra("text1", info2.getText());
            intent.putExtra("text2", info3.getText());
            intent.putExtra("text3", info4.getText());
            intent.putExtra("text4", info5.getText());
            v.getContext().startActivity(intent);
        }

    }

}
