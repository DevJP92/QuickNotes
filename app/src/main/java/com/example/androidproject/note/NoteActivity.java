package com.example.androidproject.note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidproject.menu.AppInfoActivity;
import com.example.androidproject.menu.SettingActivity;
import com.example.androidproject.login.MainActivity;
import com.example.androidproject.R;
import com.example.androidproject.model.Note;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter<Note, NoteViewHolder> noteAdapter;

    private RecyclerView recyclerViewNotes;
    private Integer selectedNoteColor;
    private View noteActivityView;

    private static final int SHOW_SETTINGACTIVITY = 1;

    // SharedPreferences
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        noteActivityView = findViewById(R.id.noteActivityLayout);

        // SharedPreferences
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();
        // SharedPreferences: get color, white color as default
        mSharedPreferences.getInt(getString(R.string.color), R.color.white);




        FloatingActionButton fPlus = findViewById(R.id.fabPlus);
        // Finding recycler view notes
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);

        db = FirebaseFirestore.getInstance();

        // Query "Notes" database list by title in descending order
        Query query = db.collection("Notes").orderBy("title", Query.Direction.DESCENDING);

        // Once build(), it is going to store all returned queried database to "notes"
        FirestoreRecyclerOptions<Note> notes = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        noteAdapter = new FirestoreRecyclerAdapter<Note, NoteViewHolder>(notes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull Note note) {

//                final int colorCode = getRandomcolor();

                // setting titles and content into holder using getter methods from class "Note"
                noteViewHolder.noteTitle.setText(note.getTitle());
                noteViewHolder.noteContent.setText(note.getContent());

                // set note item to random colors
//                noteViewHolder.cardView.setCardBackgroundColor(noteViewHolder.view.getResources().getColor(colorCode,null));

                // Get note Id of current position note
                String nId = noteAdapter.getSnapshots().getSnapshot(i).getId();

                // start NoteDetailActivity when a note is clicked
                noteViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = NoteDetailActivity.createIntent(v.getContext());
                        i.putExtra("title", note.getTitle());
                        i.putExtra("content", note.getContent());
//                        i.putExtra("code", colorCode);
                        i.putExtra("noteId", nId);

                        // start NoteDetailActivity
                        v.getContext().startActivity(i);

                    }
                });

                // Create new ImageView
                ImageView menuIcon = noteViewHolder.view.findViewById(R.id.menuIcon);
                menuIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get current note (clicked)
                        final String nId = noteAdapter.getSnapshots().getSnapshot(i).getId();

                        // Creating popup menu using PopupMenu class
                        PopupMenu menu = new PopupMenu(v.getContext(), v);
                        // Setting gravity to end, so popup menu will show under a note
                        menu.setGravity(Gravity.END);
                        // Handling "Delete" menu event
                        menu.getMenu().add("Delete").setOnMenuItemClickListener((new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                // Assign current note to "docRef"
                                DocumentReference docRef = db.collection("Notes").document(nId);
                                docRef.delete().addOnSuccessListener(aVoid -> {
                                    // Note is deleted
                                }).addOnFailureListener(e ->
                                    Toast.makeText(NoteActivity.this, "Error, Try again.", Toast.LENGTH_SHORT).show());

                                return false;
                            }
                        }));

                        menu.show();
                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflating note_layout
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, parent, false);
                return new NoteViewHolder(view);
            }
        };


        recyclerViewNotes.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerViewNotes.setAdapter(noteAdapter);


        // When + button is clicked, start AddNoteActivity to add note
        fPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = AddNoteActivity.createIntent(v.getContext());
                startActivity(i);
            }
        });
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteTitle, noteContent;
        View view;
        CardView cardView;

        // Finding note title, note content and noteItem from note_layout
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.titles);
            noteContent = itemView.findViewById(R.id.content);
            cardView = itemView.findViewById(R.id.noteItem);
            view = itemView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case SHOW_SETTINGACTIVITY:
                if (resultCode == SettingActivity.RESULT_OK) {
                    selectedNoteColor = data.getIntExtra("color", R.color.white);

                    // SharedPreferences: retrieve color from SettingActivity, then put into color preferences
                    mEditor.putInt(getString(R.string.color), selectedNoteColor);
                    mEditor.commit();

                }
                break;
        }
    }

    // Get random color
    private int getRandomcolor() {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.light_pastel_purple);
        colorCode.add(R.color.light_pastel_blue);
        colorCode.add(R.color.pastel_yellow);
        colorCode.add(R.color.pastel_mint);
        colorCode.add(R.color.pastel_peach);
        colorCode.add(R.color.pastel_magenta);

        Random randColor = new Random();

        // Get random number of color of colorCode size
        int number = randColor.nextInt(colorCode.size());

        return  colorCode.get(number);
    }

    // Handles event when an menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.setting_menuitem:
                // Code for a setting goes here...
                Intent intentSetting = new Intent(NoteActivity.this, SettingActivity.class);
                startActivityForResult(intentSetting, SHOW_SETTINGACTIVITY);
                return true;
            case R.id.info_menuitem:
                // Code for app info goes here...
                Intent i = AppInfoActivity.createIntent(NoteActivity.this);
                startActivity(i);
                return true;
            case R.id.logout_menuitem:
                // Log out a user account from Firebase
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(NoteActivity.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Inflating the note_menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); inflater.inflate(R.menu.note_menu, menu);
        return true;
    }

    // When the activity is created and started, start listening noteAdapter for any change
    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    // onCreate() -> onStart() -> (onRestart()) -> onResume()

    @Override
    protected void onResume() {
        super.onResume();

        // SharedPreferences: get color, white color as default
        Integer colorPref = mSharedPreferences.getInt(getString(R.string.color), R.color.white);

        if(colorPref == null){
            colorPref = R.color.white;
        }
        // Find the root view of NoteActivity
        View root = noteActivityView.getRootView();
        // Setting NoteActivity background color
        root.setBackgroundColor(getResources().getColor(colorPref, null));
    }

    // When application is stopped, stop listening noteAdapter
    @Override
    protected void onStop() {
        super.onStop();
        if( noteAdapter != null){
            noteAdapter.stopListening();
        }

    }

    // Create an intent to be called from MainActivity
    public static Intent createIntent(Context packageContext) {
        // Create the intent
        Intent i = new Intent(packageContext, NoteActivity.class);
        // Return the Intent from the method
        return i;
    }

}