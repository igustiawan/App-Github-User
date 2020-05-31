package com.dicoding.picodiploma.githubuserapi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.dicoding.picodiploma.githubuserapi.adapter.UserAdapter;
import com.dicoding.picodiploma.githubuserapi.model.UserModel;
import com.dicoding.picodiploma.githubuserapi.R;
import com.dicoding.picodiploma.githubuserapi.viewmodel.UserViewModel;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private UserAdapter adapter;
    private ProgressBar progressBar;
    private SearchView searchView;
    private RecyclerView rvUser;
    private UserViewModel userViewModel;
    private TextView dataNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("User Github Search");
        }

        userViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(UserViewModel.class);

        searchView = findViewById(R.id.search_users);
        searchView.setQueryHint("Search Github Users");

        progressBar = findViewById(R.id.progressBar);
        rvUser = findViewById(R.id.rv_users);
        rvUser.setHasFixedSize(true);

        dataNotFound = findViewById(R.id.data_not_found);

        searchViewListener();
        showRecyclerUser();
        moveIntent();

    }

    private void showRecyclerUser(){
        rvUser.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter();
        adapter.notifyDataSetChanged();
        rvUser.setAdapter(adapter);
    }

    private void moveIntent(){
        adapter.setOnItemClickCallback(new UserAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(UserModel userModel) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_USER, userModel);
                startActivity(intent);
            }
        });
    }

    private void searchViewListener(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                userViewModel.setListUser(query);
                dataNotFound.setVisibility(View.GONE);
                showLoading(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        userViewModel.getListUser().observe(this, new Observer<ArrayList<UserModel>>() {
            @Override
            public void onChanged(ArrayList<UserModel> userModels) {
                if (userModels.size() != 0) {
                    adapter.setData(userModels);
                }else{
                    Toast.makeText(MainActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                }
                showLoading(false);
            }
        });
    }

    private void showLoading(boolean state){
        if(state){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.open_setting:
                Intent j = new Intent(this, SettingActivity.class);
                startActivity(j);
                return true;
            case R.id.open_favorite:
                Intent i = new Intent(this, FavoriteActivity.class);
                startActivity(i);
                return true;
        }
        return false;
    }
}
