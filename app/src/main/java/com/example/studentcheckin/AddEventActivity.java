package com.example.studentcheckin;

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

public class AddEventActivity extends AppCompatActivity {

    private EditText editTextEventName;
    private EditText editTextEventDetails;
    private Button buttonSubmitEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        editTextEventName = findViewById(R.id.editTextEventName);
        editTextEventDetails = findViewById(R.id.editTextEventDetails);
        buttonSubmitEvent = findViewById(R.id.buttonSubmitEvent);

        buttonSubmitEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });
    }

    private void addEvent() {
        String eventName = editTextEventName.getText().toString();
        String eventDetails = editTextEventDetails.getText().toString();

        if (eventName.isEmpty() || eventDetails.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create JSON object to send to the backend
        JSONObject eventData = new JSONObject();
        try {
            eventData.put("name", eventName);
            eventData.put("details", eventDetails);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating event data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send data to backend
        String url = "http://192.168.1.175:8090/api/v1/events"; // Your backend URL
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                eventData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(AddEventActivity.this, "Event Added: " + eventName, Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity after successful submission
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddEventActivity.this, "Error adding event: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
}
