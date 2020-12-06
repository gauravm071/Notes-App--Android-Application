package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;

import static android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS;

public class AddNotes extends AppCompatActivity {
    EditText addTitle, addDescription;
    RecyclerView recyclerView;
    ArrayList<Notes> listOfNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        addTitle = findViewById(R.id.noteTitle);
        addDescription = findViewById(R.id.noteDescription);
        addDescription.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        saveData();
        return true;
    }

    public void saveData() {
        String myTitle = addTitle.getText().toString();
        String myDescription = addDescription.getText().toString();

        Notes notes = new Notes();
        notes.setTitle(myTitle);
        notes.setDescription(myDescription);
        Singleton.getInstance().setListOfNotes(notes);
        SharedPreferences sharedPreferences = getSharedPreferences("Notes", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String note = gson.toJson(Singleton.getInstance().getListOfNotes());
        editor.putString("listOfNotes", note).apply();
        setResult(RESULT_OK);
        finish();
    }

    public void deletePreference() {
        SharedPreferences preferences = getSharedPreferences("Notes", MODE_PRIVATE);
        preferences.edit().remove("listOfNotes").apply();
        SharedPreferences sharedPreferences = getSharedPreferences("Notes", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String note = gson.toJson(Singleton.getInstance().getListOfNotes());
        editor.putString("listOfNotes", note).apply();
        editor.commit();
    }
}