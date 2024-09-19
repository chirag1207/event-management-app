package com.example.studentcheckin;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;  // Import ImageView
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventCalendar extends AppCompatActivity {

    private CalendarView calendarView;
    private LinearLayout eventContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_calendar);

        // Back Arrow
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Navigate back to the previous activity
            }
        });

        calendarView = findViewById(R.id.calendarView);
        eventContainer = findViewById(R.id.eventContainer);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = new Date(year - 1900, month, dayOfMonth);
                String selectedDate = sdf.format(date);
                fetchEvents(selectedDate);
            }
        });
    }

    private void fetchEvents(String date) {
        String url = "http://192.168.1.175:8090/api/v1/events?date=" + date;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("EventCalendar", "Response: " + response.toString());

                        eventContainer.removeAllViews(); // Clear previous events

                        if (response.length() == 0) {
                            TextView noEventsTextView = new TextView(EventCalendar.this);
                            noEventsTextView.setText("No events found for this date.");
                            eventContainer.addView(noEventsTextView);
                            return;
                        }

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject eventSchedule = response.getJSONObject(i);
                                String eventName = eventSchedule.getString("eventName");
                                String eventDetail = eventSchedule.getString("eventDetail");
                                String eventTime = eventSchedule.getString("eventTime");

                                // Create a TextView for the event details
                                TextView eventTextView = new TextView(EventCalendar.this);
                                eventTextView.setText("Event: " + eventName + "\nDetails: " + eventDetail + "\nTime: " + eventTime);
                                eventTextView.setPadding(0, 0, 0, 16);
                                eventTextView.setTextSize(18);  // Increase font size
                                eventTextView.setTypeface(null, Typeface.BOLD);  // Make text bold
                                eventTextView.setPadding(16, 16, 16, 16);  // Set padding to improve spacing

                                // Create a Button for signing up for this event
                                Button signUpButton = new Button(EventCalendar.this);
                                signUpButton.setText("Register");
                                signUpButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        signUpForEvent(eventName, eventDetail, eventTime, date);
                                    }
                                });

                                // Add the TextView and Button to the container
                                eventContainer.addView(eventTextView);
                                eventContainer.addView(signUpButton);
                            }
                        } catch (JSONException e) {
                            Log.e("EventCalendar", "JSON parsing error", e);
                            Toast.makeText(EventCalendar.this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("EventCalendar", "Failed to fetch events", error);
                Toast.makeText(EventCalendar.this, "Failed to fetch events: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(EventCalendar.this);
        queue.add(jsonArrayRequest);
    }

    private void signUpForEvent(String eventName, String eventdetails, String eventTime, String date) {
        // Assuming you have user details available
        String userId = getIntent().getStringExtra("USER_ID");
        String fullName = getIntent().getStringExtra("FULL_NAME");
        String dateOfBirth = getIntent().getStringExtra("DATE_OF_BIRTH");

        Intent intent = new Intent(EventCalendar.this, Registration.class);
        Toast.makeText(EventCalendar.this, "HEYYYYY: " + fullName + " " + dateOfBirth + " " + userId + " " + eventName + " " + eventdetails + " " + eventTime, Toast.LENGTH_SHORT).show();

        intent.putExtra("eventName", eventName);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("DATE_OF_BIRTH", dateOfBirth);
        intent.putExtra("FULL_NAME", fullName);
        intent.putExtra("EVENT_DETAILS", eventdetails);
        intent.putExtra("EVENT_TIME", eventTime);
        intent.putExtra("EVENT_DATE", date);
        startActivity(intent);
    }
}
