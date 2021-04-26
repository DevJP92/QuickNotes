package com.example.androidproject.note;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.androidproject.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class AddNoteActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText editTextAddTitle, editTextAddContent;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();
        editTextAddTitle = findViewById(R.id.textViewAppInfo);
        editTextAddContent = findViewById(R.id.editTextAddContent);
        progressBar = findViewById(R.id.progressBarAddNote);


        FloatingActionButton fabSave = findViewById(R.id.fabSave);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextAddTitle.getText().toString();
                String content = editTextAddContent.getText().toString();

                if(title.isEmpty() || content.isEmpty()){
                    Toast.makeText(AddNoteActivity.this, "Cannot save note with empty field.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save note to firebase
                // Reference document "Notes" (if Notes does not exist, firebase will create one)
                DocumentReference docRef = db.collection("Notes").document();
                Map<String, Object> note = new HashMap<>();
                note.put("title", title);
                note.put("content", content);

                progressBar.setVisibility(View.VISIBLE);
                // Setting note<title, content) to firebase collection of "Notes"
                docRef.set(note).addOnSuccessListener(aVoid -> {
                    // If success, show toast message and back to NoteActivity
                    Toast.makeText(AddNoteActivity.this, "Note added.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    onBackPressed();
                }).addOnFailureListener(e -> {
                    // If fail, show toast message
                    Toast.makeText(AddNoteActivity.this, "Error, Try again.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                });


            }
        });


    }

    // Handles event when menu_item x is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.closeItem:
                // Back to NoteActivity
                onBackPressed();
                Toast.makeText(this, "Note has not been saved.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Inflating the note_menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); inflater.inflate(R.menu.close_menu, menu);
        return true;
    }

    // Create an intent to be called from NoteActivity
    public static Intent createIntent(Context packageContext) {
        // Create the intent
        Intent i = new Intent(packageContext, AddNoteActivity.class);
        // Return the Intent from the method
        return i;
    }

}