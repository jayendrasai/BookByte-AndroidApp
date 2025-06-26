package com.example.myapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapter1 extends RecyclerView.Adapter<RecycleViewAdapter1.MyViewHolder> {
    List<ModelBook> booklist;
   // Context context;
    private final int navActionId;
    public RecycleViewAdapter1(List<ModelBook> booklist, int navActionId) {
        this.booklist = booklist != null ? booklist : new ArrayList<>();
       // this.booklist = booklist;
        this.navActionId = navActionId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelBook book = booklist.get(position);
        holder.tv1.setText(book.getTitle());
        holder.tv2.setText(book.getAuthor());
        holder.tv3.setText(String.valueOf(book.getYear()));
        holder.tv4.setText(book.getGenre());
        //holder.imgv.setImageResource(book.getImage());

//        holder.itemView.setOnClickListener(view -> {
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("BOOK", book);
//            NavController navController = Navigation.findNavController(view);
//            navController.navigate(navActionId,bundle);
//        });
        holder.itemView.setOnClickListener(view -> {
            Log.d("BookAdapter", "Item clicked at position: " + position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("BOOK", book);
            NavController navController = Navigation.findNavController(view);
            try {
                navController.navigate(navActionId, bundle);
            } catch (Exception e) {
                Log.e("BookAdapter", "Navigation failed: " + e.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
       // return booklist.size();
        return booklist != null ? booklist.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv1, tv2, tv3, tv4;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.BookName);
            tv2 = itemView.findViewById(R.id.AuthorName);
            tv3 = itemView.findViewById(R.id.year);
            tv4 = itemView.findViewById(R.id.genreid);
        }
    }
}

