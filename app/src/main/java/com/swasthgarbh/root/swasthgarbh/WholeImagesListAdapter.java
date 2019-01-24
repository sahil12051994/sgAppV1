package com.swasthgarbh.root.swasthgarbh;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class WholeImagesListAdapter extends ArrayAdapter<WholeImagesListClass> {

    static SessionManager session;

    public WholeImagesListAdapter(Activity context, ArrayList<WholeImagesListClass> patientData) {
        super(context, 0, patientData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final WholeImagesListClass current_medicine_data = getItem(position);

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.patient_image_row, parent, false);
        }

        final TextView extraComments = (TextView)listItemView.findViewById(R.id.extraCommentsInImage);
        extraComments.setText(current_medicine_data.getExtraComments());

        final TextView dateText = (TextView)listItemView.findViewById(R.id.image_date);
        dateText.setText(current_medicine_data.getDateString());

//        final String imageString = current_medicine_data.getImgByte();

        final Integer imageId = current_medicine_data.getId();
        final LinearLayout clickDownloadImage = (LinearLayout)listItemView.findViewById(R.id.patientImage);
        clickDownloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(v.getContext(), image_view_modal.class);
                intent.putExtra("EXTRA_IMAGE_ID", imageId);
                v.getContext().startActivity(intent);
            }
        });
        return listItemView;
    }
}
