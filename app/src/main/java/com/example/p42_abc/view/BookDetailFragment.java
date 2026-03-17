package com.example.p42_abc.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.p42_abc.R;
import com.example.p42_abc.viewModels.BookViewModel;

public class BookDetailFragment extends Fragment {
    private BookViewModel bookViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView titleText = view.findViewById(R.id.text_view_detail_title);
        TextView tagsText = view.findViewById(R.id.text_view_detail_tags);
        Button deleteButton = view.findViewById(R.id.button_delete_book);

        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        bookViewModel.getSelectedBook().observe(getViewLifecycleOwner(), book -> {
            if (book != null) {
                titleText.setText(book.getTitle());

                if (book.getTags() != null && !book.getTags().isEmpty()) {
                    // On extrait juste les noms de chaque objet Tag
                    StringBuilder tagNames = new StringBuilder();
                    for (int i = 0; i < book.getTags().size(); i++) {
                        tagNames.append(book.getTags().get(i).getName());
                        // On ajoute une virgule s'il y a un autre tag après
                        if (i < book.getTags().size() - 1) {
                            tagNames.append(", ");
                        }
                    }
                    tagsText.setText(tagNames.toString());
                } else {
                    tagsText.setText("Aucun tag");
                }

                // bouton de suppression
                deleteButton.setOnClickListener(v -> {
                    bookViewModel.deleteBook(book); // On supprime
                    Navigation.findNavController(view).popBackStack(); // on retourne à la liste
                });
            }
        });
        view.findViewById(R.id.button_back).setOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });
    }
}
