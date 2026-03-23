package com.example.p42_abc.features.author.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.p42_abc.R;
import com.example.p42_abc.features.author.viewmodel.AuthorViewModel;

public class AddAuthorFragment extends Fragment {

    private AuthorViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_author, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // On récupère le même ViewModel partagé que l'activité
        viewModel = new ViewModelProvider(requireActivity()).get(AuthorViewModel.class);

        EditText editFirstName = view.findViewById(R.id.editTextAuthorFirstName);
        EditText editLastName = view.findViewById(R.id.editTextAuthorLastName);
        Button btnSave = view.findViewById(R.id.buttonSaveAuthor);

        btnSave.setOnClickListener(v -> {
            String firstName = editFirstName.getText().toString().trim();
            String lastName = editLastName.getText().toString().trim();

            if (!lastName.isEmpty()) {
                viewModel.addAuthor(firstName, lastName);
                Toast.makeText(requireContext(), "Auteur ajouté", Toast.LENGTH_SHORT).show();
                // On revient à la liste en utilisant NavController
                Navigation.findNavController(view).popBackStack();
            } else {
                // Si le nom est vide, on affiche une petite erreur rouge sur le champ pour l'aider
                editLastName.setError("Le nom est obligatoire");
            }
        });
    }
}