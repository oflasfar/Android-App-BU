package com.example.p42_abc.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p42_abc.model.Author;

import java.util.List;

public class AuthorViewModel extends ViewModel {

    // MutableLiveData est la version modifiable du LiveData
    private MutableLiveData<List<Author>> authorsLiveData;

    public LiveData<List<Author>> getAuthors() {
        if (authorsLiveData == null) {
            authorsLiveData = new MutableLiveData<>();
            // Pour l'instant, on charge nos fausses données.
            // Plus tard, c'est ici qu'on appellera Retrofit pour l'API !
            authorsLiveData.setValue(Author.getFakeAuthors());
        }
        return authorsLiveData;
    }
}
