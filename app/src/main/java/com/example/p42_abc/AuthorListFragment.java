package com.example.p42_abc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// Assure-toi que ces imports correspondent bien à tes dossiers (appuie sur Alt+Entrée si c'est rouge)
import com.example.p42_abc.adapter.AuthorAdapter;
import com.example.p42_abc.viewModel.AuthorSharedViewModel;

public class AuthorListFragment extends Fragment {

    private AuthorAdapter authorAdapter;

    // 1. On charge juste le design XML ici
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_author_list, container, false);
    }

    // 2. On fait toute la logique ici (comme le prof l'a montré)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --- CONFIGURATION DE LA LISTE ---
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewAuthors);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        authorAdapter = new AuthorAdapter();
        recyclerView.setAdapter(authorAdapter);

        // --- CONNEXION AU VIEWMODEL DU PROF ---
        // On utilise requireActivity() pour partager les données avec les autres écrans
        AuthorSharedViewModel model = new ViewModelProvider(requireActivity()).get(AuthorSharedViewModel.class);

        // On observe la liste des auteurs pour remplir l'écran
        model.getAuthors().observe(getViewLifecycleOwner(), authors -> {
            authorAdapter.setAuthors(authors);
        });

        authorAdapter.setOnAuthorClickListener(author -> {
            // On dit au ViewModel "Tiens, l'utilisateur a cliqué sur cet auteur"
            model.select(author);

            // On change de page pour aller vers les détails
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AuthorDetailFragment()) // R.id.fragment_container est dans activity_main.xml
                    .addToBackStack(null) // Permet de faire "Retour" avec le téléphone
                    .commit();
        });

        FloatingActionButton fab = view.findViewById(R.id.fabAddAuthor);
        fab.setOnClickListener(v -> {
            // On codera l'ouverture du formulaire d'ajout ici plus tard
        });
    }
}