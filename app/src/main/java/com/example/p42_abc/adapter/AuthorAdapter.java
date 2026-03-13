package com.example.p42_abc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.R;
import com.example.p42_abc.model.Author;

import java.util.ArrayList;
import java.util.List;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorViewHolder> {

    private List<Author> authorsList = new ArrayList<>();

    public void setAuthors(List<Author> authors) {
        this.authorsList = authors;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_author, parent, false);
        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        Author currentAuthor = authorsList.get(position);

        // On remplit le TextView qui se trouve dans l'autre fichier
        holder.textViewName.setText(currentAuthor.getName());
    }

    @Override
    public int getItemCount() {
        return authorsList.size();
    }
}