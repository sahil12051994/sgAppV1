package com.swasthgarbh.root.swasthgarbh;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.util.Map;

public class DoctorScreenForParticularPatient extends AppCompatActivity implements View.OnClickListener{

    private Button whoGuideLines, logOutButton;
    static SessionManager session;
    ArrayList<patient_data_listview_class> patientRowData = new ArrayList<patient_data_listview_class>();
    ListView patient_list_view;
    patientDataAdapter adapter;
    private int doctorId;
    TextView doctorName, patientName, pregStartDate, patientMobile, whoFollowing, patientEmail;
    ImageView callDoctor;
    Button notify, send;
    Dialog notify_dialog;
    EditText message_box;
    static String p_id, to_fcm;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doctor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        if (item.getItemId() == R.id.action_logout){
            logout(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout(Context _c) {
        session = new SessionManager(_c);
        session.logoutUser();
        Intent i = new Intent(DoctorScreenForParticularPatient.this, ControllerActivity.class);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_screen_for_particular_patient);

        patientMobile = (TextView) findViewById(R.id.patientMobile);
        patientEmail = (TextView) findViewById(R.id.patientEmail);
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


        Button NotificationByDoctor = (Button) findViewById(R.id.NotificationByDoctor);
        NotificationByDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorScreenForParticularPatient.this, patientDataEntry.class);
                startActivity(intent);
            }
        });

        callDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no= patientMobile.getText().toString().replaceAll("-", "");
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+phone_no));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        });

        notify = (Button) findViewById(R.id.NotificationByDoctor);
        notify.setOnClickListener(this);

        getSupportActionBar().setTitle("Patient Details");

        session = new SessionManager(this);

        final int clickedPatientId= getIntent().getIntExtra("EXTRA_PATIENT_ID", 1);
        getPatientData(clickedPatientId);


        Button MedicineReminderDoctor = (Button) findViewById(R.id.MedicineReminderDoctor);
        MedicineReminderDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorScreenForParticularPatient.this, MedicineReminder.class);
                intent.putExtra("EXTRA_PATIENT_ID", clickedPatientId);
                startActivity(intent);
            }
        });
    }


    /*
     * to fill the patient details
     * API for doctor details
     * */
    public void getPatientData(int pId){

        String url = ApplicationController.get_base_url() + "api/patient/" + pId ;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("apihit", response.toString());
                        try {
                            doctorId = response.getInt("doctor");
                            patientName.setText(response.getString("name"));
                            patientEmail.setText(response.getString("email"));
                            patientMobile.setText(String.valueOf(response.getInt("mobile")));
                            whoFollowing.setText(response.getString("who_following"));
                            JSONArray patientBpData = response.getJSONArray("data");

                            /******************************
                             * To set the charts
                             */
                            LineChart chart = (LineChart) findViewById(R.id.chart);

                            ArrayList<Entry> yValues = new ArrayList<Entry>();
                            ArrayList<Entry> y2Values = new ArrayList<Entry>();
                            ArrayList<Entry> y3Values = new ArrayList<Entry>();

                            for (int i = 0; i < patientBpData.length(); i++) {
                                JSONObject po = (JSONObject) patientBpData.get(i);
                                patient_data_listview_class pr = new patient_data_listview_class(po.getString("time_stamp"),po.getInt("systolic"), po.getInt("diastolic"), po.getDouble("urine_albumin") ,po.getInt("weight"), po.getBoolean("headache"), po.getBoolean("abdominal_pain"), po.getBoolean("visual_problems"), po.getDouble("bleeding_per_vaginum") , po.getBoolean("decreased_fetal_movements"), po.getBoolean("swelling_in_hands_or_face"), po.getString("extra_comments"));
                                patientRowData.add(pr);
                                Log.i("Data in array", "" + String.valueOf(patientBpData.get(i)));

                                yValues.add(new Entry(i, po.getInt("systolic")));
                                y2Values.add(new Entry(i, po.getInt("diastolic")));
                                y3Values.add(new Entry(i, po.getInt("weight")));
                            }

                            chart.setDragEnabled(true);
                            chart.setScaleEnabled(true);

                            LineDataSet set1 = new LineDataSet(yValues, "Systole");
                            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                            LineDataSet set2 = new LineDataSet(y2Values, "Diastole");
                            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
                            LineDataSet set3 = new LineDataSet(y3Values, "weight");
                            set3.setAxisDependency(YAxis.AxisDependency.LEFT);

                            set1.setFillAlpha(110);
                            set1.setLineWidth(3f);
                            set2.setLineWidth(2f);
                            set1.setColor(Color.rgb(36, 113, 163));
                            set2.setColor(Color.RED);
                            set3.setColor(Color.rgb(40, 180, 99));


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

//                            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//                            set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//                            set3.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                            dataSets.add(set1);
                            dataSets.add(set2);
                            dataSets.add(set3);

                            LineData data = new LineData(dataSets);
                            chart.setData(data);
                            chart.invalidate();
                            chart.animateXY(3000, 3000);
                            /*
                             * To set the charts
                             ******************************/

                            patientDataAdapter itemsAdapter = new patientDataAdapter(DoctorScreenForParticularPatient.this, patientRowData);
                            ListView listView = (ListView) findViewById(R.id.patient_data_list_view);
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.NotificationByDoctor) {
            showNotifyDialog();
        }
    }

    private void showNotifyDialog() {
        notify_dialog = new Dialog(this);
        notify_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        notify_dialog.setContentView(R.layout.notification_send);

        message_box = notify_dialog.findViewById(R.id.msg_box);
        send = notify_dialog.findViewById(R.id.sendNotif);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(notify_dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        notify_dialog.getWindow().setAttributes(lp);


        notify_dialog.setCancelable(true);
        notify_dialog.show();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.sendNotif) {
                    if (message_box.getText().length() == 0) {
                        Toast.makeText(DoctorScreenForParticularPatient.this, "enter your message", Toast.LENGTH_LONG).show();
                        return;
                    }

                    String url = ApplicationController.get_base_url() + "dhadkan/api/notification";
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            url, null,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("TAG", response.toString());

//
                                    try {
                                        JSONArray patient_details = response.getJSONArray("data");
                                        Log.d("TAG", patient_details.toString());
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
                        public byte[] getBody() {
                            JSONObject params = new JSONObject();

                            try {
                                params.put("p_id", p_id);
                                params.put("message", "" + message_box.getText());
                                params.put("to", "" + to_fcm);
                                params.put("from", "" + "me");
                                notify_dialog.dismiss();
//                        params.put("date_of_birth", );

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return params.toString().getBytes();

                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Authorization", "Token " + session.getUserDetails().get("Token"));;
                            return params;
                        }
                    };
                    ApplicationController.getInstance().addToRequestQueue(jsonObjReq);

                }


            }
        });


    }

}
