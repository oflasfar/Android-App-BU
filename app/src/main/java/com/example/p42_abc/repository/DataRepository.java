package com.example.p42_abc.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.p42_abc.author.model.Author;
import com.example.p42_abc.author.model.Book;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataRepository {
    private static DataRepository instance;
    private final AuthorApiService apiService;
    private final MutableLiveData<List<Author>> allAuthorsLiveData = new MutableLiveData<>();
    private DataRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(AuthorApiService.class);
    }
    public static synchronized DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }
    public LiveData<List<Author>> getAllAuthorsLiveData() {
        return allAuthorsLiveData;
    }

    public void fetchAllAuthors() {
        apiService.getAllAuthors().enqueue(new Callback<List<Author>>() {
            @Override
            public void onResponse(Call<List<Author>> call, Response<List<Author>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // On met à jour le LiveData ici
                    allAuthorsLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Author>> call, Throwable t) {
                // Gestion d'erreur locale
            }
        });
    }

    public void deleteAuthor(int id) {
        apiService.deleteAuthor(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Si ça marche, le repo rafraîchit la liste tout seul !
                    fetchAllAuthors();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    public void createAuthor(Author author) {
        apiService.createAuthor(author).enqueue(new Callback<Author>() {
            @Override
            public void onResponse(Call<Author> call, Response<Author> response) {
                if (response.isSuccessful()) {
                    // Idem, on rafraîchit la liste
                    fetchAllAuthors();
                }else {
                    // LE SERVEUR REFUSE : On affiche l'erreur en rouge dans Android Studio
                    Log.e("API_BUG", "Le serveur a refusé ! Code d'erreur : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Author> call, Throwable t) {}
        });
    }


    private final MutableLiveData<List<Book>> authorBooksLiveData = new MutableLiveData<>();

    //Pour que le ViewModel puisse lire le tuyau
    public LiveData<List<Book>> getAuthorBooksLiveData() {
        return authorBooksLiveData;
    }

    //La méthode qui demande les livres au serveur
    public void fetchBooksForAuthor(int authorId) {
        apiService.getBooksOfAuthor(authorId).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Si le serveur répond, on met la liste de livres dans le tuyau
                    authorBooksLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                // En cas d'erreur, on met une liste vide pour ne pas faire planter l'appli
                authorBooksLiveData.setValue(new ArrayList<>());
            }
        });
    }
}
