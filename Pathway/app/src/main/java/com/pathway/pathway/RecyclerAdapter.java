package com.pathway.pathway;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

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
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView info;
        public ViewHolder(View itemView){
            super(itemView);
            info = (TextView) itemView.findViewById(R.id.route);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            Intent intent = new Intent(v.getContext(), pop.class);
            intent.putExtra("text", info.getText());
            v.getContext().startActivity(intent);
        }

    }

}
