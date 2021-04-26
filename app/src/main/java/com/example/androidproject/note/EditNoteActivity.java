package com.example.androidproject.note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.androidproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditNoteActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Intent data;
    private EditText editTextTitle, editTextContent;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        progressBar = findViewById(R.id.progressBarEditNote);

        db = FirebaseFirestore.getInstance();

        // Get all data passed into EditNoteActivity and assign to "data"
        data = getIntent();

        String title = data.getStringExtra("title");
        String content = data.getStringExtra("content");

        editTextTitle.setText(title);
        editTextContent.setText(content);

        FloatingActionButton fabSaveEdit = findViewById(R.id.fabSaveEdit);
        fabSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString();
                String content = editTextContent.getText().toString();

                if(title.isEmpty() || content.isEmpty()){
                    Toast.makeText(EditNoteActivity.this, "Cannot save note with empty field.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update note to firebase
                // Pass collection of "Notes", then get document of current noteId
                DocumentReference docRef = db.collection("Notes").document(data.getStringExtra("noteId"));

                Map<String, Object> note = new HashMap<>();
                note.put("title", title);
                note.put("content", content);

                progressBar.setVisibility(View.VISIBLE);
                // Updating  note<title, content) to firebase collection of "Notes"
                docRef.update(note).addOnSuccessListener(aVoid -> {
                    // If success, show toast message and back to NoteActivity
                    Toast.makeText(EditNoteActivity.this, "Note saved.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), NoteActivity.class));
                }).addOnFailureListener(e -> {
                    // If fail, show toast message
                    Toast.makeText(EditNoteActivity.this, "Error, Try again.", Toast.LENGTH_SHORT).show();
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

}