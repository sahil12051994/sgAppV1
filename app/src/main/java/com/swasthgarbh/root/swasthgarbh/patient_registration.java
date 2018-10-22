package com.swasthgarbh.root.swasthgarbh;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
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
import java.util.List;
import java.util.Map;
import java.util.Random;

public class patient_registration extends AppCompatActivity {

    private Button whoGuideLines, logOutButton, done, name_of_medicine, extra_comments, doc_number, doc_name;
    static SessionManager session;
    ArrayList<patient_data_listview_class> patientRowData = new ArrayList<patient_data_listview_class>();
    ListView patient_list_view;
    patientDataAdapter adapter;
    private int doctorId;
    TextView doctorName, patientName, pregStartDate, doctorMobile, whoFollowing;
    ImageView callDoctor;

    Dialog choose_doc;
    int doc_id;
    ImageView ivImage;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String ImgBytes;
    Uri ImageUri;
    ImageView loader;

    private void logout(Context _c) {
        session = new SessionManager(_c);
        session.logoutUser();
        Intent i = new Intent(patient_registration.this, ControllerActivity.class);
        startActivity(i);
    }

    private String get_time_period(String time) {
        if (Integer.parseInt(time.split(":")[0]) > 12) {
            return "P.M";
        } else {
            return "A.M";
        }
    }

    private String get_time_min(String time) {
        return "" + time.split(":")[1];
    }

    private String get_time_hour(String time) {
        Log.d("TAG", time.toString());
        if (Integer.parseInt(time.split(":")[0]) > 12) {
            int hr_int = Integer.parseInt(time.split(":")[0]) - 12;
            return "" + hr_int;
        } else {
            return "" + time.split(":")[0];
        }
    }

    private String get_date_year(String date) {
        return date.split("-")[0];
    }

    private String get_date_month(String date) {
        String[] months = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int mon_int = Integer.parseInt(date.split("-")[1]);
        return months[mon_int];
    }

    private String get_date_date(String date) {
        return date.split("-")[2].split("T")[0];
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.patient_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        if (item.getItemId() == R.id.action_logout){
            logout(this);
            return true;
        }
        else if (item.getItemId() == R.id.action_change_doctor){
//            change_doctor();
        }
        else if (item.getItemId() == R.id.action_notification){
            i = new Intent(this, PatientNotifications.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


//    private void change_doctor() {
//        choose_doc = new Dialog(this);
//        choose_doc.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        choose_doc.setContentView(R.layout.choose_doc_dialog);
//        choose_doc.setCancelable(true);
//        done = choose_doc.findViewById(R.id.login);
//
//        doc_number = choose_doc.findViewById(R.id.editText8);
//        doc_number.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (editable.toString().length() == 10) {
//                    String url = ApplicationController.get_base_url() + "api/doctor?mobile=" + editable.toString();
//                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                            url, null,
//                            new Response.Listener<JSONObject>() {
//
//                                @Override
//                                public void onResponse(JSONObject response) {
//                                    Log.d("TAG", response.toString());
//
//                                    try {
//                                        doc_name.setText(response.get("name") + "");
//                                        doc_id = (int) response.get("pk");
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                        doc_name.setText("");
//                                        Toast.makeText(patient_registration.this, "No doctor with this mobile number is registered", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                }
//                            }, new Response.ErrorListener() {
//
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.d("TAG", "Error Message: " + error.getMessage());
//                        }
//                    }) {
//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Map<String, String> params = new HashMap<String, String>();
//                            params.put("Authorization", "Token " + session.getUserDetails().get("Token"));
//                            Log.d("TAG", "Token " + session.getUserDetails().get("Token"));
////                params.put("Authorization", "Token f00a64734d608991730ccba944776c316c38c544");
//                            return params;
//                        }
//
//                    };
//                    ApplicationController.getInstance().addToRequestQueue(jsonObjReq);
//                }
//            }
//        });
//        doc_name = choose_doc.findViewById(R.id.editText9);
//        choose_doc.show();
//
//        done.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (v.getId() == R.id.login) {
//
//
//                    String url = ApplicationController.get_base_url() + "api/patient/" + session.getUserDetails().get("id");
//                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
//                            url, null,
//                            new Response.Listener<JSONObject>() {
//
//                                @Override
//                                public void onResponse(JSONObject response) {
//                                    Log.d("TAG", response.toString());
//                                    choose_doc.dismiss();
//                                    Toast.makeText(patient_registration.this, "Doctor changed", Toast.LENGTH_LONG).show();
//
//                                }
//                            }, new Response.ErrorListener() {
//
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.d("TAG", "Error Message: " + error.getMessage());
//                        }
//                    }) {
//
//                        @Override
//                        public byte[] getBody() {
//                            JSONObject params = new JSONObject();
//                            try {
//                                params.put("d_id", doc_id);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                            return params.toString().getBytes();
//
//                        }
//
//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Map<String, String> params = new HashMap<String, String>();
//                            params.put("Authorization", "Token " + session.getUserDetails().get("Token"));
//                            Log.d("TAG", "Token " + session.getUserDetails().get("Token"));
////                params.put("Authorization", "Token f00a64734d608991730ccba944776c316c38c544");
//                            return params;
//                        }
//                    };
//                    ApplicationController.getInstance().addToRequestQueue(jsonObjReq);
//                }
//
//            }
//        });
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);

        doctorName = (TextView) findViewById(R.id.doctorName);
        doctorMobile = (TextView) findViewById(R.id.doctorMobile);
        patientName = (TextView) findViewById(R.id.patientName);
        pregStartDate = (TextView) findViewById(R.id.pregStartDate);
        whoFollowing = (TextView) findViewById(R.id.whoFollowing);
        callDoctor = (ImageView)  findViewById(R.id.callDoctor);

//        Button logOutButton = (Button) findViewById(R.id.analyseResult);
//        logOutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                logout(patient_registration.this);
//            }
//        });

        Button whoGuidelines = (Button) findViewById(R.id.who_button);
        whoGuidelines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patient_registration.this, WHOGuidelines.class);
                startActivity(intent);
            }
        });

        Button addData = (Button) findViewById(R.id.addData);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patient_registration.this, patientDataEntry.class);
                startActivity(intent);
            }
        });

        Button medicineList = (Button) findViewById(R.id.medicineList);
        medicineList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patient_registration.this, MedicineReminder.class);
                intent.putExtra("EXTRA_PATIENT_ID", session.getUserDetails().get("id"));
                startActivity(intent);
            }
        });

        callDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no= doctorMobile.getText().toString().replaceAll("-", "");
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+phone_no));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        });

        getSupportActionBar().setTitle("Patient");

        session = new SessionManager(this);

        getPatientData();
    }

    /*
    * to fill the doctor details
    * API for doctor details
    * */
    public void getDoctorData(){
        String url = ApplicationController.get_base_url() + "api/doctor/" + doctorId;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("apihit", response.toString());
                        try {
                            doctorName.setText(response.getString("name"));
                            doctorMobile.setText(String.valueOf(response.getInt("mobile")));
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

    /*
     * to fill the patient details
     * API for doctor details
     * */
    public void getPatientData(){
        String url = ApplicationController.get_base_url() + "api/patient/" + session.getUserDetails().get("id");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("apihit", response.toString());
                        try {
                            doctorId = response.getInt("doctor");
                            patientName.setText(response.getString("name"));
                            JSONArray patientBpData = response.getJSONArray("data");

                            /******************************
                             * To set the charts
                             */
                            LineChart chart = (LineChart) findViewById(R.id.chart);

                            ArrayList<Entry> yValues = new ArrayList<Entry>();
                            ArrayList<Entry> y2Values = new ArrayList<Entry>();

                            for (int i = 0; i < patientBpData.length(); i++) {
                                JSONObject po = (JSONObject) patientBpData.get(i);
                                patient_data_listview_class pr = new patient_data_listview_class(po.getString("time_stamp"),po.getInt("systolic"), po.getInt("diastolic"), po.getDouble("urine_albumin"), po.getInt("weight"), po.getDouble("bleeding_per_vaginum"));
                                patientRowData.add(pr);
                                Log.i("Data in array", "" + String.valueOf(patientBpData.get(i)));

                                yValues.add(new Entry(i, po.getInt("systolic")));
                                y2Values.add(new Entry(i, po.getInt("diastolic")));
                            }

                            chart.setDragEnabled(true);
                            chart.setScaleEnabled(true);

                            LineDataSet set1 = new LineDataSet(yValues, "Systole");
                            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                            LineDataSet set2 = new LineDataSet(y2Values, "Diastole");
                            set2.setAxisDependency(YAxis.AxisDependency.LEFT);

                            set1.setFillAlpha(110);
                            set1.setLineWidth(3f);
                            set2.setLineWidth(2f);
                            set2.setColor(Color.RED);


                            YAxis leftAxis = chart.getAxisLeft();
                            LimitLine ll = new LimitLine(140f, "Critical");
                            ll.setLineColor(Color.rgb(102, 255, 255));
                            ll.setLineWidth(1f);
                            ll.setTextColor(Color.BLACK);
                            ll.setTextSize(12f);
                            ll.enableDashedLine(4, 2, 0);
                            leftAxis.addLimitLine(ll);

                            LimitLine l2 = new LimitLine(90f, "Critical");
                            l2.setLineColor(Color.RED);
                            l2.setLineWidth(1f);
                            l2.setTextColor(Color.BLACK);
                            l2.setTextSize(12f);
                            l2.enableDashedLine(4, 2, 0);
                            leftAxis.addLimitLine(l2);

                            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                            set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                            dataSets.add(set1);
                            dataSets.add(set2);

                            LineData data = new LineData(dataSets);
                            chart.setData(data);
                            chart.invalidate();
                            chart.animateXY(3000, 3000);
                            /*
                             * To set the charts
                             ******************************/

                            patientDataAdapter itemsAdapter = new patientDataAdapter(patient_registration.this, patientRowData);
                            ListView listView = (ListView) findViewById(R.id.patient_data_list_view);
                            listView.setAdapter(itemsAdapter);
                            getDoctorData();
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
