package com.example.p42_abc.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.p42_abc.models.Book;
import com.example.p42_abc.models.Tag;
import com.example.p42_abc.repository.DataRepository;
import java.util.List;

public class BookViewModel extends ViewModel {

    private final DataRepository repository = DataRepository.getInstance();

    private final MutableLiveData<List<Book>> allBooks = new MutableLiveData<>();
    private final MutableLiveData<Book> selectedBookData = new MutableLiveData<>();

    public BookViewModel() {
        repository.fetchAllBooks(allBooks);
    }

    public LiveData<List<Book>> getBooks() {
        return allBooks;
    }

    public void refreshBooks() {
        repository.fetchAllBooks(allBooks);
    }

    public void deleteBook(Book bookToRemove) {
        if (bookToRemove.getId() != null) {
            repository.deleteBook(bookToRemove.getId(), allBooks);
        }
    }

    public void selectBook(Book book) {
        selectedBookData.setValue(book);
    }

    public LiveData<Book> getSelectedBook() {
        return selectedBookData;
    }

    public void addBook(int authorId, Book book) {
        repository.createBook(authorId, book, allBooks);
    }
    private final MutableLiveData<List<Tag>> allTags = new MutableLiveData<>();

    public LiveData<List<Tag>> getTags() {
        return allTags;
    }

    public void loadTags() {
        repository.fetchAllTags(allTags);
    }
}