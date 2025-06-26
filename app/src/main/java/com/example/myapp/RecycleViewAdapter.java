package com.example.myapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    List<ModelBook> bookList;
    Context context;

    public RecycleViewAdapter(List<ModelBook> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelBook book = bookList.get(position);
        holder.tv1.setText(book.getTitle());
        holder.tv3.setText(String.valueOf(book.getYear()));
        holder.tv2.setText(book.getAuthor());
        //Glide.with(this).load("Imageurl").into(holder.tv4);
        holder.tv4.setText(book.getGenre());
       // holder.imgv.setImageResource(book.getImage());
        holder.itemView.setOnClickListener(view -> {
//            Intent intent = new Intent(context, BookDetailFragment.class);
//              intent.putExtra("BOOK_ID",book);
//            intent.putExtra("BOOK", (Serializable) book);
//            context.startActivity(intent);
//            NavController navController = Navigation.findNavController(view);
//            navController.navigate(R.id.action_homeScreenFragment2_to_bookDetailFragment);
            Bundle bundle = new Bundle();
            bundle.putSerializable("BOOK", book); // Make sure ModelBook implements Serializable

            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_homeScreenFragment2_to_bookDetailFragment, bundle);


        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv1, tv2, tv3, tv4;
       // ImageView imgv;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.BookName);
            tv2 = itemView.findViewById(R.id.AuthorName);
            tv3 = itemView.findViewById(R.id.year);
            tv4 = itemView.findViewById(R.id.genreid);
          // imgv = itemView.findViewById(R.id.imgid);
        }
    }
}