package com.example.myapp;

import java.io.Serializable;

public class ModelBook implements Serializable {
    private int id;
    private String title;
    private String author;
    private int year;
    private String genre;
    private String description;

    public ModelBook() {} // Required for Firestore

    public ModelBook(int id, String title, String author, int year, String genre, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.genre = genre;
        this.description = description;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public String getGenre() { return genre; }
    public String getDescription() { return description; }
}