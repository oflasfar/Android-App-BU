package com.example.p42_abc.views;

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

import com.example.p42_abc.R;
import com.example.p42_abc.models.Book;
import com.example.p42_abc.viewmodels.BookViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AddBookFragment extends Fragment {

    private BookViewModel bookViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // on récup le meme viewModel que la list de livre donc pas de this
        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        TextInputEditText titleEdit = view.findViewById(R.id.edit_text_title);
        TextInputEditText descEdit = view.findViewById(R.id.edit_text_description);
        TextInputEditText tagsEdit = view.findViewById(R.id.edit_text_tags);
        Button saveButton = view.findViewById(R.id.button_save_book);

        saveButton.setOnClickListener(v ->{
            String title = titleEdit.getText() != null ? titleEdit.getText().toString() : "";
            String desc = descEdit.getText() != null ? descEdit.getText().toString() : "";
            String tagsString = tagsEdit.getText() != null ? tagsEdit.getText().toString() : "";
            // forcer à remplir tout les champs
            if(title.isEmpty() || desc.isEmpty() || tagsString.isEmpty()){
                Toast.makeText(requireContext(), "Veuillez remplir tout les champs", Toast.LENGTH_SHORT).show();
                return;
            }
            List<String> tags = Arrays.asList(tagsString.split("\\s*,\\s*"));

            Book newBook = new Book(UUID.randomUUID().toString(), title, desc, tags);

            bookViewModel.addBook(newBook);

            getParentFragmentManager().popBackStack(); // on retourne au frag parent car on à fini
        });
    }
}
