package com.dicoding.picodiploma.githubuserapi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dicoding.picodiploma.githubuserapi.R;
import com.dicoding.picodiploma.githubuserapi.adapter.UserAdapter;
import com.dicoding.picodiploma.githubuserapi.db.FavoriteHelper;
import com.dicoding.picodiploma.githubuserapi.model.UserModel;
import com.dicoding.picodiploma.githubuserapi.viewmodel.UserViewModel;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    private UserAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView rvFavorite;
    private UserViewModel userViewModel;
    private FavoriteHelper favoriteHelper;
    ArrayList<UserModel> listItem = new ArrayList<>();
    private TextView dataNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Favorite User");
        }

        favoriteHelper = FavoriteHelper.getInstance(FavoriteActivity.this);
        favoriteHelper.open();

        progressBar = findViewById(R.id.progressBar);
        rvFavorite = findViewById(R.id.rv_favorite);
        rvFavorite.setHasFixedSize(true);

        dataNotFound = findViewById(R.id.data_not_found);

        showRecyclerUser();
        moveIntent();
    }

    private void moveIntent(){
        adapter.setOnItemClickCallback(new UserAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(UserModel userModel) {
                Intent intent = new Intent(FavoriteActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_USER, userModel);
                startActivity(intent);
            }
        });
    }

    private void showRecyclerUser(){
        rvFavorite.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter();
        rvFavorite.setAdapter(adapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        listItem = favoriteHelper.getAllFavorite();
        adapter.setData(listItem);

        if(listItem.size() != 0){
            dataNotFound.setVisibility(View.GONE);
        }else{
            dataNotFound.setVisibility(View.VISIBLE);
        }
    }

}
