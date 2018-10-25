package com.swasthgarbh.root.swasthgarbh;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MedicineReminder extends AppCompatActivity {

    ArrayList<MedicineListClass> medicineRowAdapter = new ArrayList<MedicineListClass>();
    ListView patient_list_view;
    patientDataAdapter adapter;
    static SessionManager session;
    Dialog add_medicine_dialog;
    Button addMedButton;
    EditText medName, medStart, medEnd, medComments;
    int clickedPatientId;
    RadioGroup radioGroupFReq;
    RadioButton radioGroupFReqDaily,radioGroupFReqWeekly,radioGroupFReqMonthly;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_reminder);

        session = new SessionManager(this);
        clickedPatientId= getIntent().getIntExtra("EXTRA_PATIENT_ID", 1);
        getPatientData(clickedPatientId);
        fab = findViewById(R.id.fab);

        final HashMap<String, String> user = session.getUserDetails();
        if ("doctor".equals(user.get("type"))){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddMedicineDialog();
                }
            });
        } else if ("patient".equals(user.get("type"))) {
            fab.hide();
        }
    }

    public void showAddMedicineDialog() {
        add_medicine_dialog = new Dialog(this);
        add_medicine_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        add_medicine_dialog.setContentView(R.layout.medicine_add_screen);
        add_medicine_dialog.setCancelable(true);
        add_medicine_dialog.show();

        addMedButton = add_medicine_dialog.findViewById(R.id.addMedButton);
        medName = add_medicine_dialog.findViewById(R.id.medName);
        medStart = add_medicine_dialog.findViewById(R.id.medStart);
        medEnd = add_medicine_dialog.findViewById(R.id.medEnd);
        medComments = add_medicine_dialog.findViewById(R.id.medComments);
        radioGroupFReq = add_medicine_dialog.findViewById(R.id.radioGroupFreq);

        radioGroupFReqDaily = add_medicine_dialog.findViewById(R.id.daily);
        radioGroupFReqWeekly = add_medicine_dialog.findViewById(R.id.weekly);
        radioGroupFReqMonthly = add_medicine_dialog.findViewById(R.id.monthly);

        addMedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = ApplicationController.get_base_url() + "swasthgarbh/patient/" + clickedPatientId + "/med";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Intent i = new Intent(MedicineReminder.this, MedicineReminder.class);
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
                            params.put("patient_id", clickedPatientId);
                            params.put("medicine_name", medName.getText());

                            String freq = "daily";
                            if(radioGroupFReqDaily.isChecked()){
                                freq = "daily";
                            } else if (radioGroupFReqMonthly.isChecked()){
                                freq = "monthly";
                            } else if(radioGroupFReqWeekly.isChecked()){
                                freq = "weekly";
                            }

                            params.put("medicine_freq", freq);
                            params.put("medicine_extra_comments", medComments.getText());
                            params.put("medicine_Image", "Image file for Medicine 4");
                            params.put("medicine_start", "2018-10-26T00:24:11.080431");
                            params.put("medicine_end", "2018-10-26T00:24:11.080431");

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


    public void getPatientData(int id){
        String url = ApplicationController.get_base_url() + "swasthgarbh/patient/" + id;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("apihit", response.toString());
                        try {
                            JSONArray medicineData = response.getJSONArray("medicines");

                            for (int i = 0; i < medicineData.length(); i++) {
                                JSONObject po = (JSONObject) medicineData.get(i);
                                MedicineListClass pr = new MedicineListClass(po.getString("medicine_name"),po.getString("medicine_start"), po.getString("medicine_end"),po.getString("medicine_freq"),po.getString("medicine_extra_comments"));
                                medicineRowAdapter.add(pr);
                                Log.i("Data in array", "" + String.valueOf(medicineData.get(i)));
                            }
                            MedicineAdapter itemsAdapter = new MedicineAdapter(MedicineReminder.this, medicineRowAdapter);
                            ListView listView = (ListView) findViewById(R.id.patient_medicine_list);
                            listView.setAdapter(itemsAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                            edit.commit();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "Error Message: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Token " + session.getUserDetails().get("Token"));
                return params;
            }
        };
        ApplicationController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
