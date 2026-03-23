package com.example.p42_abc.repository;

import android.util.Log;

import androidx.annotation.NonNull;
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
        apiService.getAllAuthors().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Author>> call, @NonNull Response<List<Author>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    targetLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Author>> call, @NonNull Throwable t) {
            }
        });
    }

    public void deleteAuthor(int id, MutableLiveData<List<Author>> targetLiveData) {
        apiService.deleteAuthor(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    fetchAllAuthors(targetLiveData);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }

    public void createAuthor(Author author, MutableLiveData<List<Author>> targetLiveData) {
        apiService.createAuthor(author).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Author> call, @NonNull Response<Author> response) {
                if (response.isSuccessful()) {
                    fetchAllAuthors(targetLiveData);
                } else {
                    Log.e("API_BUG", "Erreur : " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Author> call, @NonNull Throwable t) {
            }
        });
    }

    public void fetchBooksForAuthor(int authorId, MutableLiveData<List<Book>> targetLiveData) {
        apiService.getBooksOfAuthor(authorId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Book>> call, @NonNull Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    targetLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                targetLiveData.setValue(new ArrayList<>());
            }
        });
    }

    public void fetchAllBooks(MutableLiveData<List<Book>> targetLiveData) {
        apiService.getAllBooks().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Book>> call, @NonNull Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    targetLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
            }
        });
    }

    public void deleteBook(int id, MutableLiveData<List<Book>> targetLiveData) {
        apiService.deleteBook(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) fetchAllBooks(targetLiveData);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }


    public void createBook(int authorId, Book book, List<Tag> tags, MutableLiveData<List<Book>> targetLiveData) {
        // On envoie le livre (qui ne contient pas de tags dans son objet pour l'instant)
        apiService.createBook(authorId, book).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Book> call, @NonNull Response<Book> response) {
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
            public void onFailure(@NonNull Call<Book> call, @NonNull Throwable t) {
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
            apiService.addTagToBook(bookId, t.getId()).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    count[0]++;
                    if (count[0] == tags.size()) {
                        fetchAllBooks(targetLiveData);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                    count[0]++;
                    if (count[0] == tags.size()) {
                        fetchAllBooks(targetLiveData);
                    }
                }
            });
        }
    }

    public void fetchAllTags(MutableLiveData<List<Tag>> targetLiveData) {
        apiService.getAllTags().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Tag>> call, @NonNull Response<List<Tag>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    targetLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Tag>> call, @NonNull Throwable t) {
            }
        });
    }

    public void fetchCommentsOfBook(MutableLiveData<List<Comment>> targetLiveData, int bookId) {
        apiService.getCommentOfBook(bookId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    targetLiveData.setValue(response.body());
                    Log.d("API_SUCCESS", "Nombre de commentaires reçus : " + response.body().size());
                } else {
                    Log.e("API_BUG", "Erreur : " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Comment>> call, @NonNull Throwable t) {
                //On gere les erreurs apres si on a le temps
                Log.e("API_BUG", "Erreur : " + t.getMessage());
            }
        });
    }

    public void fetchAverage(int bookId, MutableLiveData<Double> target) {
        apiService.getAverageRating(bookId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Double> call, @NonNull Response<Double> response) {
                if (response.isSuccessful() && response.body() != null) {
                    target.postValue(response.body());
                } else {
                    target.postValue(0.0);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Double> call, @NonNull Throwable t) {
                target.postValue(0.0);
            }
        });
    }

    public void updateBook(int bookId, String newTitle, Integer newPublicationYear, MutableLiveData<List<Book>> targetLiveData) {
        java.util.HashMap<String, Object> apiBody = new java.util.HashMap<>();

        if (newTitle != null && !newTitle.isEmpty()) {
            apiBody.put("title", newTitle);
        }
        if (newPublicationYear != null) {
            apiBody.put("publication_year", newPublicationYear);
        }

        apiService.updateBook(bookId, apiBody).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Book> call, @NonNull Response<Book> response) {
                if (response.isSuccessful()) {
                    Log.d("API_SUCCESS", "Livre modifié avec succès !");
                    fetchAllBooks(targetLiveData); // rehcarge la liste
                } else {
                    Log.e("API_BUG", "Erreur modif livre : " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Book> call, @NonNull Throwable t) {
                Log.e("API_BUG", "Crash : " + t.getMessage());
            }
        });
    }

}