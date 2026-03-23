package com.example.p42_abc.features.author.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.p42_abc.R;
import com.example.p42_abc.features.author.model.Author;

import java.util.ArrayList;
import java.util.List;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorViewHolder> {

    private List<Author> authorsList = new ArrayList<>();

    public void setAuthors(List<Author> authors) {
        this.authorsList = authors;
        notifyDataSetChanged();
    }

    private OnAuthorClickListener listener;

    public void setOnAuthorClickListener(OnAuthorClickListener listener) {
        this.listener = listener;
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

        holder.textViewName.setText(currentAuthor.getName());

        ImageView imageAvatar = holder.itemView.findViewById(R.id.image_author_avatar);

        String nomFormate = currentAuthor.getName().replace(" ", "+");

        String urlAvatar = "https://ui-avatars.com/api/?name=" + nomFormate + "&background=random&color=fff&rounded=true";

        if (imageAvatar != null) {
            Glide.with(holder.itemView.getContext())
                    .load(urlAvatar)
                    .into(imageAvatar);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAuthorClick(currentAuthor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return authorsList.size();
    }
}