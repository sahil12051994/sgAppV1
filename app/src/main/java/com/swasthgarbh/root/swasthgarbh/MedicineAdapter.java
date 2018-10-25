package com.swasthgarbh.root.swasthgarbh;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;



public class MedicineAdapter extends ArrayAdapter<MedicineListClass> {

    public MedicineAdapter(Activity context, ArrayList<MedicineListClass> patientData) {
        super(context, 0, patientData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MedicineListClass current_medicine_data = getItem(position);

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.medicine_list_row, parent, false);
        }

        final TextView medName = (TextView)listItemView.findViewById(R.id.medName);
        medName.setText(current_medicine_data.getMedName());

        final TextView medFreq = (TextView)listItemView.findViewById(R.id.medFreq);
        medFreq.setText(current_medicine_data.getfreq());

        final TextView startDateMed = (TextView)listItemView.findViewById(R.id.startDateMed);
        startDateMed.setText(current_medicine_data.getStartDate());

        final TextView endDateMed = (TextView)listItemView.findViewById(R.id.endDateMed);
        endDateMed.setText(current_medicine_data.getEndDate());

        final TextView extraComments = (TextView)listItemView.findViewById(R.id.medicineDetail);
        extraComments.setText(current_medicine_data.getComments());

        ImageView reminder = (ImageView)listItemView.findViewById(R.id.reminderButton);
        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add calender events
            }
        });

        return listItemView;
    }
}
