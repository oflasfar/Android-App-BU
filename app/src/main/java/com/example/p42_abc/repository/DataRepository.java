package com.example.p42_abc.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.p42_abc.features.author.model.Author;
import com.example.p42_abc.features.book.model.Book;
import com.example.p42_abc.features.book.model.Comment;
import com.example.p42_abc.features.book.model.Tag;
import com.example.p42_abc.network.ApiService;

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


    public void createBook(int authorId, Book book, List<Tag> tags, MutableLiveData<List<Book>> targetLiveData) {
        // On envoie le livre (qui ne contient pas de tags dans son objet pour l'instant)
        apiService.createBook(authorId, book).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // On récupère l'ID généré par la DB
                    int newBookId = response.body().getId();

                    // Si on a des tags dans les paramètres, on lance l'update
                    if (tags != null && !tags.isEmpty()) {
                        updateBookTags(newBookId, tags, targetLiveData);
                    } else {
                        fetchAllBooks(targetLiveData);
                    }
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

    public void updateBookTags(int bookId, List<Tag> tags, MutableLiveData<List<Book>> targetLiveData) {
        if (tags == null || tags.isEmpty()) {
            fetchAllBooks(targetLiveData);
            return;
        }

        final int[] count = {0};

        for (Tag t : tags) {
            apiService.addTagToBook(bookId, t.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    count[0]++;
                    if (count[0] == tags.size()) {
                        fetchAllBooks(targetLiveData);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    count[0]++;
                    if (count[0] == tags.size()) {
                        fetchAllBooks(targetLiveData);
                    }
                }
            });
        }
    }

    public void fetchAllTags(MutableLiveData<List<Tag>> targetLiveData) {
        apiService.getAllTags().enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    targetLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Tag>> call, Throwable t) {}
        });
    }

    public void fetchCommentsOfBook(MutableLiveData<List<Comment>> targetLiveData, int bookId) {
        apiService.getCommentOfBook(bookId).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    targetLiveData.setValue(response.body());
                    Log.d("API_SUCCESS", "Nombre de commentaires reçus : " + response.body().size());
                }else {
                    Log.e("API_BUG", "Erreur : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                //On gere les erreurs apres si on a le temps
                Log.e("API_BUG", "Erreur : " + t.getMessage());
            }
        });
    }

    public void fetchAverage(int bookId, MutableLiveData<Double> target) {
        apiService.getAverageRating(bookId).enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful() && response.body() != null) {
                    target.postValue(response.body());
                } else {
                    target.postValue(0.0);
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                target.postValue(0.0);
            }
        });
    }

}