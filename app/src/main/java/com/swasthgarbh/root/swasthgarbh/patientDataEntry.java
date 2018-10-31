package com.swasthgarbh.root.swasthgarbh;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.swasthgarbh.root.swasthgarbh.ApplicationController;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class patientDataEntry extends AppCompatActivity {

    TextView sysData, dysData, bodyWeight, extraComments, heartRate, bleedingVag, urineAlb;
    CheckBox headache, abdPain, visProb, decFea, swell, hyperT;
    SessionManager session;
    Button sendData, add_pic;
    ImageView ivImage;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String ImgBytes;
    ImageView loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_data_entry);
        getSupportActionBar().setTitle("Add data");
        sendData = (Button) findViewById(R.id.sendData);
        session = new SessionManager(this);

        session = new SessionManager(this);
        sysData = (TextView) findViewById(R.id.sysData);
        dysData = (TextView) findViewById(R.id.dysData);
        heartRate = (TextView) findViewById(R.id.heartRate);
        headache = (CheckBox) findViewById(R.id.headache);
        abdPain = (CheckBox) findViewById(R.id.abdPain);
        visProb = (CheckBox) findViewById(R.id.visProb);
        decFea = (CheckBox) findViewById(R.id.decFea);
        swell = (CheckBox) findViewById(R.id.swell);
        hyperT = (CheckBox) findViewById(R.id.hyperT);
        bodyWeight = (TextView) findViewById(R.id.bodyWeight);
        extraComments = (TextView) findViewById(R.id.extraComments);
        urineAlb = (TextView)findViewById(R.id.urineAlbumin);
        bleedingVag = (TextView)findViewById(R.id.bleedingVag);

        add_pic = (Button) findViewById(R.id.add_pic);
        add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ApplicationController.get_base_url() + "api/data";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
//                                Log.d("DATA", response.toString());
                                Intent i = new Intent(patientDataEntry.this, patient_registration.class);
                                startActivity(i);
                                finish();
//                                Log.d("DATA", response.toString());
//
                                //                            edit.commit();
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "Error Message: " + error.getMessage());
                    }
                }) {

                    @Override
                    public byte[] getBody() {
                        JSONObject params = new JSONObject();
                        try {
                            params.put("systolic", Integer.parseInt(sysData.getText().toString()));
                            params.put("diastolic", Integer.parseInt(dysData.getText().toString()));
                            params.put("urine_albumin", Float.parseFloat(urineAlb.getText().toString()));
                            params.put("bleeding_per_vaginum", Float.parseFloat(bleedingVag.getText().toString()));
                            params.put("headache", headache.isChecked());
                            params.put("abdominal_pain", abdPain.isChecked());
                            params.put("visual_problems", visProb.isChecked());
                            params.put("decreased_fetal_movements", decFea.isChecked());
                            params.put("swelling_in_hands_or_face", swell.isChecked());
                            params.put("hyper_tension", hyperT.isChecked());
                            params.put("weight", Integer.parseInt(bodyWeight.getText().toString()));
                            params.put("extra_comments", extraComments.getText());
                            params.put("patient", session.getUserDetails().get("id"));
                            params.put("heart_rate", Integer.parseInt(heartRate.getText().toString()));
                            Log.i("Boddddyyyyy", "getBody: " + params.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return params.toString().getBytes();
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", "Token " + session.getUserDetails().get("Token"));
                        Log.d("TAG", "Token " + session.getUserDetails().get("Token"));
                        return params;
                    }
                };
                ApplicationController.getInstance().addToRequestQueue(jsonObjReq);

                // Add Image
                if(ImgBytes.length() == 0){
                    return;
                }

                String url2 = ApplicationController.get_base_url() + "/api/image";
                JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(Request.Method.POST,
                        url2, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("TAG", response.toString());
                                Toast.makeText(patientDataEntry.this, "Image Sent!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(patientDataEntry.this, patient_registration.class);
                                startActivity(i);
                                finish();
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "Error Message: " + error.getMessage());
                    }
                }) {

                    @Override
                    public byte[] getBody() {
                        JSONObject params = new JSONObject();

                        try {
                            HashMap<String, String> user = session.getUserDetails();
                            params.put("byte", ImgBytes);
                            params.put("extra_comments_image",extraComments.getText());
                            params.put("patient", Integer.parseInt(user.get("id")));
//                            String d = "" + date.getText();
//                            String t = "" + time.getText();
//                            params.put("time_stamp", d + " " + t);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return params.toString().getBytes();

                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", "Token " + session.getUserDetails().get("Token"));
                        Log.d("TAG", "Token " + session.getUserDetails().get("Token"));
                        return params;
                    }
                };
                ApplicationController.getInstance().addToRequestQueue(jsonObjReq2);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] ba  = bytes.toByteArray();
        ImgBytes = Base64.encodeToString(ba, 0);
        ivImage.setImageBitmap(thumbnail);
    }

    private void onSelectFromGalleryResult(Intent data) {
        ivImage = (ImageView) findViewById(R.id.ivImage);
        Bitmap bm=null;

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte[] ba  = bytes.toByteArray();
        ImgBytes = Base64.encodeToString(ba, 0);
        Log.e("TAG", ImgBytes);
        ivImage.setImageBitmap(bm);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(patientDataEntry.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(patientDataEntry.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
}