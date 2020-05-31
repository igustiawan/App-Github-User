package com.dicoding.picodiploma.favoriteapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.dicoding.picodiploma.favoriteapp.R;
import com.dicoding.picodiploma.favoriteapp.model.UserModel;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private Cursor cursor;

    public UserAdapter(Context context) {
        this.context = context;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    private UserModel getItemFavorite(int Position){
        if (!cursor.moveToPosition(Position)){
            throw new IllegalStateException("Invalid Data");
        }
        return new UserModel(cursor);
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_users,parent,false);
        return new UserViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.UserViewHolder holder, int position) {
        UserModel userModel = getItemFavorite(position);
        holder.bind(userModel);
    }

    @Override
    public int getItemCount() {
        if (cursor == null) return 0;
        return cursor.getCount();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textUsername;
        ImageView imgPhoto;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textUsername = itemView.findViewById(R.id.username);
            imgPhoto = itemView.findViewById(R.id.img_photo);
        }
        
        void bind(UserModel userModel) {
            Glide.with(itemView.getContext())
                    .load(userModel.getPhoto())
                    .into(imgPhoto);
            textUsername.setText(userModel.getUsername());
        }
    }

}
