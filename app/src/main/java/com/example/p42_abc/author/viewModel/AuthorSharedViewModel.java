package com.example.p42_abc.author.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p42_abc.author.model.Author;
import com.example.p42_abc.author.model.Book;
import com.example.p42_abc.repository.DataRepository;

import java.util.List;

public class AuthorSharedViewModel extends ViewModel {

    private final DataRepository repository = DataRepository.getInstance();
    private final MutableLiveData<Author> selected = new MutableLiveData<>();

    public LiveData<List<Author>> getAuthors() {
        // On dit au repo de lancer la requête
        repository.fetchAllAuthors();
        // On renvoie directement le LiveData du repo
        return repository.getAllAuthorsLiveData();
    }

    public LiveData<Author> getSelected() { return selected; }
    public void select(Author author) {
        selected.setValue(author);
        if (author != null) {
            repository.fetchBooksForAuthor(author.getId());
        }
    }

    public void deleteAuthor(int id) {
        // Le ViewModel donne l'ordre, le repo gère le reste
        repository.deleteAuthor(id);
    }

    // On demande maintenant le prénom ET le nom
    public void addAuthor(String firstName, String lastName) {
        Author newAuthor = new Author();
        newAuthor.setFirstname(firstName);
        newAuthor.setLastname(lastName);
        repository.createAuthor(newAuthor);
    }

    public LiveData<List<Book>> getAuthorBooks() {
        return repository.getAuthorBooksLiveData();
    }


}