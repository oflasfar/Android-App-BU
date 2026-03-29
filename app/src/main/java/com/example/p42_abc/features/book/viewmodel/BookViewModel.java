package com.example.p42_abc.features.book.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.p42_abc.features.book.model.Book;
import com.example.p42_abc.features.book.model.Comment;
import com.example.p42_abc.features.book.model.Tag;
import com.example.p42_abc.repository.DataRepository;
import java.util.List;

public class BookViewModel extends ViewModel {

    //Le repository
    private final DataRepository repository = DataRepository.getInstance();
    //Tous les books
    private final MutableLiveData<List<Book>> allBooks = new MutableLiveData<>();
    //Le book selectionner
    private final MutableLiveData<Book> selectedBookData = new MutableLiveData<>();
    //Les commentaires
    private final MutableLiveData<List<Comment>> allComments =  new MutableLiveData<>();
    //Les tags
    private final MutableLiveData<List<Tag>> allTags = new MutableLiveData<>();

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

    public void addBook(int authorId, Book book, List<Tag> tags) {
        repository.createBook(authorId, book, tags, allBooks);
    }
    public void updateTags(int bookId, List<Tag> tags) {
        repository.updateBookTags(bookId, tags, allBooks);
    }

    public LiveData<List<Tag>> getTags() {
        return allTags;
    }

    public void loadTags() {
        repository.fetchAllTags(allTags);
    }

    public LiveData<List<Comment>> getBookComments() {
        return allComments;
    }

    public void loadCommentsForBook(int bookId) {
        repository.fetchCommentsOfBook(allComments, bookId);
    }

    private final MutableLiveData<Double> currentAverage = new MutableLiveData<>();

    public LiveData<Double> getCurrentAverage() {
        return currentAverage;
    }

    public void loadAverage(int bookId) {
        repository.fetchAverage(bookId, currentAverage);
    }

    public void updateBook(int bookId, String title, Integer publicationYear) {
        repository.updateBook(bookId, title, publicationYear, allBooks);
        Book current = selectedBookData.getValue();
        if (current != null && current.getId() != null && current.getId() == bookId) {
            current.setTitle(title);
            current.setPublicationYear(publicationYear);
            selectedBookData.setValue(current);
        }
    }
}