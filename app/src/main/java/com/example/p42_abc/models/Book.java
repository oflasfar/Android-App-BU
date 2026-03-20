package com.example.p42_abc.models;

import com.example.p42_abc.author.model.Author;

import java.util.ArrayList;
import java.util.Arrays;
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

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Integer getPublicationYear() { return publication_year; }

    public void setPublicationYear(Integer publication_year) { this.publication_year = publication_year; }

    public Integer getAuthorId() { return authorId; }

    public void setAuthorId(Integer authorId) { this.authorId = authorId; }

    public List<Tag> getTags() { return tags; }

    public void setTags(List<Tag> tags) { this.tags = tags; }

    public Author getAuthor() { return author; }

    public void setAuthor(Author author) { this.author = author; }
}
