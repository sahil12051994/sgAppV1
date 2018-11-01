package com.swasthgarbh.root.swasthgarbh;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.swasthgarbh.root.swasthgarbh.patient_registration.session;

public class WHOGuidelines extends AppCompatActivity {

    CheckBox anc1_diabtese, anc1_anemia, anc1_hiv, anc1_ultrasound, anc1_tetnus, anc1_urine, anc2_diabtese, anc2_anemia, anc3_diabtese, anc3_anemia, anc3_urine, anc4_diabtese, anc5_diabtese, anc5_urine, anc6_diabtese, anc6_anemia, anc7_diabtese, anc8_diabtese;
    int clickedPatientId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whoguidelines);

        anc1_diabtese = (CheckBox) findViewById(R.id.anc1dia);
//        anc1_diabtese.setOnClickListener(this);
        anc1_anemia = (CheckBox) findViewById(R.id.anc1ana);
//        anc1_anemia.setOnClickListener(this);
        anc1_hiv = (CheckBox) findViewById(R.id.anc1hiv);
//        anc1_hiv.setOnClickListener(this);
        anc1_ultrasound = (CheckBox) findViewById(R.id.anc1ult);
//        anc1_ultrasound.setOnClickListener(this);
        anc1_tetnus = (CheckBox) findViewById(R.id.anc1tet);
//        anc1_tetnus.setOnClickListener(this);
        anc1_urine = (CheckBox) findViewById(R.id.anc1uri);
//        anc1_urine.setOnClickListener(this);

        anc2_diabtese = (CheckBox) findViewById(R.id.anc2dia);
//        anc2_diabtese.setOnClickListener(this);
        anc2_anemia = (CheckBox) findViewById(R.id.anc2ult);
//        anc2_anemia.setOnClickListener(this);

        anc3_diabtese = (CheckBox) findViewById(R.id.anc3dia);
//        anc3_diabtese.setOnClickListener(this);
        anc3_anemia = (CheckBox) findViewById(R.id.anc3ana);
//        anc3_anemia.setOnClickListener(this);
        anc3_urine = (CheckBox) findViewById(R.id.anc3uri);
//        anc3_urine.setOnClickListener(this);

        anc4_diabtese = (CheckBox) findViewById(R.id.anc4dia);
//        anc4_diabtese.setOnClickListener(this);

        anc5_diabtese = (CheckBox) findViewById(R.id.anc5dia);
//        anc5_diabtese.setOnClickListener(this);
        anc5_urine = (CheckBox) findViewById(R.id.anc5uri);
//        anc5_urine.setOnClickListener(this);

        anc6_diabtese = (CheckBox) findViewById(R.id.anc6dia);
//        anc6_diabtese.setOnClickListener(this);
        anc6_anemia = (CheckBox) findViewById(R.id.anc6ana);
//        anc6_anemia.setOnClickListener(this);

        anc7_diabtese = (CheckBox) findViewById(R.id.anc7dia);
//        anc7_diabtese.setOnClickListener(this);

        anc8_diabtese = (CheckBox) findViewById(R.id.anc8dia);
//        anc8_diabtese.setOnClickListener(this);

        getSupportActionBar().setTitle("WHO Guidlines");

        session = new SessionManager(this);
//      Getting the WHO Data
        clickedPatientId= getIntent().getIntExtra("EXTRA_PATIENT_ID", 0);
        String url = ApplicationController.get_base_url() + "swasthgarbh/patient/" + clickedPatientId;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("apihit", response.toString());
                        try {

                            TextView lmpDate = (TextView) findViewById(R.id.lmpDate);
                            TextView eddDate = (TextView) findViewById(R.id.eddDate);
                            TextView anc1Date = (TextView) findViewById(R.id.anc1Date);
                            TextView anc2Date = (TextView) findViewById(R.id.anc2Date);
                            TextView anc3Date = (TextView) findViewById(R.id.anc3Date);
                            TextView anc4Date = (TextView) findViewById(R.id.anc4Date);
                            TextView anc5Date = (TextView) findViewById(R.id.anc5Date);
                            TextView anc6Date = (TextView) findViewById(R.id.anc6Date);
                            TextView anc7Date = (TextView) findViewById(R.id.anc7Date);
                            TextView anc8Date = (TextView) findViewById(R.id.anc8Date);

                            String date_date = response.getString("startDate").split("T")[0].split("-")[2];
                            String date_month = response.getString("startDate").split("T")[0].split("-")[1];
                            String date_year = response.getString("startDate").split("T")[0].split("-")[0];

                            String lmpDateString = date_date + "/" + date_month + "/" + date_year;
                            String eddDateString = date_year + "/" + date_month + "/" + date_date;

                            lmpDate.setText(lmpDateString);

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.DATE, 280);
                            String output = sdf.format(c.getTime());
                            eddDate.setText(output);

                            Calendar anc1c = Calendar.getInstance();
                            anc1c.add(Calendar.DATE, 84);
                            anc1Date.setText("12 Weeks - " + sdf.format(anc1c.getTime()));
                            anc1_diabtese.setChecked(response.getBoolean("anc1_diabtese"));
                            anc1_anemia.setChecked(response.getBoolean("anc1_anemia"));
                            anc1_hiv.setChecked(response.getBoolean("anc1_hiv"));
                            anc1_ultrasound.setChecked(response.getBoolean("anc1_ultrasound"));
                            anc1_tetnus.setChecked(response.getBoolean("anc1_tetnus"));
                            anc1_urine.setChecked(response.getBoolean("anc1_urine"));


                            Calendar anc2c = Calendar.getInstance();
                            anc2c.add(Calendar.DATE, 140);
                            anc2Date.setText("20 Weeks - " + sdf.format(anc2c.getTime()));
                            anc2_diabtese.setChecked(response.getBoolean("anc2_diabtese"));
                            anc2_anemia.setChecked(response.getBoolean("anc2_anemia"));


                            Calendar anc3c = Calendar.getInstance();
                            anc3c.add(Calendar.DATE, 182);
                            anc3Date.setText("26 Weeks - " + sdf.format(anc3c.getTime()));
                            anc3_diabtese.setChecked(response.getBoolean("anc3_diabtese"));
                            anc3_anemia.setChecked(response.getBoolean("anc3_anemia"));
                            anc3_urine.setChecked(response.getBoolean("anc3_urine"));


                            Calendar anc4c = Calendar.getInstance();
                            anc4c.add(Calendar.DATE, 210);
                            anc4Date.setText("30 Weeks - " + sdf.format(anc4c.getTime()));
                            anc4_diabtese.setChecked(response.getBoolean("anc4_diabtese"));


                            Calendar anc5c = Calendar.getInstance();
                            anc5c.add(Calendar.DATE, 238);
                            anc5Date.setText("34 Weeks - " + sdf.format(anc5c.getTime()));
                            anc5_diabtese.setChecked(response.getBoolean("anc5_diabtese"));
                            anc5_urine.setChecked(response.getBoolean("anc5_urine"));

                            Calendar anc6c = Calendar.getInstance();
                            anc6c.add(Calendar.DATE, 266);
                            anc6Date.setText("36 Weeks - " + sdf.format(anc6c.getTime()));
                            anc6_diabtese.setChecked(response.getBoolean("anc6_diabtese"));
                            anc6_anemia.setChecked(response.getBoolean("anc6_anemia"));


                            Calendar anc7c = Calendar.getInstance();
                            anc7c.add(Calendar.DATE, 280);
                            anc7Date.setText("38 Weeks - " + sdf.format(anc7c.getTime()));
                            anc7_diabtese.setChecked(response.getBoolean("anc7_diabtese"));


                            Calendar anc8c = Calendar.getInstance();
                            anc8c.add(Calendar.DATE, 84);
                            anc8Date.setText("40 Weeks - " + sdf.format(anc8c.getTime()));
                            anc8_diabtese.setChecked(response.getBoolean("anc8_diabtese"));

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

        Button updateWhoData = (Button) findViewById(R.id.updateWhoData);

        final HashMap<String, String> user = session.getUserDetails();
        if ("doctor".equals(user.get("type"))){
            updateWhoData.setVisibility(View.GONE);
//            anc8_diabtese.setEnabled(false);
        }
        updateWhoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWhoData();
            }
        });
    }

    public void updateWhoData(){
        String url = ApplicationController.get_base_url() + "swasthgarbh/patient/" + session.getUserDetails().get("id");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PATCH,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Intent i = new Intent(WHOGuidelines.this, patient_registration.class);
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
                    params.put("anc1_diabtese", anc1_diabtese.isChecked());
                    params.put("anc1_anemia", anc1_anemia.isChecked());
                    params.put("anc1_hiv", anc1_hiv.isChecked());
                    params.put("anc1_ultrasound", anc1_ultrasound.isChecked());
                    params.put("anc1_tetnus", anc1_tetnus.isChecked());
                    params.put("anc1_urine", anc1_urine.isChecked());
                    params.put("anc2_diabtese", anc2_diabtese.isChecked());
                    params.put("anc2_anemia", anc2_anemia.isChecked());
                    params.put("anc3_diabtese", anc3_diabtese.isChecked());
                    params.put("anc3_anemia", anc3_anemia.isChecked());
                    params.put("anc3_urine", anc3_urine.isChecked());
                    params.put("anc4_diabtese", anc4_diabtese.isChecked());
                    params.put("anc5_diabtese", anc5_diabtese.isChecked());
                    params.put("anc5_urine", anc5_urine.isChecked());
                    params.put("anc6_diabtese", anc6_diabtese.isChecked());
                    params.put("anc6_anemia", anc6_anemia.isChecked());
                    params.put("anc7_diabtese", anc7_diabtese.isChecked());
                    params.put("anc8_diabtese", anc8_diabtese.isChecked());

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

}

