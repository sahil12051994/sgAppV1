package com.swasthgarbh.root.swasthgarbh;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class patientDataAdapter extends ArrayAdapter<patient_data_listview_class> {

    static SessionManager session;

    public patientDataAdapter(Activity context, ArrayList<patient_data_listview_class> patientData) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, patientData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        patient_data_listview_class current_patient_data = getItem(position);
        session = new SessionManager(getContext());
        final HashMap<String, String> user = session.getUserDetails();
        View listItemView = convertView;
        if(listItemView == null){
            if ("patient".equals(user.get("type"))) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.patient_data_listview_layout, parent, false);

                ImageView statusImage = (ImageView)listItemView.findViewById(R.id.status);
                statusImage.setImageResource(current_patient_data.getStatusId());
            } else if ("doctor".equals(user.get("type"))) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.patient_detail_bp_list_in_doctor_screen, parent, false);

                LinearLayout otherDetailsPatient = (LinearLayout) listItemView.findViewById(R.id.otherDetailsPatient);
                //all getting removed on scroll
                if (!current_patient_data.getAbdominal_pain()) {
                    TextView abdominalPain = (TextView) listItemView.findViewById(R.id.abdominalPain);
                    otherDetailsPatient.removeView(abdominalPain);
                }
                if (!current_patient_data.getHeadache()) {
                    TextView headache = (TextView) listItemView.findViewById(R.id.headache);
                    otherDetailsPatient.removeView(headache);
                }
                if (!current_patient_data.getSwelling_in_hands_or_face()) {
                    TextView swelling = (TextView) listItemView.findViewById(R.id.swelling);
                    otherDetailsPatient.removeView(swelling);
                }
                if (!current_patient_data.getDecreased_fetal_movements()) {
                    TextView decreasedMove = (TextView) listItemView.findViewById(R.id.decreasedMove);
                    otherDetailsPatient.removeView(decreasedMove);
                }
                if (!current_patient_data.getVisual_problems()) {
                    TextView visualProb = (TextView) listItemView.findViewById(R.id.visualProb);
                    otherDetailsPatient.removeView(visualProb);
                }
            }
        }

        TextView bpSysTextView = (TextView)listItemView.findViewById(R.id.sysValue);
        bpSysTextView.setText("Systole :\t" + Integer.toString(current_patient_data.bpSysValue()));

        TextView bpDysTextView = (TextView)listItemView.findViewById(R.id.dysValue);
        bpDysTextView.setText("Diastole :\t" + Integer.toString(current_patient_data.bpDysValue()));

        TextView heartRateTextView = (TextView)listItemView.findViewById(R.id.weightVal);
        heartRateTextView.setText("Weight :\t" + Integer.toString(current_patient_data.weightValue()));

        TextView urineAlbumin = (TextView)listItemView.findViewById(R.id.urineAlValue);
        urineAlbumin.setText("Urine Albumin :\t" + Double.toString(current_patient_data.urineAlValue()));

        TextView bleedingVag = (TextView)listItemView.findViewById(R.id.bleedingVag);
        bleedingVag.setText("Bleeding/vaginum :\t" + Double.toString(current_patient_data.bleedingValue()));

        TextView datePatientRow = (TextView)listItemView.findViewById(R.id.dateRow);
        datePatientRow.setText(current_patient_data.dateVal() + "th");

        TextView monthPatientRow = (TextView)listItemView.findViewById(R.id.monthRow);
        monthPatientRow.setText(current_patient_data.monthVal() + "  " + current_patient_data.yearVal());

        TextView yearPatientRow = (TextView)listItemView.findViewById(R.id.timeRow);
        yearPatientRow.setText(current_patient_data.timeVal());

        return listItemView;
    }
}
