package com.example.p42_abc.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddBookFragment extends Fragment {

    private BookViewModel bookViewModel;
    private AuthorSharedViewModel authorSharedViewModel;
    private Integer selectedAuthorId = null;

    private int indexImageActuelle = 0;
    private final String[] nomDesImages = {"cover_1", "cover_2", "cover_3", "cover_4"};
    private final int[] idDesImages = {R.drawable.cover_1, R.drawable.cover_2, R.drawable.cover_3, R.drawable.cover_4};

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

        // On récupère le conteneur des Chips
        ChipGroup chipGroupTags = view.findViewById(R.id.chip_group_tags);

        ImageView imageSelector = view.findViewById(R.id.image_cover_selector);
        imageSelector.setOnClickListener(v -> {
            indexImageActuelle = (indexImageActuelle + 1) % idDesImages.length;
            imageSelector.setImageResource(idDesImages[indexImageActuelle]);
        });

        bookViewModel.loadTags();
        bookViewModel.getTags().observe(getViewLifecycleOwner(), tags -> {
            if (tags != null && !tags.isEmpty()) {
                chipGroupTags.removeAllViews(); // On nettoie avant d'afficher
                for (Tag t : tags) {
                    Chip chip = new Chip(requireContext());
                    chip.setText(t.getName());
                    chip.setCheckable(true);
                    chip.setTag(t); // On cache l'objet Tag dans la puce
                    chipGroupTags.addView(chip);
                }
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

        saveButton.setOnClickListener(v -> {
            String title = titleEdit.getText() != null ? titleEdit.getText().toString().trim() : "";
            String yearStr = yearEdit.getText() != null ? yearEdit.getText().toString().trim() : "";

            if (title.isEmpty() || selectedAuthorId == null) {
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

            List<Tag> bookTags = new ArrayList<>();
            for (int i = 0; i < chipGroupTags.getChildCount(); i++) {
                Chip chip = (Chip) chipGroupTags.getChildAt(i);
                if (chip.isChecked()) {
                    Tag selectedTag = (Tag) chip.getTag();
                    Tag lightTag = new Tag();
                    lightTag.setId(selectedTag.getId());
                    lightTag.setName(selectedTag.getName());
                    bookTags.add(lightTag);
                }
            }

            SharedPreferences carnetSecret = requireContext().getSharedPreferences("MesCouvertures", Context.MODE_PRIVATE);
            carnetSecret.edit().putString(title, nomDesImages[indexImageActuelle]).apply();
            bookViewModel.addBook(selectedAuthorId, newBook, bookTags);

            Navigation.findNavController(view).popBackStack();
        });
    }
}