package com.example.myapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateAccount#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateAccount extends Fragment {
    private EditText nameInput, emailInput, mobileInput, passwordInput;
    private RadioGroup genderGroup, roleGroup;
    private Button submitButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public CreateAccount() {}

    public static CreateAccount newInstance(String param1, String param2) {
        CreateAccount fragment = new CreateAccount();
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
            getArguments().getString(ARG_PARAM1);
            getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);
        initializeViews(view);
        submitButton.setOnClickListener(v -> {
            if (handleSubmit()) {
                registerUser(view);
            }
        });
        return view;
    }

    private void registerUser(View view) {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String name = nameInput.getText().toString().trim();
        String role = getSelectedRole();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Update user profile
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        user.updateProfile(profileUpdates).addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                // Save user data to Firestore
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("name", name);
                                userData.put("email", email);
                                userData.put("mobile", mobileInput.getText().toString().trim());
                                userData.put("gender", getSelectedGender());
                                userData.put("role", role);

                                db.collection("users").document(user.getUid())
                                        .set(userData)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                                            // Navigate based on role
                                            if (role.equals("Admin")) {
                                                Navigation.findNavController(view)
                                                        .navigate(R.id.action_createAccount_to_homeScreenFragment2);

                                            } else {
                                                Navigation.findNavController(view)
                                                        .navigate(R.id.action_createAccount_to_loginFragment);
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(requireContext(), "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                Toast.makeText(requireContext(), "Failed to update profile: " +
                                        updateTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        String errorMessage = task.getException() instanceof FirebaseAuthUserCollisionException ?
                                "Email is already in use" :
                                task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(requireContext(), "Registration failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initializeViews(View view) {
        nameInput = view.findViewById(R.id.name);
        emailInput = view.findViewById(R.id.email);
        mobileInput = view.findViewById(R.id.mobile);
        passwordInput = view.findViewById(R.id.password);
        genderGroup = view.findViewById(R.id.genderGroup);
        roleGroup = view.findViewById(R.id.roleGroup);
        submitButton = view.findViewById(R.id.submitButton);
    }

    private boolean handleSubmit() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String mobile = mobileInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String gender = getSelectedGender();
        String role = getSelectedRole();
        return validateInputs(name, email, mobile, password, gender, role);
    }

    private String getSelectedGender() {
        int genderId = genderGroup.getCheckedRadioButtonId();
        if (genderId != -1) {
            RadioButton selectedGender = genderGroup.findViewById(genderId);
            return selectedGender.getText().toString();
        }
        return "";
    }

    private String getSelectedRole() {
        int roleId = roleGroup.getCheckedRadioButtonId();
        if (roleId != -1) {
            RadioButton selectedRole = roleGroup.findViewById(roleId);
            return selectedRole.getText().toString();
        }
        return "";
    }

    private boolean validateInputs(String name, String email, String mobile, String password, String gender, String role) {
        if (name.isEmpty()) {
            nameInput.setError("Name is required");
            return false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Valid email is required");
            return false;
        }
        if (mobile.isEmpty() || !mobile.matches("^[0-9]{10}$")) {
            mobileInput.setError("Enter a valid 10-digit mobile number");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            return false;
        }
        if (gender.isEmpty()) {
            Toast.makeText(requireContext(), "Please select a gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (role.isEmpty()) {
            Toast.makeText(requireContext(), "Please select a role", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}