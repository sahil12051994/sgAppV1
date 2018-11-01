package com.swasthgarbh.root.swasthgarbh;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorScreen extends AppCompatActivity {

    private Button whoGuideLines, logOutButton;
    static SessionManager session;
    ArrayList<patient_data_listview_class> patientRowData = new ArrayList<patient_data_listview_class>();
    ListView patient_list_view;
    patientDataAdapter adapter;
    private int doctorId;
    TextView doctorName, doctorEmail, doctorTotalPatients, doctorHospital, doctorSpeciality;
    ImageView callDoctor;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doctor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        if (item.getItemId() == R.id.action_logout) {
            logout(this);
            return true;
        } else if (item.getItemId() == R.id.action_notification){
            i = new Intent(this, PatientNotifications.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.action_refresh){
            getDoctorData();
            getDoctorAllPatientsData();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout(Context _c) {
        session = new SessionManager(_c);
        session.logoutUser();
        Intent i = new Intent(DoctorScreen.this, ControllerActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_screen);

        doctorName = (TextView) findViewById(R.id.registeredDoctorName);
        doctorHospital = (TextView) findViewById(R.id.registeredDoctorHospital);
        doctorEmail = (TextView) findViewById(R.id.registeredDoctorEmail);
        doctorTotalPatients = (TextView) findViewById(R.id.registeredDoctorTotalPatients);
        doctorSpeciality = (TextView) findViewById(R.id.registeredDoctorSpeciality);

        getSupportActionBar().setTitle("Doctor");

        session = new SessionManager(this);

        Button allPatientsDoctor = (Button) findViewById(R.id.allPatientsDoctor);
        allPatientsDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorScreen.this, AllPatientListActivity.class);
                startActivity(intent);
            }
        });

        Button allNotificationsDoctor = (Button) findViewById(R.id.allNotificationsDoctor);
        allNotificationsDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DoctorScreen.this, PatientNotifications.class);
                startActivity(i);
            }
        });
        getDoctorData();
        getDoctorAllPatientsData();
    }


    public void getDoctorAllPatientsData() {
        String url = ApplicationController.get_base_url() + "swasthgarbh/patient";

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        int anc1Count, anc2Count, anc3Count, anc4Count, anc5Count, anc6Count, anc7Count, anc8Count;
                        anc1Count = anc2Count = anc3Count = anc4Count = anc5Count = anc6Count = anc7Count = anc8Count = 0;
                        Log.d("apihit", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject po = response.getJSONObject(i);
                                if (po.getBoolean("anc1_diabtese") || po.getBoolean("anc1_anemia") || po.getBoolean("anc1_hiv") || po.getBoolean("anc1_ultrasound") || po.getBoolean("anc1_anemia") || po.getBoolean("anc1_tetnus") || po.getBoolean("anc1_urine")) {
                                    anc1Count++;
                                }
                                if (po.getBoolean("anc2_diabtese") || po.getBoolean("anc2_anemia")) {
                                    anc2Count++;
                                }
                                if (po.getBoolean("anc3_urine") || po.getBoolean("anc3_anemia") || po.getBoolean("anc3_diabtese")) {
                                    anc3Count++;
                                }
                                if (po.getBoolean("anc4_diabtese")) {
                                    anc4Count++;
                                }
                                if (po.getBoolean("anc5_diabtese") || po.getBoolean("anc5_urine")) {
                                    anc5Count++;
                                }
                                if (po.getBoolean("anc6_diabtese") || po.getBoolean("anc6_anemia")) {
                                    anc6Count++;
                                }
                                if (po.getBoolean("anc7_diabtese")) {
                                    anc7Count++;
                                }
                                if (po.getBoolean("anc8_diabtese")) {
                                    anc8Count++;
                                }
                            }

                            int total_ladies = response.length();

                            BarChart barChart = (BarChart) findViewById(R.id.whoChart);

                            List<BarEntry> entriesGroup1 = new ArrayList<>();
                            List<BarEntry> entriesGroup2 = new ArrayList<>();

                            entriesGroup1.add(new BarEntry(1,anc1Count));
                            entriesGroup2.add(new BarEntry(1,total_ladies-anc1Count));
                            entriesGroup1.add(new BarEntry(2, anc2Count));
                            entriesGroup2.add(new BarEntry(2,total_ladies-anc2Count));
                            entriesGroup1.add(new BarEntry(3, anc3Count));
                            entriesGroup2.add(new BarEntry(3,total_ladies-anc3Count));
                            entriesGroup1.add(new BarEntry(4, anc4Count));
                            entriesGroup2.add(new BarEntry(4,total_ladies-anc4Count));
                            entriesGroup1.add(new BarEntry(5, anc5Count));
                            entriesGroup2.add(new BarEntry(5,total_ladies-anc5Count));
                            entriesGroup1.add(new BarEntry(6, anc6Count));
                            entriesGroup2.add(new BarEntry(6,total_ladies-anc6Count));
                            entriesGroup1.add(new BarEntry(7, anc7Count));
                            entriesGroup2.add(new BarEntry(7,total_ladies-anc7Count));
                            entriesGroup1.add(new BarEntry(8, anc8Count));
                            entriesGroup2.add(new BarEntry(8,total_ladies-anc8Count));

                            BarDataSet set1 = new BarDataSet(entriesGroup1, "Following WHO Guidelines");
                            BarDataSet set2 = new BarDataSet(entriesGroup2, "Not Following WHO Guidelines");
                            set2.setColor(Color.RED);

                            float groupSpace = 0.06f;
                            float barSpace = 0.02f; // x2 dataset
                            float barWidth = 0.45f; // x2 dataset
//                          (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"

                            BarData data = new BarData(set1, set2);
                            data.setBarWidth(barWidth); // set the width of each bar
                            barChart.setData(data);
                            barChart.groupBars(0.5f,groupSpace, barSpace); // perform the "explicit" grouping
                            barChart.setDragEnabled(true);
                            barChart.setScaleEnabled(true);
                            barChart.invalidate();
                            barChart.animateXY(3000, 3000);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    /*
     * to fill the doctor details
     * API for doctor details
     * */
    public void getDoctorData() {
        String url = ApplicationController.get_base_url() + "api/doctor/" + session.getUserDetails().get("id");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("apihit", response.toString());
                        try {
                            doctorName.setText(response.getString("name"));
                            doctorHospital.setText(response.getString("hospital"));
                            doctorEmail.setText(response.getString("email"));
                            doctorTotalPatients.setText(String.valueOf(response.getJSONArray("patients").length()));
                            doctorSpeciality.setText(response.getString("speciality"));

                            JSONObject analysis_obj = (JSONObject) response.getJSONObject("analysis_object");
                            PieChart pieChart = (PieChart) findViewById(R.id.doc2chart);
                            ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
                            yvalues.add(new PieEntry(analysis_obj.getInt("high_sys"), "High Systole"));
                            yvalues.add(new PieEntry(analysis_obj.getInt("high_dys"), "High Diastole"));
                            yvalues.add(new PieEntry(analysis_obj.getInt("high_weight"), "Over Weight"));
                            yvalues.add(new PieEntry(analysis_obj.getInt("hyper_tension"), "Hypertensed"));
                            yvalues.add(new PieEntry(analysis_obj.getInt("urine_albumin"), "High Urine Albumin"));
                            yvalues.add(new PieEntry(analysis_obj.getInt("who_following"), "Following WHO"));

                            PieDataSet dataSet = new PieDataSet(yvalues, getString(R.string.PieChartDesc));

                            PieData data = new PieData(dataSet);
                            pieChart.setDrawHoleEnabled(true);
                            data.setValueTextSize(13f);
                            data.setValueTextColor(Color.DKGRAY);
                            pieChart.setEntryLabelColor(Color.BLACK);
                            dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                            dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//                            data.setValueFormatter(new PercentFormatter());
                            pieChart.setData(data);
                            pieChart.invalidate();

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
