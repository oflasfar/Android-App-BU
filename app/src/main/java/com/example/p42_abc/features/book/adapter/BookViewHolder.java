package com.example.p42_abc.features.book.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.R;
import com.example.p42_abc.features.book.model.Book;

public class BookViewHolder extends RecyclerView.ViewHolder {

    private final TextView titleTextView;
    private TextView authorTextView;


    public BookViewHolder(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.text_view_book_title);
        authorTextView = itemView.findViewById(R.id.text_view_book_author);
    }

    public void bind(Book book){
        if(book != null) {
            titleTextView.setText(book.getTitle());
            String sub = "";
            //On ajoute l'auteur au texte
            if (book.getAuthor() != null) {
                sub += book.getAuthor().getName();
            } else {
                sub += "Auteur inconnu";
            }
            if (book.getPublicationYear() != null) {
                sub += " • " + book.getPublicationYear();
            }
            authorTextView.setText(sub);
        }
    }
}
