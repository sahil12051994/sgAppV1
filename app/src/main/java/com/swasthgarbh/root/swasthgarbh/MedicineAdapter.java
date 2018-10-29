package com.swasthgarbh.root.swasthgarbh;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;



public class MedicineAdapter extends ArrayAdapter<MedicineListClass> {

    static SessionManager session;

    public MedicineAdapter(Activity context, ArrayList<MedicineListClass> patientData) {
        super(context, 0, patientData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final MedicineListClass current_medicine_data = getItem(position);

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.medicine_list_row, parent, false);
        }

        final TextView medName = (TextView)listItemView.findViewById(R.id.medName);
        medName.setText(current_medicine_data.getMedName());

        final TextView medFreq = (TextView)listItemView.findViewById(R.id.medFreq);
        medFreq.setText(current_medicine_data.getfreq());

        final TextView startDateMed = (TextView)listItemView.findViewById(R.id.startDateMed);
        startDateMed.setText(current_medicine_data.getStartDate_date() + "-" + current_medicine_data.getStartDate_month() + "-" + current_medicine_data.getStartDate_year());

        final TextView endDateMed = (TextView)listItemView.findViewById(R.id.endDateMed);
        endDateMed.setText(current_medicine_data.getEndDate_date() + "-" + current_medicine_data.getEndDate_month() + "-" + current_medicine_data.getEndDate_year());

        final TextView extraComments = (TextView)listItemView.findViewById(R.id.medicineDetail);
        extraComments.setText(current_medicine_data.getComments());

        ImageView reminder = (ImageView)listItemView.findViewById(R.id.reminderButton);

        session = new SessionManager(getContext());
        final HashMap<String, String> user = session.getUserDetails();
        Log.i("typeeeeee", "getView: " + user.get("type"));
        if ("patient".equals(user.get("type"))){
            reminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // add calender events
                    Intent calIntent = new Intent(Intent.ACTION_INSERT);
                    calIntent.setData(CalendarContract.Events.CONTENT_URI);
                    calIntent.setType("vnd.android.cursor.item/event");

                    String date = current_medicine_data.getStartDate();
                    int month = Integer.parseInt(date.split("-")[1]);

                    GregorianCalendar calDate = new GregorianCalendar(Integer.parseInt(current_medicine_data.getStartDate_year()), month, Integer.parseInt(current_medicine_data.getStartDate_year()));
                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                            calDate.getTimeInMillis());

                    date = current_medicine_data.getEndDate();
                    month = Integer.parseInt(date.split("-")[1]);
                    GregorianCalendar calEndDate = new GregorianCalendar(Integer.parseInt(current_medicine_data.getEndDate_date()), month, Integer.parseInt(current_medicine_data.getEndDate_year()));
                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                            calEndDate.getTimeInMillis());

                    calIntent.putExtra("allDay", false);
                    calIntent.putExtra("rrule", "FREQ=" + current_medicine_data.getfreq());

                    calIntent.putExtra("title", "Reminder for " + current_medicine_data.getMedName());
                    calIntent.putExtra("description", current_medicine_data.getComments());
                    getContext().startActivity(calIntent);
                }
            });
        } else if ("doctor".equals(user.get("type"))){
            reminder.setVisibility(View.INVISIBLE);
        }
        return listItemView;
    }
}
