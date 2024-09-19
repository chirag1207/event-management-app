package com.example.studentcheckin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        // Set up focus and keyboard handling
        editTextUsername.postDelayed(() -> {
            editTextUsername.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editTextUsername, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100); // Introduce a slight delay to ensure UI is ready

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Redirecting to registration", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });
    }

    private void authenticateUser() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String adminEmail = "chiragnarkar@gmail.com"; // replace with actual admin email
        String adminPassword = "Chirag"; // replace with actual admin password

        if (username.equals(adminEmail) && password.equals(adminPassword)) {
            // Redirect to AdminActivity
            Intent intent = new Intent(Login.this, AdminActivity.class);
            startActivity(intent);
            return;
        }

        String url = "http://192.168.1.175:8090/api/v1/user/login";

        HashMap<String, String> params = new HashMap<>();
        params.put("email", username);
        params.put("password", password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String fullName = response.getString("fullName");
                            String dateOfBirth = response.getString("dob");
                            String userId = response.getString("id");
                            Toast.makeText(Login.this, "Hello, " + fullName, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, HomeActivity.class);
                            intent.putExtra("USER_ID", userId);
                            intent.putExtra("DATE_OF_BIRTH", dateOfBirth);
                            intent.putExtra("FULL_NAME", fullName);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Login.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {



            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Unexpected error occurred"; // Default message

                // Check for a network or server response error
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        // Convert response data (byte array) to a string
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject jsonObject = new JSONObject(responseBody);

                        // Extract the "message" field from the JSON object if it exists
                        errorMessage = jsonObject.optString("message", errorMessage); // Default message if "message" field is not found
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle the common connection-related errors
                    if (error instanceof TimeoutError) {
                        errorMessage = "Request timed out. Please check your connection.";
                    } else if (error instanceof NoConnectionError) {
                        errorMessage = "No connection. Please check your internet settings.";
                    } else if (error instanceof AuthFailureError) {
                        errorMessage = "Authentication failure. Please check your credentials.";
                    } else if (error instanceof ServerError) {
                        errorMessage = "Server error. Please try again later.";
                    } else if (error instanceof NetworkError) {
                        errorMessage = "Network error. Please check your internet connection.";
                    } else if (error instanceof ParseError) {
                        errorMessage = "Parsing error. Please try again.";
                    }
                }

                // Show the error message to the user
                Toast.makeText(Login.this, errorMessage, Toast.LENGTH_LONG).show();
            }




        });

        RequestQueue queue = Volley.newRequestQueue(Login.this);
        queue.add(jsonObjectRequest);
    }


}
