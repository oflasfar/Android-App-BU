package com.example.p42_abc.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.p42_abc.author.model.Author;
import com.example.p42_abc.models.Book;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataRepository {
    private static DataRepository instance;
    private final ApiService apiService;

    private DataRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static synchronized DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    public void fetchAllAuthors(MutableLiveData<List<Author>> targetLiveData) {
        apiService.getAllAuthors().enqueue(new Callback<List<Author>>() {
            @Override
            public void onResponse(Call<List<Author>> call, Response<List<Author>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    targetLiveData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Author>> call, Throwable t) {}
        });
    }

    public void deleteAuthor(int id, MutableLiveData<List<Author>> targetLiveData) {
        apiService.deleteAuthor(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    fetchAllAuthors(targetLiveData);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    public void createAuthor(Author author, MutableLiveData<List<Author>> targetLiveData) {
        apiService.createAuthor(author).enqueue(new Callback<Author>() {
            @Override
            public void onResponse(Call<Author> call, Response<Author> response) {
                if (response.isSuccessful()) {
                    fetchAllAuthors(targetLiveData);
                } else {
                    Log.e("API_BUG", "Erreur : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Author> call, Throwable t) {}
        });
    }

    public void fetchBooksForAuthor(int authorId, MutableLiveData<List<Book>> targetLiveData) {
        apiService.getBooksOfAuthor(authorId).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    targetLiveData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                targetLiveData.setValue(new ArrayList<>());
            }
        });
    }

    public void fetchAllBooks(MutableLiveData<List<Book>> targetLiveData) {
        apiService.getAllBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    targetLiveData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {}
        });
    }

    public void deleteBook(int id, MutableLiveData<List<Book>> targetLiveData) {
        apiService.deleteBook(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) fetchAllBooks(targetLiveData);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    public void createBook(int authorId, Book book, MutableLiveData<List<Book>> targetLiveData) {
        apiService.createBook(authorId, book).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    fetchAllBooks(targetLiveData);
                } else {
                    Log.e("API_BUG", "Erreur ajout livre : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e("API_BUG", "Crash réseau : " + t.getMessage());
            }
        });
    }
}