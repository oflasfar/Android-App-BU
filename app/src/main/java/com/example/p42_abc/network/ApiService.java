package com.example.p42_abc.network;


import com.example.p42_abc.features.author.model.Author;
import com.example.p42_abc.features.book.model.Book;
import com.example.p42_abc.features.book.model.Comment;
import com.example.p42_abc.features.book.model.Tag;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    //Routes pour les auteurs
    @GET("authors")
    Call<List<Author>> getAllAuthors();
    @DELETE("authors/{id}")
    Call<Void> deleteAuthor(@Path("id") int authorId);

    @POST("authors")
    Call<Author> createAuthor(@Body Author author);

    @GET("authors/{author_id}/books")
    Call<List<Book>> getBooksOfAuthor(@Path("author_id") int authorId);

    //Routes pour les livres
    @GET("books?include=author")
    Call<List<Book>> getAllBooks();

    @DELETE("books/{id}")
    Call<Void> deleteBook(@Path("id") int bookId);

    @POST("authors/{author_id}/books")
    Call<Book> createBook(@Path("author_id") int authorId, @Body Book book);

    @PATCH("books/{id}")
    Call<Book> updateBook(@Path("id") int bookId, @Body HashMap<String, Object> body);

    @GET("tags")
    Call<List<Tag>> getAllTags();

    @GET("books/{bookId}/comments")
    Call<List<Comment>> getCommentOfBook(@Path("bookId") int bookId);

    @POST("books/{book_id}/tags/{tag_id}")
    Call<Void> addTagToBook(@Path("book_id") int bookId, @Path("tag_id") int tagId);

    @GET("books/{book_id}/ratings/average")
    Call<Double> getAverageRating(@Path("book_id") int bookId);
}