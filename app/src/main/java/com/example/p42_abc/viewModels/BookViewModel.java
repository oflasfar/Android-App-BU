package com.example.p42_abc.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p42_abc.models.Book;
import com.example.p42_abc.repository.DataRepository;

import java.util.List;

public class BookViewModel extends ViewModel {

    private final DataRepository repository = DataRepository.getInstance();
    private final MutableLiveData<Book> selectedBookData = new MutableLiveData<>();

    public BookViewModel() {
        // Au démarrage, on demande au serveur de charger les livres
        repository.fetchAllBooks();
    }

    public LiveData<List<Book>> getBooks() {
        // On renvoie le tuyau du serveur
        return repository.getAllBooksLiveData();
    }

    public void deleteBook(Book bookToRemove) {
        if (bookToRemove.getId() != null) {
            repository.deleteBook(bookToRemove.getId());
        }
    }

    public void selectBook(Book book) {
        selectedBookData.setValue(book);
    }

    public LiveData<Book> getSelectedBook() {
        return selectedBookData;
    }

    public void addBook(int authorId, Book book) {
        repository.createBook(authorId, book);
    }
}
