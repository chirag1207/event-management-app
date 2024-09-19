package com.example.studentcheckin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Registration extends Activity {

    private EditText eventNameEditText;
    private EditText eventDateEditText;
    private EditText eventTimeEditText;
    private EditText eventDetailsEditText;
    private EditText userIdEditText;
    private EditText userNameEditText;
    private EditText userDobEditText;
    private CheckBox termsCheckBox;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        eventNameEditText = findViewById(R.id.eventName);
        eventDateEditText = findViewById(R.id.eventDate);
        eventTimeEditText = findViewById(R.id.eventTime);
        eventDetailsEditText = findViewById(R.id.eventDetails);
        userIdEditText = findViewById(R.id.userId);
        userNameEditText = findViewById(R.id.userName);
        userDobEditText = findViewById(R.id.userDob);
        termsCheckBox = findViewById(R.id.termsCheckBox);
        submitButton = findViewById(R.id.submitButton);



        // Retrieve and set data from intent
        Intent intent = getIntent();
        eventNameEditText.setText(intent.getStringExtra("eventName"));
        eventDateEditText.setText(intent.getStringExtra("EVENT_DATE"));
        eventTimeEditText.setText(intent.getStringExtra("EVENT_TIME"));
        eventDetailsEditText.setText(intent.getStringExtra("EVENT_DETAILS"));
        userIdEditText.setText(intent.getStringExtra("USER_ID"));
        userNameEditText.setText(intent.getStringExtra("FULL_NAME"));
        userDobEditText.setText(intent.getStringExtra("DATE_OF_BIRTH"));



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (termsCheckBox.isChecked()) {
                    // Get form data
                    String eventName = eventNameEditText.getText().toString();
                    String eventDate = eventDateEditText.getText().toString();  // Ensure format is "YYYY-MM-DD"
                    String eventTime = eventTimeEditText.getText().toString();
                    String eventDetails = eventDetailsEditText.getText().toString();
                    String userId = userIdEditText.getText().toString();
                    String userName = userNameEditText.getText().toString();
                    String userDob = userDobEditText.getText().toString();

                    // Create a JSON object
                    JSONObject jsonRegistration = new JSONObject();
                    try {
                        jsonRegistration.put("eventName", eventName);
                        jsonRegistration.put("eventDate", eventDate);  // Ensure this matches LocalDate format
                        jsonRegistration.put("eventTime", eventTime);
                        jsonRegistration.put("eventDetails", eventDetails);
                        jsonRegistration.put("userId", userId);
                        jsonRegistration.put("userName", userName);
                        jsonRegistration.put("userDob", userDob);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Make the POST request
                    String url = "http://192.168.1.175:8090/api/registrations";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRegistration,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // Handle response
                                    if (response == null) {
                                        Toast.makeText(Registration.this, "You are already registered for this event.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Registration.this, "Registration submitted successfully!", Toast.LENGTH_SHORT).show();
                                        finish(); // Close the activity
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(Registration.this, "Failed to submit registration: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    // Add the request to the RequestQueue
                    RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
                    requestQueue.add(jsonObjectRequest);
                } else {
                    Toast.makeText(Registration.this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
