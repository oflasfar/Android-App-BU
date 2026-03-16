package com.example.p42_abc.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.R;
import com.example.p42_abc.models.Book;

public class BookViewHolder extends RecyclerView.ViewHolder {

    private final TextView titleTextView;


    public BookViewHolder(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.text_view_book_title);
    }

    public void bind(Book book){
        if(book != null) {
            titleTextView.setText(book.getTitle());
        }
    }
}
