package com.example.p42_abc.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.p42_abc.R;
import com.example.p42_abc.author.model.Author;
import com.example.p42_abc.author.viewModel.AuthorSharedViewModel;
import com.example.p42_abc.models.Book;
import com.example.p42_abc.models.Tag;
import com.example.p42_abc.viewModels.BookViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddBookFragment extends Fragment {

    private BookViewModel bookViewModel;
    private AuthorSharedViewModel authorSharedViewModel;
    private Integer selectedAuthorId = null;

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
        TextInputEditText yearEdit = view.findViewById(R.id.edit_text_year);
        Button saveButton = view.findViewById(R.id.button_save_book);
        AutoCompleteTextView authorAutoComplete = view.findViewById(R.id.auto_complete_txt_author);
        ///
        MultiAutoCompleteTextView tagsEdit = view.findViewById(R.id.edit_text_tags);

        bookViewModel.loadTags();
        bookViewModel.getTags().observe(getViewLifecycleOwner(), tags -> {
            if (tags != null && !tags.isEmpty()) {
                List<String> tagNames = new ArrayList<>();
                for (Tag t : tags) {
                    tagNames.add(t.getName());
                }

                ArrayAdapter<String> tagsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, tagNames);
                tagsEdit.setAdapter(tagsAdapter);
                tagsEdit.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            }
        });

        authorSharedViewModel.getAuthors().observe(getViewLifecycleOwner(), authors -> {
            if (authors != null && !authors.isEmpty()) {
                List<String> authorNames = new ArrayList<>();
                for (Author a : authors) {
                    authorNames.add(a.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, authorNames);
                authorAutoComplete.setAdapter(adapter);

                authorAutoComplete.setOnItemClickListener((parent, view1, position, id) -> {
                    selectedAuthorId = authors.get(position).getId();
                });
            }
        });

        saveButton.setOnClickListener(v ->{

            String tagsStr = tagsEdit.getText() != null ? tagsEdit.getText().toString().trim() : "";
            String title = titleEdit.getText() != null ? titleEdit.getText().toString().trim() : "";
            String yearStr = yearEdit.getText() != null ? yearEdit.getText().toString().trim() : "";

            if(title.isEmpty() || selectedAuthorId == null){
                Toast.makeText(requireContext(), "Veuillez remplir le titre et sélectionner un auteur", Toast.LENGTH_SHORT).show();
                return;
            }

            Integer pubYear = null;
            if (!yearStr.isEmpty()) {
                try {
                    pubYear = Integer.parseInt(yearStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "L'année doit être un nombre valide", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Book newBook = new Book();
            newBook.setTitle(title);
            newBook.setPublicationYear(pubYear);
            if (!tagsStr.isEmpty()) {
                String[] tagsArray = tagsStr.split(",");
                List<Tag> bookTags = new ArrayList<>();
                for (String t : tagsArray) {
                    String tagName = t.trim();
                    if (!tagName.isEmpty()) {
                        Tag tag = new Tag();
                        tag.setName(tagName);
                        bookTags.add(tag);
                    }
                }
                newBook.setTags(bookTags);
            }
            bookViewModel.addBook(selectedAuthorId, newBook);

            Navigation.findNavController(view).popBackStack();
        });
    }
}