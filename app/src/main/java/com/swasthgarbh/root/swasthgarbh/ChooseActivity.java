package com.swasthgarbh.root.swasthgarbh;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {

    Button doctor, patient, sign_in, login, send_otp;
    Dialog sign_in_dialog;
    EditText username, password;
    SessionManager session;
    TextView disclaimer,ancAssist;
    ProgressBar signInPB;
    TextView forgot_pass, gen_otp;
    LinearLayout otp_form, gen_otp_form;
    EditText gen_otp_mobile, new_pass, editTextotp;
    int otp_id;
    Dialog otp_dialog, dis_dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        doctor = (Button) findViewById(R.id.doctor_registration);
        patient = (Button) findViewById(R.id.patient_registration);
        sign_in = (Button) findViewById(R.id.signin_modal);
        disclaimer = (TextView) findViewById(R.id.privacyPolicy);
        ancAssist = (TextView) findViewById(R.id.offlineAnc);

        forgot_pass = (TextView) findViewById(R.id.forgot_pass);
        doctor.setOnClickListener(this);
        patient.setOnClickListener(this);
        sign_in.setOnClickListener(this);
        disclaimer.setOnClickListener(this);
        ancAssist.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.doctor_registration) {
            Intent i = new Intent(this, DoctorSignup.class);
            startActivity(i);
        } else if (view.getId() == R.id.patient_registration) {
            Intent i = new Intent(this, PatientSignup.class);
            startActivity(i);
        } else if (view.getId() == R.id.signin_modal) {
            callLoginDialog();
        } else if (view.getId() == R.id.privacyPolicy) {
            Intent i = new Intent(this, Disclaimer.class);
            startActivity(i);
        } else if (view.getId() == R.id.offlineAnc) {
            Intent i = new Intent(this, ANC_Assist.class);
            startActivity(i);
        }
    }

    private void generateOTP(Dialog otp_dialog) {
        gen_otp_mobile = otp_dialog.findViewById(R.id.mobile_num);

        String str_username = "" + gen_otp_mobile.getText();

        if (str_username.length() == 0) {
            Toast.makeText(ChooseActivity.this, "Enter your mobile number", Toast.LENGTH_LONG).show();
            return;
        }

        String url = ApplicationController.get_base_url() + "/api/gen_otp";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            otp_id = response.getInt("otp_id");

                            Log.d("TAG", response.get("message").toString());
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
            public byte[] getBody() {
                JSONObject params = new JSONObject();
                try {
                    String str_mobile = "" + gen_otp_mobile.getText();
                    params.put("user", str_mobile);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params.toString().getBytes();

            }
        };
        ApplicationController.getInstance().addToRequestQueue(jsonObjReq);

    }

    private void callOTPDialog() {

        otp_dialog = new Dialog(this);
        otp_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otp_dialog.setContentView(R.layout.activity_otp);
        otp_dialog.setCancelable(true);


        otp_form = otp_dialog.findViewById(R.id.otp_form);
        gen_otp_form = otp_dialog.findViewById(R.id.gen_otp_form);

        gen_otp = otp_dialog.findViewById(R.id.gen_otp);
        send_otp = otp_dialog.findViewById(R.id.send_otp);

        otp_dialog.show();
        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(otp_dialog.getWindow().getAttributes());

        // Set the alert dialog window width and height
        // Set alert dialog width equal to screen width 90%
        // int dialogWindowWidth = (int) (displayWidth * 0.9f);
        // Set alert dialog height equal to screen height 90%
        // int dialogWindowHeight = (int) (displayHeight * 0.9f);

        // Set alert dialog width equal to screen width 80%
        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        // Set alert dialog height equal to screen height 70%
        int dialogWindowHeight = (int) (displayHeight * 0.45f);
        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        // Apply the newly created layout parameters to the alert dialog window
        otp_dialog.getWindow().setAttributes(layoutParams);

        gen_otp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.gen_otp) {
                    generateOTP(otp_dialog);
                    otp_form.setVisibility(View.VISIBLE);
                    gen_otp_form.setVisibility(View.GONE);
                }
            }
        });


        send_otp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.send_otp) {
                    sendOTP(otp_dialog);

                }
            }
        });

    }

    private void sendOTP(Dialog otp_dialog) {
        editTextotp = otp_dialog.findViewById(R.id.editTextotp);
        new_pass = otp_dialog.findViewById(R.id.new_pass);
        String str_otp = "" + editTextotp.getText();
        final String str_new_pass = "" + new_pass.getText();

        if (str_otp.length() == 0) {
            Toast.makeText(ChooseActivity.this, "Enter OTP", Toast.LENGTH_LONG).show();
            return;
        }

        if (str_new_pass.length() == 0) {
            Toast.makeText(ChooseActivity.this, "Enter new password", Toast.LENGTH_LONG).show();
            return;
        }

        String url = ApplicationController.get_base_url() + "/api/verify_otp";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());

                        try {
                            session = new SessionManager(ChooseActivity.this);
                            session.createLoginSession(
                                    "" + response.get("Token"),
                                    response.getInt("U_ID"),
                                    "" + response.get("Type"),
                                    response.getInt("ID")
                            );
                            Intent i = new Intent(ChooseActivity.this, ControllerActivity.class);
                            startActivity(i);
                            finish();
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
            public byte[] getBody() {
                JSONObject params = new JSONObject();
                try {
                    String str_otp = "" + editTextotp.getText();
                    params.put("otp", str_otp);
                    params.put("otp_id", otp_id);
                    params.put("new_pass", str_new_pass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params.toString().getBytes();

            }
        };
        ApplicationController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void callLoginDialog() {

        sign_in_dialog = new Dialog(this);
        sign_in_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sign_in_dialog.setContentView(R.layout.activity_signin_modal);
        sign_in_dialog.setCancelable(true);
        login = sign_in_dialog.findViewById(R.id.login);

        username = sign_in_dialog.findViewById(R.id.username);
        password = sign_in_dialog.findViewById(R.id.password);
        forgot_pass = (TextView) sign_in_dialog.findViewById(R.id.forgot_pass);
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callOTPDialog();
            }
        });
        sign_in_dialog.show();
        signInPB = (ProgressBar) sign_in_dialog.findViewById(R.id.signInPB);
        signInPB.setVisibility(View.GONE);
        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(sign_in_dialog.getWindow().getAttributes());

        // Set the alert dialog window width and height
        // Set alert dialog width equal to screen width 90%
        // int dialogWindowWidth = (int) (displayWidth * 0.9f);
        // Set alert dialog height equal to screen height 90%
        // int dialogWindowHeight = (int) (displayHeight * 0.9f);

        // Set alert dialog width equal to screen width 80%
        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        // Set alert dialog height equal to screen height 70%
        int dialogWindowHeight = (int) (displayHeight * 0.45f);
        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        // Apply the newly created layout parameters to the alert dialog window
        sign_in_dialog.getWindow().setAttributes(layoutParams);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signInPB.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);
                if (v.getId() == R.id.login) {
                    String str_username = "" + username.getText();
                    String str_password = "" + password.getText();

                    if (str_username.length() == 0) {
                        Toast.makeText(ChooseActivity.this, "enter your mobile number", Toast.LENGTH_LONG).show();
                        signInPB.setVisibility(View.GONE);
                        login.setVisibility(View.VISIBLE);
                        return;
                    }

                    if (str_password.length() == 0) {
                        Toast.makeText(ChooseActivity.this, "enter your password", Toast.LENGTH_LONG).show();
                        signInPB.setVisibility(View.GONE);
                        login.setVisibility(View.VISIBLE);
                        return;
                    }
                    String url = ApplicationController.get_base_url() + "/api/login";
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            url, null,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("TAG", response.toString());

                                    try {
                                        session = new SessionManager(ChooseActivity.this);
                                        session.createLoginSession(
                                                "" + response.get("Token"),
                                                response.getInt("U_ID"),
                                                "" + response.get("Type"),
                                                response.getInt("ID")
                                        );
                                        Intent i = new Intent(ChooseActivity.this, ControllerActivity.class);
                                        startActivity(i);
                                        finish();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("TAG", "Error Message: " + error.getMessage());
                            signInPB.setVisibility(View.GONE);
                            login.setVisibility(View.VISIBLE);
                        }
                    }) {

                        @Override
                        public byte[] getBody() {
                            JSONObject params = new JSONObject();
                            try {
                                String str_mobile = "" + username.getText();
                                String str_password = "" + password.getText();
                                params.put("user", str_mobile);
                                params.put("password", str_password);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return params.toString().getBytes();

                        }
                    };
                    ApplicationController.getInstance().addToRequestQueue(jsonObjReq);
                }

            }
        });
    }
}
