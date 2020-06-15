package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ListView listView;
    static ArrayAdapter arrayAdapter;
    static ArrayList<String> notes;
    Intent intent;
    static int newFlag;

    static SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.addNote:
                newFlag = 0;
                startActivity(intent);
                return true;
            default:
                return false;
        }

    }

    static public void updateNotes(ArrayList<String> notes){

        try {
            sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(notes)).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            notes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notes",ObjectSerializer.serialize(new ArrayList<String>()) ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);


        listView = findViewById(R.id.lisView);

        notes = new ArrayList<>();

        try {
            notes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notes",ObjectSerializer.serialize(new ArrayList<String>()) ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (notes.isEmpty()) {

            notes.add("Example Note");

            try {
                sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(notes)).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(arrayAdapter);

        intent = new Intent(getApplicationContext(), NoteActivity.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newFlag = 1;
                intent.putExtra("noteIndex", position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                listView.setAdapter(arrayAdapter);
                                updateNotes(notes);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });


    }
}