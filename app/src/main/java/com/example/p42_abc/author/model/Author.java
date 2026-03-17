package com.example.p42_abc.author.model;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private Integer id;
    private String firstname;
    private String lastname;

    // Constructeur vide (Obligatoire pour Retrofit)
    public Author() {}

    public Author(int id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public int getId() { return id; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }

    public String getName() {
        return (firstname != null ? firstname : "") + " " + (lastname != null ? lastname : "");
    }

    public void setId(int id) { this.id = id; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

}