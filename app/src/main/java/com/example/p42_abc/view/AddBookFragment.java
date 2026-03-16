package com.example.p42_abc.view;

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
import com.example.p42_abc.author.model.Author;
import com.example.p42_abc.author.viewModel.AuthorSharedViewModel;
import com.example.p42_abc.models.Book;
import com.example.p42_abc.models.Tag;
import com.example.p42_abc.viewModels.BookViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AddBookFragment extends Fragment {

    private BookViewModel bookViewModel;
    private AuthorSharedViewModel authorSharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
        this.authorSharedViewModel = new ViewModelProvider(requireActivity()).get(AuthorSharedViewModel.class);

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
                Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }
            Author currentAuthor = authorSharedViewModel.getSelected().getValue();

            if (currentAuthor == null || currentAuthor.getId() <= 0) {
                Toast.makeText(requireContext(), "Erreur : Auteur introuvable", Toast.LENGTH_SHORT).show();
                return;
            }

            List<String> stringTags = Arrays.asList(tagsString.split("\\s*,\\s*"));
            List<Tag> tags = new ArrayList<>();

            for (String tagName : stringTags) {
                Tag newTag = new Tag();
                newTag.setName(tagName.trim()); // trim() est super pour enlever les espaces inutiles que l alexis aurait tapés
                tags.add(newTag);
            }

            Book newBook = new Book();
            newBook.setTitle(title);
            newBook.setDescription(desc);
            newBook.setTags(tags);

            bookViewModel.addBook(currentAuthor.getId(), newBook);

            getParentFragmentManager().popBackStack();
        });
    }
}