package com.example.studentcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class EditPersonalInfo extends AppCompatActivity {

    private EditText editTextFullName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPhoneNumber;
    private EditText editTextDOB;
    private EditText editTextHeight;
    private EditText editTextWeight;
    private EditText editTextStreetName;
    private EditText editTextCity;
    private EditText editTextState;
    private EditText editTextPincode;
    private Button buttonSave;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextDOB = findViewById(R.id.editTextDOB);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextStreetName = findViewById(R.id.editTextStreetName);
        editTextCity = findViewById(R.id.editTextCity);
        editTextState = findViewById(R.id.editTextState);
        editTextPincode = findViewById(R.id.editTextPincode);
        buttonSave = findViewById(R.id.buttonSave);

        userId = getIntent().getStringExtra("USER_ID");

        fetchUserInfo();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });
    }

    private void fetchUserInfo() {
        String url = "http://192.168.1.175:8090/api/v1/user/" + userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            editTextFullName.setText(response.getString("fullName"));
                            editTextEmail.setText(response.getString("email"));
                            editTextPassword.setText(response.getString("password"));
                            editTextPhoneNumber.setText(response.getString("phone"));
                            editTextDOB.setText(response.getString("dob"));
                            editTextHeight.setText(String.valueOf(response.getInt("height")));
                            editTextWeight.setText(String.valueOf(response.getInt("weight")));
                            editTextStreetName.setText(response.getString("streetName"));
                            editTextCity.setText(response.getString("city"));
                            editTextState.setText(response.getString("state"));
                            editTextPincode.setText(response.getString("pincode"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditPersonalInfo.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditPersonalInfo.this, "Failed to fetch user info", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(EditPersonalInfo.this);
        queue.add(jsonObjectRequest);
    }

    private void updateUserInfo() {
        String url = "http://192.168.1.175:8090/api/v1/user/update/" + userId;

        JSONObject user = new JSONObject();
        try {
            user.put("fullName", editTextFullName.getText().toString());
            user.put("email", editTextEmail.getText().toString());
            user.put("password", editTextPassword.getText().toString());
            user.put("phone", editTextPhoneNumber.getText().toString());
            user.put("dob", editTextDOB.getText().toString());
            user.put("height", Integer.parseInt(editTextHeight.getText().toString()));
            user.put("weight", Integer.parseInt(editTextWeight.getText().toString()));
            user.put("streetName", editTextStreetName.getText().toString());
            user.put("city", editTextCity.getText().toString());
            user.put("state", editTextState.getText().toString());
            user.put("pincode", Integer.parseInt(editTextPincode.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(EditPersonalInfo.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditPersonalInfo.this, PersonalInfo.class);
                        intent.putExtra("USER_ID", userId); // Pass userId to the UserInfoActivity if needed
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditPersonalInfo.this, "Failed to update user info", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(EditPersonalInfo.this);
        queue.add(jsonObjectRequest);
    }
}
