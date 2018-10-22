package com.swasthgarbh.root.swasthgarbh;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_reminder);

        session = new SessionManager(this);
        int clickedPatientId= getIntent().getIntExtra("EXTRA_PATIENT_ID", 1);
        getPatientData(clickedPatientId);

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
                                MedicineListClass pr = new MedicineListClass(po.getString("medicine_name"),"3 Weeks", "Take this medicine with luke warm water.", "15th May");
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
