package com.example.p42_abc.author.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p42_abc.author.model.Author;
import com.example.p42_abc.repository.DataRepository;
import com.example.p42_abc.models.Book;
import java.util.List;

public class AuthorSharedViewModel extends ViewModel {

    private final DataRepository repository = DataRepository.getInstance();

    private final MutableLiveData<List<Author>> allAuthors = new MutableLiveData<>();
    private final MutableLiveData<List<Book>> authorBooks = new MutableLiveData<>();
    private final MutableLiveData<Author> selected = new MutableLiveData<>();

    public AuthorSharedViewModel() {
        repository.fetchAllAuthors(allAuthors);
    }

    public LiveData<List<Author>> getAuthors() {
        return allAuthors;
    }

    public LiveData<Author> getSelected() {
        return selected;
    }

    public void select(Author author) {
        selected.setValue(author);
        if (author != null) {
            repository.fetchBooksForAuthor(author.getId(), authorBooks);
        }
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