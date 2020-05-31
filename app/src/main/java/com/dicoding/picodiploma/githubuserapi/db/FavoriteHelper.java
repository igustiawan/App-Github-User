package com.dicoding.picodiploma.githubuserapi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.dicoding.picodiploma.githubuserapi.model.UserModel;
import java.util.ArrayList;
import static android.provider.BaseColumns._ID;
import static com.dicoding.picodiploma.githubuserapi.db.DatabaseContract.FavoriteColumns.IMAGE;
import static com.dicoding.picodiploma.githubuserapi.db.DatabaseContract.FavoriteColumns.TABLE_NAME;
import static com.dicoding.picodiploma.githubuserapi.db.DatabaseContract.FavoriteColumns.TITLE;


public class FavoriteHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DatabaseHelper dataBaseHelper;
    private static FavoriteHelper INSTANCE;
    private static SQLiteDatabase database;

    public FavoriteHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void addFavorite(UserModel userModel){
        ContentValues values = new ContentValues();
        values.put(TITLE, userModel.getUsername());
        values.put(IMAGE, userModel.getPhoto());
        database.insert(DATABASE_TABLE, null, values);
    }

    public int deleteFavorite(String title){
        return database.delete(TABLE_NAME, TITLE + " = '" + title + "'", null);
    }

    public boolean checkForTableExists(String title){
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE login='"+title+"'";
        Cursor mCursor = database.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    public ArrayList<UserModel> getAllFavorite(){
        ArrayList<UserModel> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE,null,
                null,
                null,
                null,
                null,
                _ID+ " ASC",
                null);
        cursor.moveToFirst();
        UserModel userModel;
        if (cursor.getCount()>0){
            do{
                userModel = new UserModel();
                userModel.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                userModel.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(IMAGE)));
                arrayList.add(userModel);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        } cursor.close();
        return arrayList;
    }

    public Cursor queryAll() {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC");
    }
}
