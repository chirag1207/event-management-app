package com.example.studentcheckin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisteredEvents extends AppCompatActivity {

    private LinearLayout registeredEventsContainer;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_events);

        registeredEventsContainer = findViewById(R.id.registeredEventsContainer);
        userId = getIntent().getStringExtra("USER_ID");

        fetchRegisteredEvents(userId);
    }

    private void fetchRegisteredEvents(String userId) {
        String url = "http://192.168.1.175:8090/api/registrations/user/" + userId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("RegisteredEvents", "Response: " + response.toString());

                        registeredEventsContainer.removeAllViews(); // Clear previous events

                        if (response.length() == 0) {
                            TextView noEventsTextView = new TextView(RegisteredEvents.this);
                            noEventsTextView.setText("No registered events found.");
                            registeredEventsContainer.addView(noEventsTextView);
                            return;
                        }

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject event = response.getJSONObject(i);
                                String eventName = event.getString("eventName");
                                String eventDetail = event.getString("eventDetails");
                                String eventTime = event.getString("eventTime");

                                // Inflate the card layout
                                View cardView = LayoutInflater.from(RegisteredEvents.this).inflate(R.layout.event_card, registeredEventsContainer, false);

                                // Set data to the card views
                                TextView eventNameTextView = cardView.findViewById(R.id.eventName);
                                TextView eventDetailsTextView = cardView.findViewById(R.id.eventDetails);
                                TextView eventTimeTextView = cardView.findViewById(R.id.eventTime);

                                eventNameTextView.setText(eventName);
                                eventDetailsTextView.setText(eventDetail);
                                eventTimeTextView.setText(eventTime);

                                // Set background color for zebra striping
                                if (i % 2 == 0) {
                                    cardView.setBackgroundColor(getResources().getColor(R.color.light_gray)); // Light gray for even rows
                                } else {
                                    cardView.setBackgroundColor(getResources().getColor(R.color.white)); // White for odd rows
                                }

                                // Add the card view to the container
                                registeredEventsContainer.addView(cardView);
                            }
                        } catch (JSONException e) {
                            Log.e("RegisteredEvents", "JSON parsing error", e);
                            Toast.makeText(RegisteredEvents.this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RegisteredEvents", "Failed to fetch events", error);
                Toast.makeText(RegisteredEvents.this, "Failed to fetch events: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(RegisteredEvents.this);
        queue.add(jsonArrayRequest);
    }
}
