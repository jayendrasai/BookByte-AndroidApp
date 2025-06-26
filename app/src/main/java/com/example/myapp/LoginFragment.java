package com.example.myapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private EditText emailInput, passwordInput;
    private  TextView goToRegisterView;
    private Button loginButton, goToRegisterButton;
    private ImageButton helpline;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initializeViews(view);
        helpline.setOnClickListener(view1 -> {
            dial(view1);
        });

        loginButton.setOnClickListener(v -> {
            if (validateInputs()) {
                loginUser(view);
            }
        });


        goToRegisterView.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.action_loginFragment_to_createAccount);
        });

        return view;
    }

//    public void onStart() {
//        super.onStart();
//        // Check if user is already logged in
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            // User is logged in, fetch their role and navigate
//            String userId = currentUser.getUid();
//            db.collection("users").document(userId).get()
//                    .addOnCompleteListener(roleTask -> {
//                        if (roleTask.isSuccessful()) {
//                            DocumentSnapshot document = roleTask.getResult();
//                            if (document.exists()) {
//                                String role = document.getString("role");
//                                Toast.makeText(requireContext(), "Welcome back", Toast.LENGTH_SHORT).show();
//                                // Navigate to HomeScreenFragment
//                                Navigation.findNavController(requireView())
//                                        .navigate(R.id.action_loginFragment_to_homeScreenFragment2);
//                            } else {
//                                Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(requireContext(), "Failed to fetch user data: " +
//                                    roleTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//    }

    private void dial(View view1) {
        Uri s=Uri.parse("tel:9908443777");
        Intent i1=new Intent(Intent.ACTION_DIAL,s);
        startActivity(i1);
    }

    private void initializeViews(View view) {
        emailInput = view.findViewById(R.id.email);
        passwordInput = view.findViewById(R.id.password);
        loginButton = view.findViewById(R.id.loginButton);
        goToRegisterView = view.findViewById(R.id.createid);
        helpline = view.findViewById(R.id.helpid);
    }

    private boolean validateInputs() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Valid email is required");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            return false;
        }
        return true;
    }

    private void loginUser(View view) {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();

                        db.collection("users").document(userId).get()
                                .addOnCompleteListener(roleTask -> {
                                    if (roleTask.isSuccessful()) {
                                        DocumentSnapshot document = roleTask.getResult();
                                        if (document.exists()) {
                                            String role = document.getString("role");
                                            Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show();
                                            if ("Admin".equals(role)) {
                                                Navigation.findNavController(view)
                                                        .navigate(R.id.action_loginFragment_to_homeScreenFragment2);
                                            } else {
                                                Navigation.findNavController(view)
                                                        .navigate(R.id.action_loginFragment_to_homeScreenFragment2);
                                            }
                                        } else {
                                            Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(requireContext(), "Failed to fetch user data: " +
                                                roleTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(requireContext(), "Login failed: " +
                                        (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


}