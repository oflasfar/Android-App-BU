package com.example.p42_abc.author.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// Assure-toi que ces imports correspondent bien à tes dossiers (appuie sur Alt+Entrée si c'est rouge)
import com.example.p42_abc.author.adapter.AuthorAdapter;
import com.example.p42_abc.author.viewModel.AuthorSharedViewModel;

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

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewAuthors);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        authorAdapter = new AuthorAdapter();
        recyclerView.setAdapter(authorAdapter);

        // On utilise requireActivity() pour partager les données avec les autres écrans
        AuthorSharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(AuthorSharedViewModel.class);

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
    }
}