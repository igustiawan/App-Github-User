package com.dicoding.picodiploma.favoriteapp.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import com.dicoding.picodiploma.favoriteapp.db.DatabaseContract;

import static com.dicoding.picodiploma.favoriteapp.db.DatabaseContract.getItemFavorite;

public class UserModel implements Parcelable{
    private String username;
    private String photo;

    public String getUsername() {
        return username;
    }

    public String getPhoto() {
        return photo;
    }

    public UserModel(Parcel in) {
        username = in.readString();
        photo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(photo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public UserModel(Cursor cursor) {
        this.username = getItemFavorite(cursor, DatabaseContract.FavoriteColumns.TITLE);
        this.photo = getItemFavorite(cursor,DatabaseContract.FavoriteColumns.IMAGE);
    }

}
