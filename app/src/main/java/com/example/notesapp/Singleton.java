package com.example.notesapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Singleton extends AppCompatActivity {
    private static ArrayList<Notes> listOfNotes = new ArrayList<>();
    private static Singleton singleton = null;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }

    public ArrayList<Notes> getListOfNotes() {
        return listOfNotes;
    }

    public void setListOfNotes(Notes notes) {
        listOfNotes.add(notes);
    }

    public void rearrangeData() {
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
