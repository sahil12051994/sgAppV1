package com.swasthgarbh.root.swasthgarbh;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class patientDataAdapter extends ArrayAdapter<patient_data_listview_class> {

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

        View listItemView = convertView;
        if(listItemView == null){
            if(current_patient_data.getDocOrPat() == 1){
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.patient_detail_bp_list_in_doctor_screen, parent, false);
            } else {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.patient_data_listview_layout, parent, false);
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

        if(current_patient_data.getDocOrPat() == 0){
            ImageView statusImage = (ImageView)listItemView.findViewById(R.id.status);
            statusImage.setImageResource(current_patient_data.getStatusId());
        }


        TextView datePatientRow = (TextView)listItemView.findViewById(R.id.dateRow);
        datePatientRow.setText(current_patient_data.dateVal() + "th");

        TextView monthPatientRow = (TextView)listItemView.findViewById(R.id.monthRow);
        monthPatientRow.setText(current_patient_data.monthVal() + "  " + current_patient_data.yearVal());

        TextView yearPatientRow = (TextView)listItemView.findViewById(R.id.timeRow);
        yearPatientRow.setText(current_patient_data.timeVal());

        if(current_patient_data.getDocOrPat() == 1) {

//            if (current_patient_data.abdominal_pain) {
//                TextView abdominalPain = (TextView) listItemView.findViewById(R.id.abdominalPain);
//                abdominalPain.setTextColor(Color.GREEN);
//            }
//            if (current_patient_data.headache) {
//                TextView headache = (TextView) listItemView.findViewById(R.id.headache);
//                headache.setTextColor(Color.GREEN);
//            }
//            if (current_patient_data.swelling_in_hands_or_face) {
//                TextView swelling = (TextView) listItemView.findViewById(R.id.swelling);
//                swelling.setTextColor(Color.GREEN);
//            }
//            if (current_patient_data.decreased_fetal_movements) {
//                TextView decreasedMove = (TextView) listItemView.findViewById(R.id.decreasedMove);
//                decreasedMove.setTextColor(Color.GREEN);
//            }
//            if (current_patient_data.visual_problems) {
//                TextView visualProb = (TextView) listItemView.findViewById(R.id.visualProb);
//                visualProb.setTextColor(Color.GREEN);
//            }
//            if (current_patient_data.bleeding_per_vaginum > 0) {
//                TextView bleedingVag = (TextView) listItemView.findViewById(R.id.bleedingVag);
//                bleedingVag.setText("Bleeding/Vag : " + current_patient_data.bleeding_per_vaginum);
//            }
        }
        return listItemView;
    }
}
