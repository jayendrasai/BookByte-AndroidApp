package com.example.myapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddBookFragment} factory method to
 * create an instance of this fragment.
 */
public class AddBookFragment extends Fragment {
    private EditText bookIdInput, bookTitleInput, bookAuthorInput, bookYearInput, bookGenreInput, bookDescriptionInput;
    private Button submitBookButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public AddBookFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_book, container, false);
        initializeViews(view);

        submitBookButton.setOnClickListener(v -> {
            if (validateInputs()) {
                addBook(view);
            }
        });

        return view;
    }

    private void initializeViews(View view) {
        bookIdInput = view.findViewById(R.id.bookId);
        bookTitleInput = view.findViewById(R.id.bookTitle);
        bookAuthorInput = view.findViewById(R.id.bookAuthor);
        bookYearInput = view.findViewById(R.id.bookYear);
        bookGenreInput = view.findViewById(R.id.bookGenre);
        bookDescriptionInput = view.findViewById(R.id.bookDescription);
        submitBookButton = view.findViewById(R.id.submitBookButton);
    }

    private boolean validateInputs() {
        String id = bookIdInput.getText().toString().trim();
        String title = bookTitleInput.getText().toString().trim();
        String author = bookAuthorInput.getText().toString().trim();
        String year = bookYearInput.getText().toString().trim();
        String genre = bookGenreInput.getText().toString().trim();
        String description = bookDescriptionInput.getText().toString().trim();

        if (id.isEmpty()) {
            bookIdInput.setError("Book ID is required");
            return false;
        }
        if (title.isEmpty()) {
            bookTitleInput.setError("Title is required");
            return false;
        }
        if (author.isEmpty()) {
            bookAuthorInput.setError("Author is required");
            return false;
        }
        if (year.isEmpty() || Integer.parseInt(year) < 0) {
            bookYearInput.setError("Valid year is required");
            return false;
        }
        if (genre.isEmpty()) {
            bookGenreInput.setError("Genre is required");
            return false;
        }
        if (description.isEmpty()) {
            bookDescriptionInput.setError("Description is required");
            return false;
        }
        return true;
    }

    private void addBook(View view) {
        int id = Integer.parseInt(bookIdInput.getText().toString());
        String title = bookTitleInput.getText().toString().trim();
        String author = bookAuthorInput.getText().toString().trim();
        int year = Integer.parseInt(bookYearInput.getText().toString().trim());
        String genre = bookGenreInput.getText().toString().trim();
        String description = bookDescriptionInput.getText().toString().trim();

        ModelBook book = new ModelBook(id, title, author, year, genre, description);

        db.collection("books").document(String.valueOf(id))
                .set(book)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(requireContext(), "Book added successfully", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.action_addBookFragment_to_homeScreenFragment2);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to add book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}