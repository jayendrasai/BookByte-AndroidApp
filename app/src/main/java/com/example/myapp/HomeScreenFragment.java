package com.example.myapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeScreenFragment extends Fragment {
    private List<ModelBook> bookList;
    private TextView welcomeText;
    private Button searchBtn,filterBtn,  addBookButton;
    private ImageButton logoutButton;
    private ImageButton accountBtn;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public HomeScreenFragment() {}

    public static HomeScreenFragment newInstance(String param1, String param2) {
        HomeScreenFragment fragment = new HomeScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            getArguments().getString(ARG_PARAM1);
            getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);

        // Initialize views
        welcomeText = view.findViewById(R.id.title);
        filterBtn = view.findViewById(R.id.filter);
        searchBtn = view.findViewById(R.id.searchbtn);
        accountBtn = view.findViewById(R.id.profilebtn);
       // logoutButton = view.findViewById(R.id.logoutButton);
        addBookButton = view.findViewById(R.id.addBookButton);
        recyclerView = view.findViewById(R.id.recycleview);

        // Initialize book list and RecyclerView
        bookList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new RecycleViewAdapter(bookList, requireContext());
        recyclerView.setAdapter(myAdapter);

        // Display user info and check role
        try {
            displayUserInfo(view);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error loading user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.action_homeScreenFragment2_to_loginFragment);
        }

        // Set up button listeners
        filterBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("BOOKLIST", (Serializable) bookList);
            try {
                Navigation.findNavController(v).navigate(R.id.action_homeScreenFragment2_to_filterBookFragment, bundle);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Navigation error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        searchBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("BOOKLIST", (Serializable) bookList);
            try {
                Navigation.findNavController(v).navigate(R.id.action_homeScreenFragment2_to_searchFragment, bundle);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Navigation error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        accountBtn.setOnClickListener(v -> {
            try {
               // mAuth.signOut();
                Navigation.findNavController(v).navigate(R.id.action_homeScreenFragment2_to_accountDetailFragment);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Logout error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        addBookButton.setOnClickListener(v -> {
            try {
                Navigation.findNavController(v).navigate(R.id.action_homeScreenFragment2_to_addBookFragment);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Navigation error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch books from Firestore
        try {
            fillBookList();
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error loading books: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void displayUserInfo(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid()).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String role = document.getString("role");
//                                String displayName = user.getDisplayName() != null ? user.getDisplayName() : role;
//                                welcomeText.setText("Welcome, " + displayName);
                                if ("Admin".equals(role)) {
                                    addBookButton.setVisibility(View.VISIBLE);
                                } else if (!"User".equals(role)) {
                                    Toast.makeText(requireContext(), "Access denied", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(view).navigate(R.id.action_homeScreenFragment2_to_loginFragment);
                                }
                            } else {
                                Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.action_homeScreenFragment2_to_loginFragment);
                            }
                        } else {
                            Toast.makeText(requireContext(), "Failed to fetch user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(requireContext(), "No user logged in", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.action_homeScreenFragment2_to_loginFragment);
        }
    }

    private void fillBookList() {
        db.collection("books")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bookList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ModelBook book = document.toObject(ModelBook.class);
                            bookList.add(book);
                        }
                        myAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(requireContext(), "Failed to fetch books: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}