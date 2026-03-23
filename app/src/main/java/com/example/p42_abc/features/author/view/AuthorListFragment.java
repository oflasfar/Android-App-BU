package com.example.p42_abc.features.author.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.R;
import com.example.p42_abc.features.author.model.Author;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// Assure-toi que ces imports correspondent bien à tes dossiers (appuie sur Alt+Entrée si c'est rouge)
import com.example.p42_abc.features.author.adapter.AuthorAdapter;
import com.example.p42_abc.features.author.viewmodel.AuthorViewModel;

import java.util.ArrayList;
import java.util.List;

public class AuthorListFragment extends Fragment {

    private AuthorAdapter authorAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_author_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText searchBar = view.findViewById(R.id.edit_text_search_author);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewAuthors);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        authorAdapter = new AuthorAdapter();
        recyclerView.setAdapter(authorAdapter);

        // On utilise requireActivity() pour partager les données avec les autres écrans
        AuthorViewModel viewModel = new ViewModelProvider(requireActivity()).get(AuthorViewModel.class);

        // On observe la liste des auteurs pour remplir l'écran
        viewModel.getAuthors().observe(getViewLifecycleOwner(), authors -> {
            authorAdapter.setAuthors(authors);
        });

        authorAdapter.setOnAuthorClickListener(author -> {
            viewModel.select(author);

            // On change de page pour aller vers les détails en utilisant NavController
            Navigation.findNavController(view).navigate(R.id.action_authorListFragment_to_authorDetailFragment);
        });

        FloatingActionButton fab = view.findViewById(R.id.fabAddAuthor);
        fab.setOnClickListener(v -> {
            // On change de page pour aller vers l'ajout en utilisant NavController
            Navigation.findNavController(view).navigate(R.id.action_authorListFragment_to_addAuthorFragment);
        });

        //Barre de recherche
        // On ajoute un écouteur qui va se déclencher à chaque lettre tapée
        searchBar.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().toLowerCase().trim();

                List<Author> allAuthors = viewModel.getAuthors().getValue();

                if (allAuthors != null) {
                    List<Author> filteredList = new ArrayList<>();

                    // On boucle sur tous les auteurs
                    for (Author author : allAuthors) {
                        String fullName = (author.getFirstname() + " " + author.getLastname()).toLowerCase();

                        if (fullName.contains(query)) {
                            filteredList.add(author);
                        }
                    }

                    authorAdapter.setAuthors(filteredList);
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {

            }
        });
    }
}