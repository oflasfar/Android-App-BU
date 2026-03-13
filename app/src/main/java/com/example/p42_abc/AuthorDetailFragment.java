package com.example.p42_abc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AuthorDetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_author_detail, container, false);

        TextView textName = view.findViewById(R.id.textViewDetailAuthorName);
        Button btnDelete = view.findViewById(R.id.buttonDeleteAuthor);

        // Pour l'instant, on met un texte temporaire pour vérifier que l'écran s'ouvre bien
        textName.setText("Nom de l'auteur ici");

        btnDelete.setOnClickListener(v -> {
            // On codera l'appel à l'API pour supprimer l'auteur ici plus tard
        });

        return view;
    }
}