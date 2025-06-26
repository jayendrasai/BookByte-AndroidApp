package com.example.myapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountDetailFragment extends Fragment {
    private Button logoutButton;
    private TextView userName,userEmail,userGender,userRole;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountDetailFragment newInstance(String param1, String param2) {
        AccountDetailFragment fragment = new AccountDetailFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_detail, container, false);
        // Inflate the layout for this fragment
        //intialize viewa
        logoutButton = view.findViewById(R.id.logoutButton);
        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.mailId);
        userGender = view.findViewById(R.id.genderid);
        userRole = view.findViewById(R.id.roleid);

        // Display user info and check role
        try {
            loadUserData();
        } catch (Exception e) {

            Toast.makeText(requireContext(), "Error loading user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
          //  Navigation.findNavController(view).navigate(R.id.action_homeScreenFragment2_to_loginFragment);
        }
        logoutButton.setOnClickListener(v -> {
            try {
                mAuth.signOut();
                Navigation.findNavController(v).navigate(R.id.action_accountDetailFragment_to_loginFragment);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Logout error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private void loadUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userEmail.setText(user.getEmail() != null ? user.getEmail() : "N/A");
            db.collection("users").document(user.getUid()).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                userName.setText(document.getString("name") != null ? document.getString("name") : "N/A");
                                userGender.setText(document.getString("gender") != null ? document.getString("gender") : "N/A");
                                userRole.setText(document.getString("role") != null ? document.getString("role") : "N/A");
                            } else {
                                Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(requireContext(), "Failed to fetch user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(requireContext(), "No user logged in", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigate(R.id.action_accountDetailFragment_to_loginFragment);
        }
    }
}