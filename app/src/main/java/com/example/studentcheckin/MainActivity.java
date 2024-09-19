package com.example.studentcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
//    public void onLoginButtonClick(View view) {
//        // Intent to start LoginActivity (replace with your actual login activity)
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//    }

    // Method to handle signup button click
    public void onSignupButtonClick(View view) {
        // Intent to start Signup activity
        Intent intent = new Intent(MainActivity.this, Signup.class);
        startActivity(intent);
    }

    public void onLoginButtonClick(View view) {
        // Intent to start Signup activity
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }
}


