package com.example.androidproject.note;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.androidproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class NoteDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get data from NoteAdpater using getIntent()
        Intent data = getIntent();

        // Creating textView detailTitle and detailContent
        TextView content = findViewById(R.id.textViewDetailContent);
        TextView title = findViewById(R.id.textViewDetailTitle);

        // Enabling users to scroll textViewDetailContent vertically
        content.setMovementMethod(new ScrollingMovementMethod());

        // Setting text of title and content as data from NoteAdapter
        // "content", "title" KEY values from NoteAdapter.onBindViewHolder.holder.view.setOnClickListener
        content.setText(data.getStringExtra("content"));
        title.setText(data.getStringExtra("title"));
//        content.setBackgroundColor(getResources().getColor(data.getIntExtra("code", 0), null));


        FloatingActionButton fabEdit = findViewById(R.id.fabEdit);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(view.getContext(), EditNoteActivity.class);
                i.putExtra("title", data.getStringExtra("title"));
                i.putExtra("content", data.getStringExtra("content"));
//                i.putExtra("code", data.getIntExtra("code", 0));
                i.putExtra("noteId", data.getStringExtra("noteId"));

                startActivity(i);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    // Create an intent to be called from NoteActivity
    public static Intent createIntent(Context packageContext) {
        // Create the intent
        Intent i = new Intent(packageContext, NoteDetailActivity.class);
        // Return the Intent from the method
        return i;
    }
}