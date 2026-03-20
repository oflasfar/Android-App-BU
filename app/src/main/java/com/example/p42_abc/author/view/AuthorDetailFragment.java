package com.example.p42_abc.author.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.adapters.BookAdapter;
import com.example.p42_abc.models.Book;
import com.example.p42_abc.R;
import com.example.p42_abc.author.model.Author;
import com.example.p42_abc.author.viewModel.AuthorSharedViewModel;
import com.example.p42_abc.viewModels.BookViewModel;

public class AuthorDetailFragment extends Fragment {

    //Adapter pour le recycler View
    private BookAdapter bookAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_author_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textName = view.findViewById(R.id.textViewDetailAuthorName);
        Button btnDelete = view.findViewById(R.id.buttonDeleteAuthor);

        // On utilise bien requireActivity() pour récupérer le meme ViewModel que la liste
        AuthorSharedViewModel model = new ViewModelProvider(requireActivity()).get(AuthorSharedViewModel.class);
        BookViewModel bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        model.getSelected().observe(getViewLifecycleOwner(), author -> {
            if (author != null) {
                // des que on trouve l'auteur, on met son nom dans le TextView
                textName.setText(author.getName());
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_author_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // On crée l'adapter en lui donnant l'action de clic
        bookAdapter = new BookAdapter(clickedBook -> {

            Book completeBook = clickedBook;

            // On cherche ce livre dans la liste complète
            if (bookViewModel.getBooks().getValue() != null) {
                for (Book bookInList : bookViewModel.getBooks().getValue()) {
                    if (bookInList.getId() == clickedBook.getId()) {
                        completeBook = bookInList;
                        break;
                    }
                }
            }

            Author currentAuthor = model.getSelected().getValue();
            if (currentAuthor != null && completeBook.getAuthor() == null) {
                completeBook.setAuthor(currentAuthor);
            }

            bookViewModel.selectBook(completeBook);
            Navigation.findNavController(view).navigate(R.id.bookDetailFragment);
        });
        recyclerView.setAdapter(bookAdapter);

        //On observe le livedata des livres de l auteur
        model.getAuthorBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                //On récupère l'auteur actuellement affiché sur l'écran
                Author currentAuthor = model.getSelected().getValue();

                //On fait une boucle pour distribuer cet auteur à chaque livre de la liste
                if (currentAuthor != null) {
                    for (Book book : books) {
                        if (book.getAuthor() == null) {
                            book.setAuthor(currentAuthor);
                        }
                    }
                }
                bookAdapter.setBooks(books);
            }
        });

        btnDelete.setOnClickListener(v -> {
            //On recupère l'auteur actuellement sélectionné dans le ViewModel
            Author currentAuthor = model.getSelected().getValue();
            if (currentAuthor != null) {

                model.deleteAuthor(currentAuthor.getId());
                bookViewModel.refreshBooks();
                Navigation.findNavController(view).popBackStack();

                Toast.makeText(getContext(), "Auteur supprimé", Toast.LENGTH_SHORT).show();
            }
        });
    }
}