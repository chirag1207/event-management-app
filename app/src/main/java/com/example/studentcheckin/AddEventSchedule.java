package com.example.studentcheckin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddEventSchedule extends AppCompatActivity {

    private EditText editTextEventDate;
    private EditText editTextEventTime;
    private Spinner spinnerEventId; // To hold Event IDs retrieved from the API
    private Button buttonSubmitSchedule;

    private ArrayList<String> eventIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_schedule);

        editTextEventDate = findViewById(R.id.editTextEventDate);
        editTextEventTime = findViewById(R.id.editTextEventTime);
        spinnerEventId = findViewById(R.id.spinnerEventId);
        buttonSubmitSchedule = findViewById(R.id.buttonSubmitSchedule);

        // Fetch Event IDs from API
        fetchEventIds();

        buttonSubmitSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEventSchedule();
            }
        });
    }

    private void fetchEventIds() {
        String url = "http://192.168.1.175:8090/api/v1/events"; // Replace with your actual API endpoint

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray eventsArray = response.getJSONArray("events"); // Assuming "events" is the key
                            for (int i = 0; i < eventsArray.length(); i++) {
                                JSONObject event = eventsArray.getJSONObject(i);
                                String eventId = event.getString("id"); // Assuming "id" is the key for event ID
                                eventIds.add(eventId);
                            }
                            // Populate spinner with event IDs
                            // (Implement a method to set the adapter for spinner here)
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddEventSchedule.this, "Error retrieving events", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddEventSchedule.this, "Error fetching event IDs", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(AddEventSchedule.this);
        queue.add(jsonObjectRequest);
    }

    private void addEventSchedule() {
        String eventDate = editTextEventDate.getText().toString();
        String eventTime = editTextEventTime.getText().toString();
        String eventId = spinnerEventId.getSelectedItem().toString(); // Get selected Event ID

        if (eventDate.isEmpty() || eventTime.isEmpty() || eventId.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Here you would send the schedule data to your backend
        // For example, you could use a Volley request to send the data

        Toast.makeText(this, "Schedule Added for Event ID: " + eventId, Toast.LENGTH_SHORT).show();
        finish(); // Close the activity after submission
    }
}
