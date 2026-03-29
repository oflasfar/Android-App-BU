package com.example.p42_abc.features.author.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p42_abc.features.author.model.Author;
import com.example.p42_abc.repository.DataRepository;
import com.example.p42_abc.features.book.model.Book;
import java.util.List;

public class AuthorViewModel extends ViewModel {

    private final DataRepository repository = DataRepository.getInstance();

    //les donnees de tous les auteurs
    private final MutableLiveData<List<Author>> allAuthors = new MutableLiveData<>();
    //les donnees des livres d'un auteur
    private final MutableLiveData<List<Book>> authorBooks = new MutableLiveData<>();
    //l'auteur selectionne
    private final MutableLiveData<Author> selected = new MutableLiveData<>();

    //Constructeur
    public AuthorViewModel() {
        repository.fetchAllAuthors(allAuthors);
    }

    //Getters
    public LiveData<List<Author>> getAuthors() {
        return allAuthors;
    }

    //Getters
    public LiveData<Author> getSelected() {
        return selected;
    }

    public void select(Author author) {
        selected.setValue(author);
        if (author != null) {
            repository.fetchBooksForAuthor(author.getId(), authorBooks);
        }
    }

    public void refreshBookOfAuthor(int authorId) {
        repository.fetchBooksForAuthor(authorId, authorBooks);
    }

    public LiveData<List<Book>> getAuthorBooks() {
        return authorBooks;
    }

    public void deleteAuthor(int id) {
        repository.deleteAuthor(id, allAuthors);
    }

    public void addAuthor(String firstName, String lastName) {
        Author newAuthor = new Author();
        newAuthor.setFirstname(firstName);
        newAuthor.setLastname(lastName);
        repository.createAuthor(newAuthor, allAuthors);
    }
}