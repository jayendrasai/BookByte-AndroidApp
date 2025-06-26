package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

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

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            if (currentUser != null) {
                String userId = currentUser.getUid();
                db.collection("users").document(userId).get()
                        .addOnCompleteListener(roleTask -> {
                            if (roleTask.isSuccessful()) {
                                DocumentSnapshot document = roleTask.getResult();
                                if (document.exists()) {
                                    intent.putExtra("destination", "homeScreenFragment2");
                                } else {
                                    intent.putExtra("destination", "loginFragment");
                                }
                            } else {
                                intent.putExtra("destination", "loginFragment");
                            }
                            startActivity(intent);
                            finish();
                        });
            } else {
                intent.putExtra("destination", "loginFragment");
                startActivity(intent);
                finish();
            }
        }, 2000);

    }

}