package com.example.p42_abc.author.model;

public class Book {
    private Integer id;
    private String title;
    private Integer publication_year; // Doit être exactement comme dans la base de données

    // Getters pour que Retrofit puisse lire les données
    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public Integer getPublicationYear() { return publication_year; }
}