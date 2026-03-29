package com.example.p42_abc.features.book.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.p42_abc.R;
import com.example.p42_abc.features.book.viewmodel.BookViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class EditBookFragment extends Fragment {

    private BookViewModel bookViewModel;
    private int currentBookId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // On récupère le ViewModel
        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        // On récupère les views
        TextInputEditText titleEdit = view.findViewById(R.id.edit_text_title);
        TextInputEditText yearEdit = view.findViewById(R.id.edit_text_year);
        Button saveButton = view.findViewById(R.id.button_save_book);

        saveButton.setText("Enregistrer les modifications");

        //On observe le livre selectionné
        bookViewModel.getSelectedBook().observe(getViewLifecycleOwner(), book -> {
            if (book != null) {
                currentBookId = book.getId();
                titleEdit.setText(book.getTitle());
                if (book.getPublicationYear() != null) {
                    yearEdit.setText(String.valueOf(book.getPublicationYear()));
                }
            }
        });

        //Boutton pour enregistrer les modifications
        saveButton.setOnClickListener(v -> {
            String newTitle = titleEdit.getText().toString().trim();
            String yearStr = yearEdit.getText().toString().trim();

            if (newTitle.isEmpty()) {
                Toast.makeText(requireContext(), "Le titre est obligatoire", Toast.LENGTH_SHORT).show();
                return;
            }

            Integer newYear = null;
            if (!yearStr.isEmpty()) {
                newYear = Integer.parseInt(yearStr);
            }

            bookViewModel.updateBook(currentBookId, newTitle, newYear);
            Navigation.findNavController(view).popBackStack();
        });
    }
}