package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        if (navHostFragment == null) {
            throw new IllegalStateException("NavHostFragment not found");
        }

        NavController navController = navHostFragment.getNavController();

        // Handle navigation based on Intent extra
        Intent intent = getIntent();
        String destination = intent.getStringExtra("destination");

        if ("homeScreenFragment2".equals(destination)) {
            navController.navigate(R.id.homeScreenFragment2);
        } else {
            navController.navigate(R.id.loginFragment);
        }

    }
}