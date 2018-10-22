package com.swasthgarbh.root.swasthgarbh;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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

        TextView medName = (TextView)listItemView.findViewById(R.id.medName);
        medName.setText(current_medicine_data.getMedName());

        return listItemView;
    }
}
