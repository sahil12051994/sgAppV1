package com.swasthgarbh.root.swasthgarbh;

import android.content.Context;
import android.graphics.Color;
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

public class PatientListRowInDoctorClass {

    private String patientName;
    private String patientLmp;
    private int patientId;
    private ArrayList<Entry> yValues = new ArrayList<Entry>();
    private ArrayList<Entry> y2Values = new ArrayList<Entry>();

    public PatientListRowInDoctorClass(int patientId, String name, String lmp){

        this.patientName = name;
        this.patientLmp = lmp.split("T")[0].split("-")[2] + "-" + lmp.split("T")[0].split("-")[1] + "-" + lmp.split("T")[0].split("-")[0];;
        this.patientId = patientId;
    }

    public String getName() {return patientName; }

    public String getLmp() {return patientLmp; }

    public int getPatientId() {return patientId; }

}
