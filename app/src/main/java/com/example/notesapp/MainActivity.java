package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements RecylerViewInterface {
    FloatingActionButton add;
    static RecyclerView recyclerView;
    static NotesAdapter adapter = null;
    static ArrayList<Notes> listOfNotes = new ArrayList<>();
    ImageView delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.rvNotesItem);
        delete = findViewById(R.id.delete);
        loadData();
        listData();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNotes.class);
                startActivityForResult(intent, 1);

            }
        });
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("Notes", MODE_PRIVATE);
        String note = sharedPreferences.getString("listOfNotes", "Nothing");
        if (!note.equals("Nothing")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Notes>>() {
            }.getType();
            listOfNotes = gson.fromJson(note, type);
            for (Notes notes : listOfNotes) {
                Singleton.getInstance().setListOfNotes(notes);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            listData();
        }
    }

    public void listData() {
        SharedPreferences sharedPreferences = getSharedPreferences("Notes", MODE_PRIVATE);
        String note = sharedPreferences.getString("listOfNotes", "Nothing");
        if (!note.equals("Nothing")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Notes>>() {
            }.getType();
            listOfNotes = gson.fromJson(note, type);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            adapter = new NotesAdapter(this, getApplicationContext(), listOfNotes);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_item, menu);
        MenuItem menuItem = menu.findItem(R.id.searchItem);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RecylerViewInterface recylerViewInterface = null;
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(int position) {
        Notes notes = Singleton.getInstance().getListOfNotes().get(position);
        if (notes != null) {
            String myTitle = notes.getTitle();
            String myDescription = notes.getDescription();
            Intent intent = new Intent(MainActivity.this, NotesActivity.class);
            intent.putExtra("title", myTitle);
            intent.putExtra("desc", myDescription);
            intent.putExtra("position", String.valueOf(position));
            startActivity(intent);
        }
    }
}