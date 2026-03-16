package com.example.p42_abc.author.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.p42_abc.R;
import com.example.p42_abc.author.viewModel.AuthorSharedViewModel;

public class AddAuthorFragment extends Fragment {

    private AuthorSharedViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_author, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // On récupère le même ViewModel partagé que l'activité
        viewModel = new ViewModelProvider(requireActivity()).get(AuthorSharedViewModel.class);

        // 1. NOUVEAU : On récupère tes DEUX champs de texte (les IDs de ton nouveau XML)
        EditText editFirstName = view.findViewById(R.id.editTextAuthorFirstName);
        EditText editLastName = view.findViewById(R.id.editTextAuthorLastName);
        Button btnSave = view.findViewById(R.id.buttonSaveAuthor);

        btnSave.setOnClickListener(v -> {
            // 2. On lit ce que l'utilisateur a tapé dans les deux champs
            String firstName = editFirstName.getText().toString().trim();
            String lastName = editLastName.getText().toString().trim();

            // 3. On vérifie qu'il a au moins mis un nom de famille (le prénom peut être vide si on veut)
            if (!lastName.isEmpty()) {

                // 4. On envoie les deux infos au ViewModel
                viewModel.addAuthor(firstName, lastName);

                // On revient à la liste
                getParentFragmentManager().popBackStack();

            } else {
                // Si le nom est vide, on affiche une petite erreur rouge sur le champ pour l'aider
                editLastName.setError("Le nom est obligatoire");
            }
        });
    }
}