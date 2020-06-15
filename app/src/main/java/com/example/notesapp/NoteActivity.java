package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class NoteActivity extends AppCompatActivity {

    EditText editText;
    int noteIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        editText = (EditText) findViewById(R.id.editTextBox);

        Intent intent = getIntent();

        if (MainActivity.newFlag==1) {
            noteIndex = intent.getIntExtra("noteIndex", 0);

            editText.setText(MainActivity.notes.get(noteIndex));


        } else {
            editText.setText("");
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String currentNote = editText.getText().toString();

                if (currentNote!=""){
                    if (MainActivity.newFlag==1) {
                        MainActivity.notes.set(noteIndex, currentNote);
                    } else {
                        MainActivity.newFlag = 1;
                        MainActivity.notes.add(currentNote);
                        noteIndex = MainActivity.notes.indexOf(currentNote);
                    }
                    MainActivity.listView.setAdapter(MainActivity.arrayAdapter);
                    MainActivity.updateNotes(MainActivity.notes);
                }
            }
        });



    }
}