package com.dicoding.picodiploma.favoriteapp.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.dicoding.picodiploma.githubuserapi";
    private static final String SCHEME = "content";

    public static final class FavoriteColumns implements BaseColumns {
        public static final String TABLE_NAME = "favorite";
        public static final String TITLE = "login";
        public static final String IMAGE = "avatar_url";
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static String getItemFavorite(Cursor cursor, String column){
        return cursor.getString(cursor.getColumnIndex(column));
    }
}
