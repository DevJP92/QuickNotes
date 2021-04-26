package com.example.androidproject.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.androidproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SettingActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Integer selectedNoteColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        progressBar = findViewById(R.id.progressBarSetting);
        selectedNoteColor = R.color.light_pastel_blue;
        initColorSetting();

        FloatingActionButton fabSaveSetting = findViewById(R.id.fabSaveSetting);
        fabSaveSetting.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);

            Intent result = new Intent();
            result.putExtra("color", selectedNoteColor);
            setResult(RESULT_OK, result);
            finish();

        });

        progressBar.setVisibility(View.GONE);
    }


    private void initColorSetting() {
        final ImageView imageColor1 = findViewById(R.id.imageColor1);
        final ImageView imageColor2 = findViewById(R.id.imageColor2);
        final ImageView imageColor3 = findViewById(R.id.imageColor3);
        final ImageView imageColor4 = findViewById(R.id.imageColor4);
        final ImageView imageColor5 = findViewById(R.id.imageColor5);

        findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageColor1.setImageResource(R.drawable.ic_baseline_done_24);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);

                // Setting note background to default color
                selectedNoteColor = R.color.light_pastel_blue;

                Toast.makeText(SettingActivity.this, "You picked pastel blue.", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_baseline_done_24);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);

                // Setting note background to color2
                selectedNoteColor = R.color.pastel_magenta;
                Toast.makeText(SettingActivity.this, "You picked pastel magenta.", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_baseline_done_24);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);

                // Setting note background to color3
                selectedNoteColor = R.color.pastel_yellow;
                Toast.makeText(SettingActivity.this, "You picked pastel yellow.", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_baseline_done_24);
                imageColor5.setImageResource(0);

                // Setting note background to color4
                selectedNoteColor = R.color.light_pastel_purple;
                Toast.makeText(SettingActivity.this, "You picked pastel purple.", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_baseline_done_24);

                // Setting note background to color5
                selectedNoteColor = R.color.pastel_mint;
                Toast.makeText(SettingActivity.this, "You picked pastel mint.", Toast.LENGTH_SHORT).show();
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