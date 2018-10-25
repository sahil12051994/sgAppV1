package com.swasthgarbh.root.swasthgarbh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter <ImageListAdapter.MyViewHolder>{

    List<PatientImageRow> data = Collections.emptyList();
    private LayoutInflater inflater;


    public ImageListAdapter(Context context, List<PatientImageRow> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ImageListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  inflater.inflate(R.layout.patient_image_row, parent, false);
        ImageListAdapter.MyViewHolder viewHolder = new ImageListAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageListAdapter.MyViewHolder holder, int position) {
        PatientImageRow current = data.get(position);

        holder.image_date.setText("Image - " + current.date_date + "th " + current.date_month + " " + current.date_year);
        holder.pk.setText(current.pk);
    }

    @Override
    public int getItemCount() {
        Log.d("TAG", data.size()+"");
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView image_date, pk;
        public MyViewHolder(View itemView) {
            super(itemView);
            image_date = itemView.findViewById(R.id.image_date);
            pk = itemView.findViewById(R.id.pk);
        }
    }
}
