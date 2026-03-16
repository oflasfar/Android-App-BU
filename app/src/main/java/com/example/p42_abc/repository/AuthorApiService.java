package com.example.p42_abc.repository;


import com.example.p42_abc.author.model.Author;
import com.example.p42_abc.author.model.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthorApiService {
    @GET("authors")
    Call<List<Author>> getAllAuthors();
    @DELETE("authors/{id}")
    Call<Void> deleteAuthor(@Path("id") int authorId);

    @POST("authors")
    Call<Author> createAuthor(@Body Author author);

    @GET("authors/{author_id}/books")
    Call<List<Book>> getBooksOfAuthor(@Path("author_id") int authorId);
}