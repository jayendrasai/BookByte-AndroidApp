package com.example.myapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class filterBookFragment extends Fragment {
    private List<ModelBook> bookList;
    private List<ModelBook> filterbookList = new ArrayList<>();
    private CheckBox horror, romantic, poetry, psychology;
    private Button filterbtn;
    private RecycleViewAdapter1 adapter;

    // Factory method (simplified, removed unused parameters)
    public static filterBookFragment newInstance(List<ModelBook> bookList) {
        filterBookFragment fragment = new filterBookFragment();
        Bundle args = new Bundle();
        args.putSerializable("BOOKLIST", (Serializable) bookList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookList = (List<ModelBook>) getArguments().getSerializable("BOOKLIST");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_book, container, false);

        // Initialize UI components
        filterbtn = view.findViewById(R.id.filterbtn);
        horror = view.findViewById(R.id.horror);
        romantic = view.findViewById(R.id.romantic);
        poetry = view.findViewById(R.id.poetry);
        psychology = view.findViewById(R.id.psychology);
        RecyclerView recyclerView = view.findViewById(R.id.recycleview);

        // Set up RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecycleViewAdapter1(filterbookList, R.id.action_filterBookFragment_to_bookDetailFragment);
        recyclerView.setAdapter(adapter);

        // Initialize with full book list (optional)
        if (bookList != null) {
            filterbookList.addAll(bookList);
            adapter.notifyDataSetChanged();
        }

        // Set up filter button click listener
        filterbtn.setOnClickListener(v -> applyFilters());

        return view;
    }

    private void applyFilters() {
        if (bookList == null) return;

        // Clear previous filtered results
        filterbookList.clear();

        // Check which categories are selected
        boolean isHorrorChecked = horror.isChecked();
        boolean isRomanticChecked = romantic.isChecked();
        boolean isPoetryChecked = poetry.isChecked();
        boolean isPsychologyChecked = psychology.isChecked();

        // If no filters are selected, show all books
        if (!isHorrorChecked && !isRomanticChecked && !isPoetryChecked && !isPsychologyChecked) {
            filterbookList.addAll(bookList);
        } else {

            for (ModelBook book : bookList) {
                String category = book.getGenre();
                if ((isHorrorChecked && category.contains("Horror")) ||
                        (isRomanticChecked && category.contains("Romantic")) ||
                        (isPoetryChecked && category.contains("Poetry")) ||
                        (isPsychologyChecked && category.contains("Psychology"))) {
                    filterbookList.add(book);
                }
            }
        }

        // Notify adapter of data change
        adapter.notifyDataSetChanged();
    }
}