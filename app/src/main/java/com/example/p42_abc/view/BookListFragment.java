package com.example.p42_abc.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.R;
import com.example.p42_abc.adapters.BookAdapter;
import com.example.p42_abc.viewModels.BookViewModel;

public class BookListFragment extends Fragment {
    private BookViewModel bookViewModel;
    private BookAdapter bookAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // lier le fragment a fragment_book_list
        return inflater.inflate(R.layout.fragment_book_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        // on observe les nouveaus books
        bookViewModel.getBooks().observe(getViewLifecycleOwner(), books -> {
            // on envoie a l'adapter pour l'afficher ensuite dans la view
            if (books != null) {
                bookAdapter.setBooks(books);
            }
        });
        view.findViewById(R.id.fab_add_book).setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_bookListFragment_to_addBookFragment);
        });
    }
}
