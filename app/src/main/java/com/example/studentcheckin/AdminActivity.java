package com.example.studentcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private Button buttonAddEvent;
    private Button buttonAddEventSchedule;
    private Button buttonShowEvents;
    private Button buttonDeleteEvent;
    private Button buttonViewParticipants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        buttonAddEvent = findViewById(R.id.buttonAddEvent);
        buttonAddEventSchedule = findViewById(R.id.buttonAddEventSchedule);
        buttonShowEvents = findViewById(R.id.buttonShowEvents);
        buttonDeleteEvent = findViewById(R.id.buttonDeleteEvent);
        buttonViewParticipants = findViewById(R.id.buttonViewParticipants);

        buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AddEventActivity.class);
                startActivity(intent);
                Toast.makeText(AdminActivity.this, "Add Event clicked", Toast.LENGTH_SHORT).show();
            }
        });


        buttonAddEventSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AddEventSchedule.class);
                startActivity(intent);
                Toast.makeText(AdminActivity.this, "Show Events clicked", Toast.LENGTH_SHORT).show();
            }
        });

        buttonShowEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AddEventActivity.class);
                Toast.makeText(AdminActivity.this, "Show Events clicked", Toast.LENGTH_SHORT).show();
            }
        });

        buttonDeleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement Delete Event functionality
                Toast.makeText(AdminActivity.this, "Delete Event clicked", Toast.LENGTH_SHORT).show();
            }
        });

        buttonViewParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement View Participants functionality
                Toast.makeText(AdminActivity.this, "View Participants clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
