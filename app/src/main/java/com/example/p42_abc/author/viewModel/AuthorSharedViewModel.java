package com.example.p42_abc.author.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p42_abc.author.model.Author;

import java.util.List;

public class AuthorSharedViewModel extends ViewModel {

    private MutableLiveData<List<Author>> authorsLiveData;

    public LiveData<List<Author>> getAuthors() {
        if (authorsLiveData == null) {
            authorsLiveData = new MutableLiveData<>();
            authorsLiveData.setValue(Author.getFakeAuthors());
        }
        return authorsLiveData;
    }

    private final MutableLiveData<Author> selected = new MutableLiveData<Author>();

    public LiveData<Author> getSelected() {
        return selected;
    }

    public void select(Author author) {
        selected.setValue(author);
    }

    public void addAuthor(String name) {
        // 1. On récupère la liste actuelle du LiveData
        List<Author> currentAuthors = authorsLiveData.getValue();

        if (currentAuthors != null) {
            // 2. On crée le nouvel auteur (on lui donne un ID simple basé sur la taille de la liste)
            Author newAuthor = new Author(currentAuthors.size() + 1, name);

            // 3. On l'ajoute à la liste
            currentAuthors.add(newAuthor);

            // 4. On "pousse" la mise à jour pour que les Fragments soient prévenus
            authorsLiveData.setValue(currentAuthors);
        }
    }

    public void deleteAuthor(int id) {
        // 1. On récupère la liste qui est actuellement dans le LiveData
        List<Author> currentAuthors = authorsLiveData.getValue();

        if (currentAuthors != null) {
            // 2. On cherche l'auteur qui a cet ID et on le vire de la liste
            currentAuthors.removeIf(author -> author.getId() == id);


            authorsLiveData.setValue(currentAuthors);
        }
    }
}
