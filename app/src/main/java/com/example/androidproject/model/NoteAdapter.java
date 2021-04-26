package com.example.androidproject.model;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.note.NoteDetailActivity;
import com.example.androidproject.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// NOT IN USED. NoteAdapter is implemented in NoteActivity.

//public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
//
//    // create list of strings for note titles and content
//    private List<String> titles;
//    private List<String> content;
//
//    // constructor
//    public NoteAdapter(List<String> title, List<String> content) {
//        this.titles = title;
//        this.content = content;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        // Inflating note_layout
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
//        // setting titles and content into holder
//        holder.noteTitle.setText(titles.get(position));
//        holder.noteContent.setText(content.get(position));
//
//        final int colorCode = getRandomcolor();
//        // set note item to random colors
//        holder.cardView.setCardBackgroundColor(holder.view.getResources().getColor(colorCode, null));
//
//        // start NoteDetailActivity when a note is clicked
//        holder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = NoteDetailActivity.createIntent(v.getContext());
//                // Whenever an user clicks a first item of recyclerView, it's going to get position as zero
//                // Then extracting data from title or content of position
//                i.putExtra("title", titles.get(position));
//                i.putExtra("content", content.get(position));
//                i.putExtra("code", colorCode);
//
//                // start NoteDetailActivity
//                v.getContext().startActivity(i);
//
//            }
//        });
//    }
//
//    // Get random color
//    private int getRandomcolor() {
//        List<Integer> colorCode = new ArrayList<>();
//        colorCode.add(R.color.light_pastel_purple);
//        colorCode.add(R.color.light_pastel_blue);
//        colorCode.add(R.color.pastel_yellow);
//        colorCode.add(R.color.pastel_mint);
//        colorCode.add(R.color.pastel_peach);
//        colorCode.add(R.color.pastel_magenta);
//
//        Random randColor = new Random();
//
//        // Get random number of color of colorCode size
//        int number = randColor.nextInt(colorCode.size());
//
//        return  colorCode.get(number);
//    }
//
//    // Get number(s) of title notes
//    @Override
//    public int getItemCount() {
//        return titles.size();
//    }
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//
//        TextView noteTitle, noteContent;
//        View view;
//        CardView cardView;
//
//        // Finding note title and note content from note_layout
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            noteTitle = itemView.findViewById(R.id.titles);
//            noteContent = itemView.findViewById(R.id.content);
//            cardView = itemView.findViewById(R.id.noteItem);
//            view = itemView;
//        }
//    }
//}
