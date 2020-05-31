package com.dicoding.picodiploma.githubuserapi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.dicoding.picodiploma.githubuserapi.model.UserModel;
import com.dicoding.picodiploma.githubuserapi.R;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private ArrayList<UserModel> mUser = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public void setData(ArrayList<UserModel> items){
        mUser.clear();
        mUser.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_users,parent,false);
        return new UserViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.UserViewHolder holder, int position) {
        holder.bind(mUser.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(mUser.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
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

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback{
        void onItemClicked(UserModel userModel);
    }
}
