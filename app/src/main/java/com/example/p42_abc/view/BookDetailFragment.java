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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.R;
import com.example.p42_abc.adapters.CommentAdapter;
import com.example.p42_abc.author.viewModel.AuthorSharedViewModel;
import com.example.p42_abc.models.Book;
import com.example.p42_abc.viewModels.BookViewModel;

public class BookDetailFragment extends Fragment {
    private BookViewModel bookViewModel;
    private AuthorSharedViewModel authorViewModel;


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

        TextView dateText = view.findViewById(R.id.text_view_detail_date);
        TextView authorText = view.findViewById(R.id.text_view_detail_author);

        TextView ratingText = view.findViewById(R.id.text_view_detail_rating);

        Button deleteButton = view.findViewById(R.id.button_delete_book);

        RecyclerView recyclerComments = view.findViewById(R.id.recycler_view_comments);
        recyclerComments.setLayoutManager(new LinearLayoutManager(requireContext()));
        CommentAdapter commentAdapter = new CommentAdapter();
        recyclerComments.setAdapter(commentAdapter);

        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        bookViewModel.getSelectedBook().observe(getViewLifecycleOwner(), book -> {
            if (book != null) {
                // Titre
                titleText.setText(book.getTitle());

                //Ajout de la date de publication
                if (book.getPublicationYear() != null) {
                    dateText.setText("Publié en : " + book.getPublicationYear());
                } else {
                    dateText.setText("Date inconnue");
                }

                //Ajout de l'auteur (vérifie le nom de tes méthodes dans Book.java)
                if (book.getAuthor() != null) {
                    authorText.setText("Auteur : " + book.getAuthor().getFirstname() + " " + book.getAuthor().getLastname());

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

                bookViewModel.loadAverage(book.getId());
                bookViewModel.loadCommentsForBook(book.getId());

                // Bouton de suppression
                deleteButton.setOnClickListener(v -> {
                    bookViewModel.deleteBook(book);
                    //bookViewModel.refreshBooks();
//                    authorViewModel.refreshBookOfAuthor(book.getAuthor().getId());
                    Navigation.findNavController(view).popBackStack();
                });
            }
        });

        bookViewModel.getBookComments().observe(getViewLifecycleOwner(), comments -> {
            if (comments != null) {
                commentAdapter.setComments(comments);
            }
        });

        // Observer pour la note moyenne
        bookViewModel.getCurrentAverage().observe(getViewLifecycleOwner(), average -> {
            if (average != null && average > 0) {
                // Affiche la note avec une décimale
                ratingText.setText(String.format("Note : %.1f / 5", average));
            } else {
                ratingText.setText("Note : Aucune note");
            }
        });

    }
}