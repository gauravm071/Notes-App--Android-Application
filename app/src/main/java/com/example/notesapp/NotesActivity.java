package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class NotesActivity extends AppCompatActivity {
    TextView titleView, descriptionView;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("desc");
        position = Integer.parseInt(intent.getStringExtra("position"));
        titleView = findViewById(R.id.noteTitle);
        descriptionView = findViewById(R.id.noteDescription);
        titleView.setText(title);
        descriptionView.setText(description);

    }
}