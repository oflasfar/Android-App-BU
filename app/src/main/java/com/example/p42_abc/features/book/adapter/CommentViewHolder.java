package com.example.p42_abc.features.book.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.R;
import com.example.p42_abc.features.book.model.Comment;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    private final TextView content;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        content = itemView.findViewById(R.id.text_comment_content);
    }

    // Petite méthode propre pour lier les données
    public void bind(Comment comment) {
        content.setText(comment.getContent());
    }
}
