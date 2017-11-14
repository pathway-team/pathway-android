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
    ArrayList<String> arrayList;
    public RecyclerAdapter(ArrayList<String> arrayList){
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_child, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.info.setText(arrayList.get(position));
        holder.info2.setText(arrayList.get(position));
        holder.info3.setText(arrayList.get(position));
        holder.info4.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView info;
        TextView info2;
        TextView info3;
        TextView info4;
        public ViewHolder(View itemView){
            super(itemView);
            info = (TextView) itemView.findViewById(R.id.route);
            info2 = (TextView) itemView.findViewById(R.id.max_speed);
            info3 = (TextView) itemView.findViewById(R.id.avg_speed);
            info4 = (TextView) itemView.findViewById(R.id.totalTimeSec);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            Intent intent = new Intent(v.getContext(), pop.class);
            intent.putExtra("text", info.getText());
            intent.putExtra("text1", info2.getText());
            intent.putExtra("text2", info3.getText());
            intent.putExtra("text3", info4.getText());
            v.getContext().startActivity(intent);
        }

    }

}
