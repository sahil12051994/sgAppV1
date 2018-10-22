package com.swasthgarbh.root.swasthgarbh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class patientDataEntry extends AppCompatActivity {

    TextView sysData, dysData, bodyWeight, extraComments, heartRate, bleedingVag, urineAlb;
    CheckBox headache, abdPain, visProb, decFea, swell, hyperT;
    SessionManager session;
    Button sendData;


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
            }
        });

    }
}