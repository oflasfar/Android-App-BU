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

        // 1. On ajoute les liens vers les nouveaux TextViews du Layout
        TextView dateText = view.findViewById(R.id.text_view_detail_date);
        TextView authorText = view.findViewById(R.id.text_view_detail_author);

        Button deleteButton = view.findViewById(R.id.button_delete_book);

        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        bookViewModel.getSelectedBook().observe(getViewLifecycleOwner(), book -> {
            if (book != null) {
                // Titre
                titleText.setText(book.getTitle());

                // 2. Ajout de la date de publication
                if (book.getPublicationYear() != null) {
                    dateText.setText("Publié en : " + book.getPublicationYear());
                } else {
                    dateText.setText("Date inconnue");
                }

                // 3. Ajout de l'auteur (vérifie le nom de tes méthodes dans Book.java)
                if (book.getAuthor() != null) {
                    // Si tu as un objet Author imbriqué :
                    authorText.setText("Auteur : " + book.getAuthor().getFirstname() + " " + book.getAuthor().getLastname());

                    // Si ton modèle a directement une méthode getName() :
                    // authorText.setText("Auteur : " + book.getAuthor().getName());
                } else {
                    authorText.setText("Auteur inconnu");
                }

                // Tags
                if (book.getTags() != null && !book.getTags().isEmpty()) {
                    StringBuilder tagNames = new StringBuilder();
                    for (int i = 0; i < book.getTags().size(); i++) {
                        tagNames.append(book.getTags().get(i).getName());
                        if (i < book.getTags().size() - 1) {
                            tagNames.append(", ");
                        }
                    }
                    tagsText.setText("Tags : " + tagNames.toString());
                } else {
                    tagsText.setText("Aucun tag");
                }

                // Bouton de suppression
                deleteButton.setOnClickListener(v -> {
                    bookViewModel.deleteBook(book);
                    Navigation.findNavController(view).popBackStack();
                });
            }
        });
    }
}