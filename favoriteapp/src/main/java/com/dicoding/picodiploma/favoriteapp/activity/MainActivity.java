package com.dicoding.picodiploma.favoriteapp.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import com.dicoding.picodiploma.favoriteapp.R;
import com.dicoding.picodiploma.favoriteapp.adapter.UserAdapter;
import static com.dicoding.picodiploma.favoriteapp.db.DatabaseContract.FavoriteColumns.CONTENT_URI;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvFavoriteUser;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Favorite User Github");
        }

        rvFavoriteUser = findViewById(R.id.rv_favorite_users);
        rvFavoriteUser.setHasFixedSize(true);

        new FavoriteGithubUser().execute();

    }

    private class FavoriteGithubUser extends AsyncTask<Void,Void, Cursor>{
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Cursor doInBackground(Void... voids) {
            return getApplicationContext().getContentResolver().query(CONTENT_URI,null,null,null,null);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            showRecyclerFavorite(cursor);
        }

    }

    private void showRecyclerFavorite(Cursor cursor){
        rvFavoriteUser.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(getApplicationContext());
        adapter.setCursor(cursor);
        adapter.notifyDataSetChanged();
        rvFavoriteUser.setAdapter(adapter);
    }
}
