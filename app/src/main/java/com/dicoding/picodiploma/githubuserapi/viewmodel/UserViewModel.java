package com.dicoding.picodiploma.githubuserapi.viewmodel;

import android.util.Log;
import com.dicoding.picodiploma.githubuserapi.BuildConfig;
import com.dicoding.picodiploma.githubuserapi.model.UserModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import cz.msebera.android.httpclient.Header;

public class UserViewModel extends ViewModel {
    private MutableLiveData<ArrayList<UserModel>> listUser = new MutableLiveData<>();
    private static final String API_KEY = BuildConfig.API_KEY;

    public void setListUser(final String username) {
        final ArrayList<UserModel> listItems = new ArrayList<>();

        AsyncHttpClient client = new AsyncHttpClient();
        String url = " https://api.github.com/search/users?q=" + username;

        client.addHeader("User-Agent", "request");
        client.addHeader("Authorization", "token " + API_KEY);

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{

                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("items");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject user = list.getJSONObject(i);
                        UserModel userModel = new UserModel();
                        userModel.setUsername(user.getString("login"));
                        userModel.setPhoto(user.getString("avatar_url"));
                        listItems.add(userModel);
                    }
                    listUser.postValue(listItems);

                }catch (Exception e){
                    Log.d("Exception: ", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
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
                Log.d("Error : ",errorMessage);
            }
        });
    }

    public LiveData<ArrayList<UserModel>> getListUser() {
        return listUser;
    }

}
