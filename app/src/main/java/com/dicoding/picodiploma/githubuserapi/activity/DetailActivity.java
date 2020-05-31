package com.dicoding.picodiploma.githubuserapi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import cz.msebera.android.httpclient.Header;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.dicoding.picodiploma.githubuserapi.BuildConfig;
import com.dicoding.picodiploma.githubuserapi.R;
import com.dicoding.picodiploma.githubuserapi.adapter.SectionsPagerAdapter;
import com.dicoding.picodiploma.githubuserapi.db.FavoriteHelper;
import com.dicoding.picodiploma.githubuserapi.model.UserModel;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONObject;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_USER = "extra_user";
    private ProgressBar progressBar;
    private TextView textUsername,textName,textFollower,textFollowing,textRepository;
    private ImageView imgPhoto;
    private FavoriteHelper favoriteHelper;
    private MaterialFavoriteButton materialFavoriteButtonNice;
    private UserModel favorite;
    String username, avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final UserModel userModelIntent = getIntent().getParcelableExtra(EXTRA_USER);

        imgPhoto = findViewById(R.id.photo_detail);
        progressBar = findViewById(R.id.progressbar_detail);
        textUsername = findViewById(R.id.username_detail);
        textName = findViewById(R.id.name_detail);
        textFollower = findViewById(R.id.follower_detail);
        textRepository = findViewById(R.id.repo_detail);
        textFollowing = findViewById(R.id.following_detail);

        materialFavoriteButtonNice =findViewById(R.id.favorite_button);
        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();

        showLoading(true);
        if(userModelIntent!=null) {
            username = userModelIntent.getUsername();
            avatar = userModelIntent.getPhoto();
            setUserDetail(username);
            listenerFavorite();
            setPager(userModelIntent);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Detail User");
        }

    }

    private void listenerFavorite(){

        boolean existFavorite = favoriteHelper.checkForTableExists(username);
        if(existFavorite){
            materialFavoriteButtonNice.setFavorite(true);
        }

        materialFavoriteButtonNice.setOnFavoriteChangeListener(
            new MaterialFavoriteButton.OnFavoriteChangeListener(){
                @Override
                public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite){
                    if (favorite){
                        saveFavorite();
                        Toast.makeText(DetailActivity.this, "Added to Favorite", Toast.LENGTH_SHORT).show();
                    }else{
                        deleteFavorite(username);
                        Toast.makeText(DetailActivity.this, "Removed from Favorite", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );
    }

    private void saveFavorite(){
        favorite = new UserModel();
        favorite.setUsername(username);
        favorite.setPhoto(avatar);
        favoriteHelper.addFavorite(favorite);
    }

    private void deleteFavorite(String title){
        favoriteHelper.deleteFavorite(title);
    }

    private void showLoading(boolean state){
        if(state){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }

    final void setUserDetail(final String username){
        String API_KEY = BuildConfig.API_KEY;

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.github.com/users/" + username;
        Log.d("setUserDetail: ",url);
        client.addHeader("User-Agent", "request");
        client.addHeader("Authorization", "token " + API_KEY);

        client.get(url, new AsyncHttpResponseHandler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{
                    showLoading(false);
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);

                    UserModel userModel = new UserModel();
                    userModel.setName(responseObject.getString("name"));
                    userModel.setRepository(responseObject.getString("public_repos"));
                    userModel.setFollowers(responseObject.getString("followers"));
                    userModel.setFollowing(responseObject.getString("following"));

                    textName.setText(userModel.getName());
                    textUsername.setText(username);
                    textRepository.setText(userModel.getRepository());
                    textFollower.setText(userModel.getFollowers());
                    textFollowing.setText(userModel.getFollowing());

                    Glide.with(getApplicationContext())
                            .load(responseObject.getString("avatar_url"))
                            .into(imgPhoto);

                }catch (Exception e){
                    Log.d("Exception: ",e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showLoading(false);
                String errorMessage;
                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbidden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage =  statusCode + " : " + error.getMessage();
                        break;
                }
                Toast.makeText(DetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    private void setPager(UserModel userModel){
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(),userModel);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
