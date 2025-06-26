package com.example.myapp;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookDetailFragment extends Fragment {
    TextView bookTv,authorTv,genreTv,yearTv,descTv;
    List<ModelBook> bookList;
    ModelBook book;
    Button downloadbtn;
  //  ImageView img;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BookDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookDetailFragment newInstance(String param1, String param2) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
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
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_detail, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Now it's safe to access views
        bookTv = view.findViewById(R.id.bookname);
        authorTv = view.findViewById(R.id.authorname);
        genreTv = view.findViewById(R.id.genreid);
        yearTv = view.findViewById(R.id.yearid);
        descTv = view.findViewById(R.id.descid);
      //  img = view.findViewById(R.id.img);
        downloadbtn = view.findViewById(R.id.downloadbtn);

        downloadbtn.setOnClickListener(v -> {
            google(v);
            //Toast.makeText(this.getContext(), "PDF Downloaded", Toast.LENGTH_SHORT).show();
           // Navigation.findNavController(v).navigate(R.id.action_bookDetailFragment_to_homeScreenFragment2);
          //  Navigation.findNavController(v).popBackStack();

        });

        // Get the passed book object
        Bundle args = getArguments();
        if (args != null) {
            ModelBook book = (ModelBook) args.getSerializable("BOOK");
            if (book != null) {
                bookTv.setText(book.getTitle());
                authorTv.setText(book.getAuthor());
                genreTv.setText(book.getGenre());
                yearTv.setText(String.valueOf(book.getYear()));
                descTv.setText(book.getDescription());
               // img.setImageResource(book.getImage());
            }
        }



    }
    public void google(View view) {
        Uri u=Uri.parse("https://www.pdfbooksworld.com/gsearch.php");
        Intent i=new Intent(Intent.ACTION_VIEW,u);
        startActivity(i);
    }



}