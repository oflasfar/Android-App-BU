package com.example.p42_abc.features.book.model;

import com.example.p42_abc.features.author.model.Author;

import java.util.List;

public class Book {

    private Integer id;
    private String title;
    private String description;
    private List<Tag> tags;
    private Integer publication_year;
    private Integer authorId;
    private Author author;

    public Book() {} // Constructeur vide

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public Integer getPublicationYear() { return publication_year; }

    public void setPublicationYear(Integer publication_year) { this.publication_year = publication_year; }

    public List<Tag> getTags() { return tags; }

    public Author getAuthor() { return author; }

    public void setAuthor(Author author) { this.author = author; }
}
