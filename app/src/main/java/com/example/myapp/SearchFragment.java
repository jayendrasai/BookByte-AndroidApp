package com.example.myapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    private List<ModelBook> bookList;
    private List<ModelBook> searchBookList= new ArrayList<>();
    private ImageButton searchbtn;
    private TextInputEditText editText;
    private RecyclerView recyclerView;
    private RecycleViewAdapter1 adapter;
    private TextView emptyBookList;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2 , List<ModelBook> bookList) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putSerializable("BOOKLIST", (Serializable) bookList);
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
           // bookList = (List<ModelBook>) getArguments().getSerializable("BOOKLIST");

            bookList = (List<ModelBook>) getArguments().getSerializable("BOOKLIST");

        }
        else {
            bookList = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        //intialize views
        searchbtn = view.findViewById(R.id.searchbutton);
        editText = view.findViewById(R.id.etSearchQuery);
        recyclerView = view.findViewById(R.id.recycleview);
        emptyBookList = view.findViewById(R.id.emptyid);
        // Set up RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecycleViewAdapter1(searchBookList, R.id.action_searchFragment_to_bookDetailFragment);
        recyclerView.setAdapter(adapter);



        // Initialize with full book list (optional)
        if (bookList != null) {

            searchBookList.addAll(bookList);
            adapter.notifyDataSetChanged();
        }
        searchbtn.setOnClickListener(view1 -> {
            Log.d("SearchFragment", " before Search button clicked");
            searchBook();
            Log.d("SearchFragment", " After Search button clicked");
        });

        searchBook();
       // return inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    private void searchBook() {
        // Clear previous filtered results
        searchBookList.clear();

        String requireBook = editText.getText().toString().toLowerCase(Locale.ROOT);

        if(bookList != null && !bookList.isEmpty()) {
            for (ModelBook book : bookList) {
                String bookTitle = book.getTitle();
                Log.d("SearchFragment", "book getted");

                if(bookTitle.toLowerCase().contains(requireBook)){
                Log.d("SearchFragment", "before book Added");
                    searchBookList.add(book);
                    Log.d("SearchFragment", " after book Added");
                }

//                if (bookTitle != null && bookTitle.toLowerCase(Locale.ROOT).contains(requireBook)) {
//                    Log.d("SearchFragment", "book Added");
//                    searchBookList.add(book);
//                }

            }
        }

        if (searchBookList.isEmpty()) {
            Log.d("SearchFragment", "booklist is empty");
            emptyBookList.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyBookList.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        // Notify adapter of data change
        adapter.notifyDataSetChanged();

    }
}