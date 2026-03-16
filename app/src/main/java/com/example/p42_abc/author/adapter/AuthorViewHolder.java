package com.example.p42_abc.author.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.R;

public class AuthorViewHolder extends RecyclerView.ViewHolder {

    // On la met en public pour que l'Adapter puisse y accéder facilement
    public TextView textViewName;

    public AuthorViewHolder(@NonNull View itemView) {
        super(itemView);
        // On fait le lien avec l'ID du fichier XML
        textViewName = itemView.findViewById(R.id.textViewAuthorName);
    }

    public TextView getTextViewName() {
        return textViewName;
    }
}
