package com.example.p42_abc.features.book.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.R;
import com.example.p42_abc.features.book.adapter.CommentAdapter;
import com.example.p42_abc.features.author.viewmodel.AuthorViewModel;
import com.example.p42_abc.features.book.viewmodel.BookViewModel;

public class BookDetailFragment extends Fragment {
    private BookViewModel bookViewModel;
    private AuthorViewModel authorViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imageCouverture = view.findViewById(R.id.image_couverture);

        TextView titleText = view.findViewById(R.id.text_view_detail_title);
        TextView tagsText = view.findViewById(R.id.text_view_detail_tags);

        TextView dateText = view.findViewById(R.id.text_view_detail_date);
        TextView authorText = view.findViewById(R.id.text_view_detail_author);


        Button deleteButton = view.findViewById(R.id.button_delete_book);
        Button editButton = view.findViewById(R.id.button_edit_book);
        CommentAdapter commentAdapter = new CommentAdapter();

        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        bookViewModel.getSelectedBook().observe(getViewLifecycleOwner(), book -> {
            if (book != null) {

                titleText.setText(book.getTitle());

                SharedPreferences carnetSecret = requireContext().getSharedPreferences("MesCouvertures", Context.MODE_PRIVATE);

                String imageChoisie = carnetSecret.getString(book.getTitle(), null);

                if (imageChoisie != null) {
                    int idImageDrawable = requireContext().getResources().getIdentifier(imageChoisie, "drawable", requireContext().getPackageName());
                    if (idImageDrawable != 0) {
                        imageCouverture.setImageResource(idImageDrawable);
                    } else {
                        imageCouverture.setImageResource(android.R.drawable.ic_menu_gallery);
                    }
                } else {
                    int idImageParDefaut = requireContext().getResources().getIdentifier("cover_" + book.getId(), "drawable", requireContext().getPackageName());

                    if (idImageParDefaut != 0) {
                        imageCouverture.setImageResource(idImageParDefaut);
                    } else {
                        imageCouverture.setImageResource(android.R.drawable.ic_menu_gallery);
                    }
                }

                if (book.getPublicationYear() != null) {
                    dateText.setText("Publié en : " + book.getPublicationYear());
                } else {
                    dateText.setText("Date inconnue");
                }

                if (book.getAuthor() != null) {
                    authorText.setText("Auteur : " + book.getAuthor().getFirstname() + " " + book.getAuthor().getLastname());

                } else {
                    authorText.setText("Auteur inconnu");
                }

                if (book.getTags() != null && !book.getTags().isEmpty()) {
                    StringBuilder tagNames = new StringBuilder();
                    for (int i = 0; i < book.getTags().size(); i++) {
                        tagNames.append(book.getTags().get(i).getName());
                        if (i < book.getTags().size() - 1) {
                            tagNames.append(", ");
                        }
                    }
                    tagsText.setText(tagNames.toString());
                } else {
                    tagsText.setText("Aucun tag");
                }


                bookViewModel.loadAverage(book.getId());
                bookViewModel.loadCommentsForBook(book.getId());

                deleteButton.setOnClickListener(v -> {
                    bookViewModel.deleteBook(book);
                    Navigation.findNavController(view).popBackStack();
                });

                editButton.setOnClickListener(v -> {
                    Navigation.findNavController(view).navigate(R.id.action_bookDetailFragment_to_editBookFragment);
                });
            }
        });

        bookViewModel.getBookComments().observe(getViewLifecycleOwner(), comments -> {
            if (comments != null) {
                commentAdapter.setComments(comments);
            }
        });


    }
}