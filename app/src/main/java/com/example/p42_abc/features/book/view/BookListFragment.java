package com.example.p42_abc.features.book.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.R;
import com.example.p42_abc.features.book.adapter.BookAdapter;
import com.example.p42_abc.features.book.model.Book;
import com.example.p42_abc.features.book.viewmodel.BookViewModel;

import java.util.ArrayList;
import java.util.List;

public class BookListFragment extends Fragment {
    private BookViewModel bookViewModel;
    private BookAdapter bookAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // lier le fragment a fragment_book_list
        return inflater.inflate(R.layout.fragment_book_list, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText searchBar = view.findViewById(R.id.edit_text_search);

        // init RecyclerView et Adapter
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bookAdapter = new BookAdapter(clickedBook ->{
            bookViewModel.selectBook(clickedBook);
            Navigation.findNavController(view).navigate(R.id.action_bookListFragment_to_bookDetailFragment);
        });
        recyclerView.setAdapter(bookAdapter);

        // init le ViewModel
        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
        
        // On force le rafraîchissement pour voir les changements
        bookViewModel.refreshBooks();

        // on observe les nouveaus books
        bookViewModel.getBooks().observe(getViewLifecycleOwner(), books -> {
            // on envoie a l'adapter pour l'afficher ensuite dans la view
            if (books != null) {
                bookAdapter.setBooks(books);
                bookAdapter.notifyDataSetChanged(); // Rafraîchissement manuel
            }
        });
        // On écoute le bouton pour ajouter un livre
        view.findViewById(R.id.fab_add_book).setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_bookListFragment_to_addBookFragment);
        });



        // On écoute chaque lettre tapée au clavier
        searchBar.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Le texte que l'utilisateur est en train de taper
                String query = s.toString().toLowerCase();

                // On récupère la liste complète des livres depuis le ViewModel
                List<Book> allBooks = bookViewModel.getBooks().getValue();

                if (allBooks != null) {
                    List<Book> filteredList = new ArrayList<>();

                    // On fait le tri
                    for (Book book : allBooks) {
                        // Si le titre contient le texte tapé on le garde
                        if (book.getTitle().toLowerCase().contains(query)) {
                            filteredList.add(book);
                        }
                    }

                    // On envoie la liste triée à l'adaptateur
                    bookAdapter.setBooks(filteredList);
                    bookAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

}
