package com.example.p42_abc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.R;
import com.example.p42_abc.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookViewHolder>{

    private List<Book> _bookList = new ArrayList<>();

    private OnBookClickListener _listener;

    public BookAdapter(OnBookClickListener listener){
        this._listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = _bookList.get(position);
        holder.bind(book);

        // au click sur l'itemView
        holder.itemView.setOnClickListener(v -> {
            if(this._listener != null){
                _listener.onBookClick(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return _bookList != null ? _bookList.size() : 0;
    }

    public void setBooks(List<Book> books){
        this._bookList = books;
        notifyDataSetChanged();
    }
}
