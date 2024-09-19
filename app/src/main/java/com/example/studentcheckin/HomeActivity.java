package com.example.studentcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class HomeActivity extends AppCompatActivity {

    private CardView cardPersonalInfo;
    private CardView cardEventsList;
    private CardView cardRegisteredEvents;
    private String userId;
    private String dateOfBirth;
    private String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cardPersonalInfo = findViewById(R.id.cardPersonalInfo);
        cardEventsList = findViewById(R.id.cardEventsList);
        cardRegisteredEvents = findViewById(R.id.cardRegisteredEvents);

        userId = getIntent().getStringExtra("USER_ID");
        fullName = getIntent().getStringExtra("FULL_NAME");
        dateOfBirth = getIntent().getStringExtra("DATE_OF_BIRTH");

        cardPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PersonalInfo.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("DATE_OF_BIRTH", dateOfBirth);
                intent.putExtra("FULL_NAME", fullName);
                startActivity(intent);
            }
        });

        cardEventsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EventCalendar.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("DATE_OF_BIRTH", dateOfBirth);
                intent.putExtra("FULL_NAME", fullName);
                startActivity(intent);
            }
        });

        cardRegisteredEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RegisteredEvents.class);
                Toast.makeText(HomeActivity.this, "HEYYYYY: " + fullName  + dateOfBirth + userId, Toast.LENGTH_SHORT).show();


                intent.putExtra("USER_ID", userId);

                intent.putExtra("DOB", dateOfBirth);
                intent.putExtra("FULL_NAME", fullName);

//                intent.putExtra("USER_ID", user);
                startActivity(intent);
            }
        });
    }
}
