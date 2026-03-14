package com.example.p42_abc.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p42_abc.models.Book;

import java.util.List;

public class BookViewModel extends ViewModel {

    // ! list de book
    private final MutableLiveData<List<Book>> booksData = new MutableLiveData<>();

    public BookViewModel(){
        loadBooks();
    }

    public LiveData<List<Book>> getBooks(){
        return booksData;
    }

    private void loadBooks(){
        List<Book> list = Book.getBooks();
        booksData.setValue(list);
    }

    public void addBook(Book book){
       List<Book> list = booksData.getValue();
       if(list != null){
           list.add(book);
           // refresh la list de liveData
           booksData.setValue(list);
       }
    }
}
