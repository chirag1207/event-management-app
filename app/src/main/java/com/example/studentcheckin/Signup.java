package com.example.studentcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;
import java.util.Calendar;
import java.util.UUID;

public class Signup extends AppCompatActivity {

    EditText full_name, dob, height, weight, email, password, phone, street_name, city, state, pincode;
    Button submit;
    private TextView textViewRegister;
        private static final Pattern EMAIL_PATTERN = Pattern.compile(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        textViewRegister = findViewById(R.id.textViewRegister);
        full_name = findViewById(R.id.full_name);
        dob = findViewById(R.id.dob);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phoneNumber);
        street_name = findViewById(R.id.streetName);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        pincode = findViewById(R.id.pinCode);

        submit = findViewById(R.id.submit);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    processFormFields();
                }
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle register action
                Toast.makeText(Signup.this, "Redirecting to Login", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean validateForm() {
        String emailInput = email.getText().toString();
        String phoneInput = phone.getText().toString();
        if (full_name.getText().toString().isEmpty() ||
                dob.getText().toString().isEmpty() ||
                height.getText().toString().isEmpty() ||
                weight.getText().toString().isEmpty() ||
                email.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty() ||
                phone.getText().toString().isEmpty() ||
                street_name.getText().toString().isEmpty() ||
                city.getText().toString().isEmpty() ||
                state.getText().toString().isEmpty() ||
                pincode.getText().toString().isEmpty()) {
            Toast.makeText(Signup.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!EMAIL_PATTERN.matcher(emailInput).matches()) {
            Toast.makeText(Signup.this, "Invalid email format.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check phone number length
        if (phoneInput.length() != 10) {
            Toast.makeText(Signup.this, "Phone number must be exactly 10 digits.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Additional validation can be added here (e.g., email format, password strength)
        return true;
    }
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Signup.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public void processFormFields() {
        RequestQueue queue = Volley.newRequestQueue(Signup.this);
        String url = "http://192.168.1.175:8090/api/v1/user/register";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SignupResponse", response);
                try {
                    JSONObject jsonResponse = new JSONObject(response); // Parse the JSON response
                    String message = jsonResponse.getString("message"); // Get the message

                    if (message.equalsIgnoreCase("User Registered")) {
                        clearFormFields();
                        Toast.makeText(Signup.this, "Registration Successful", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Signup.this, "Registration unsuccessful: " + message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e("SignupError", "JSON parsing error: " + e.getMessage());
                    Toast.makeText(Signup.this, "Registration unsuccessful: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SignupError", "Error: " + error.toString());

                String errorMessage = "Registration unsuccessful: ";
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    errorMessage += new String(error.networkResponse.data);
                } else {
                    errorMessage += error.getMessage();
                }

                Toast.makeText(Signup.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("full_name", full_name.getText().toString());
                params.put("dob", dob.getText().toString());
                params.put("height", height.getText().toString());
                params.put("weight", weight.getText().toString());
                params.put("email", email.getText().toString());
                params.put("password", password.getText().toString());
                params.put("phone", phone.getText().toString());
                params.put("street_name", street_name.getText().toString());
                params.put("city", city.getText().toString());
                params.put("state", state.getText().toString());
                params.put("pincode", pincode.getText().toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        queue.add(stringRequest);
    }





    private void clearFormFields() {
        full_name.setText(null);
        dob.setText(null);
        height.setText(null);
        weight.setText(null);
        email.setText(null);
        password.setText(null);
        phone.setText(null);
        street_name.setText(null);
        city.setText(null);
        state.setText(null);
        pincode.setText(null);
    }
}
