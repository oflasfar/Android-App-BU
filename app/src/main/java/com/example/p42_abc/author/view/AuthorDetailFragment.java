package com.example.p42_abc.author.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.p42_abc.models.Book;
import com.example.p42_abc.R;
import com.example.p42_abc.author.model.Author;
import com.example.p42_abc.author.viewModel.AuthorSharedViewModel;

public class AuthorDetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_author_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textName = view.findViewById(R.id.textViewDetailAuthorName);
        Button btnDelete = view.findViewById(R.id.buttonDeleteAuthor);

        // On utilise bien requireActivity() pour récupérer le meme ViewModel que la liste
        AuthorSharedViewModel model = new ViewModelProvider(requireActivity()).get(AuthorSharedViewModel.class);

        // On regarde ce qu'il y a dans "getSelected()"
        model.getSelected().observe(getViewLifecycleOwner(), author -> {
            if (author != null) {
                // Dès qu'on trouve l'auteur, on met son nom dans le TextView
                textName.setText(author.getName());
            }
        });

        btnDelete.setOnClickListener(v -> {
            //On récupère l'auteur actuellement sélectionné dans le ViewModel
            Author currentAuthor = model.getSelected().getValue();

            if (currentAuthor != null) {
                //On demande au ViewModel de supprimer l'auteur (et ses livres)
                model.deleteAuthor(currentAuthor.getId());

                //On ferme le fragment de détail pour revenir à la liste
                // C'est l'équivalent du bouton "Retour"

                getParentFragmentManager().popBackStack();

                // Petit message pour confirmer à l'utilisateur
                Toast.makeText(getContext(), "Auteur supprimé", Toast.LENGTH_SHORT).show();
            }
        });


        TextView textBooksList = view.findViewById(R.id.textViewBooksList);

        model.getAuthorBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null && !books.isEmpty()) {
                // S'il a des livres, on crée un texte avec tous les titres
                StringBuilder liste = new StringBuilder("Livres de l'auteur :\n\n");
                for (Book livre : books) {
                    liste.append("- ").append(livre.getTitle())
                            .append(" (").append(livre.getPublicationYear()).append(")\n");
                }
                textBooksList.setText(liste.toString());
            } else {
                // S'il n'en a pas
                textBooksList.setText("Cet auteur n'a aucun livre enregistré.");
            }
        });



        Button btnBack = view.findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(v -> {
            // Cette commande simule lappui sur la touche "Retour" du telephone c juste le depilementd e la île
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }
}