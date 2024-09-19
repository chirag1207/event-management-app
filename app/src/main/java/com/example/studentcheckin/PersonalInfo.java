package com.example.studentcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PersonalInfo extends AppCompatActivity {

    private TextView textViewFullName;
    private TextView textViewEmail;
    private TextView textViewPhoneNumber;
    private TextView textViewDOB;
    private TextView textViewHeight;
    private TextView textViewWeight;
    private TextView textViewStreetName;
    private TextView textViewCity;
    private TextView textViewState;
    private TextView textViewPincode;
    private String userId;
    private Button buttonEdit;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        textViewFullName = findViewById(R.id.textViewFullName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPhoneNumber = findViewById(R.id.textViewPhoneNumber);
        textViewDOB = findViewById(R.id.textViewDOB);
        textViewHeight = findViewById(R.id.textViewHeight);
        textViewWeight = findViewById(R.id.textViewWeight);
        textViewStreetName = findViewById(R.id.textViewStreetName);
        textViewCity = findViewById(R.id.textViewCity);
        textViewState = findViewById(R.id.textViewState);
        textViewPincode = findViewById(R.id.textViewPincode);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonBack = findViewById(R.id.buttonBack);

        userId = getIntent().getStringExtra("USER_ID");

        fetchUserInfo();

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalInfo.this, EditPersonalInfo.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void fetchUserInfo() {
        String url = "http://192.168.1.175:8090/api/v1/user/" + userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Log the entire response for debugging
                        Log.d("PersonalInfo", "Response: " + response.toString());

                        String fullName = response.optString("fullName", "N/A");
                        String email = response.optString("email", "N/A");
                        String phoneNumber = response.optString("phone", "N/A");
                        String dob = response.optString("dob", "N/A");
                        String height = response.optString("height", "N/A");
                        String weight = response.optString("weight", "N/A");
                        String streetName = response.optString("streetName", "N/A");
                        String city = response.optString("city", "N/A");
                        String state = response.optString("state", "N/A");
                        String pincode = response.optString("pincode", "N/A");

                        textViewFullName.setText("Full Name: " + fullName);
                        textViewEmail.setText("Email: " + email);
                        textViewPhoneNumber.setText("Phone Number: " + phoneNumber);
                        textViewDOB.setText("Date of Birth: " + dob);
                        textViewHeight.setText("Height: " + height);
                        textViewWeight.setText("Weight: " + weight);
                        textViewStreetName.setText("Street Name: " + streetName);
                        textViewCity.setText("City: " + city);
                        textViewState.setText("State: " + state);
                        textViewPincode.setText("Pincode: " + pincode);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Network error";
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject jsonObject = new JSONObject(responseBody);
                        errorMessage = jsonObject.optString("message", errorMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(PersonalInfo.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(PersonalInfo.this);
        queue.add(jsonObjectRequest);
    }
}
