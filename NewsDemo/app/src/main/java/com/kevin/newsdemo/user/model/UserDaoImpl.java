package com.kevin.newsdemo.user.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.kevin.newsdemo.data.Auth;
import com.kevin.newsdemo.data.User;

/**
 * Created by kevin on 2019/07/18 10:25.
 */
public class UserDaoImpl implements UserDao {
    private Context mContext;
    private MySqLiteOpenHelper mySqLiteOpenHelper;

    public static final String TABLE_NAME = "user";

    public UserDaoImpl(Context mContext) {
        this.mContext = mContext;
        mySqLiteOpenHelper = new MySqLiteOpenHelper(mContext);
    }


    @Override
    public boolean insert(User user) {
        final SQLiteDatabase database = mySqLiteOpenHelper.getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_NAME + " (id, access_token, refresh_token), values(?,?, ?)";
        ContentValues values = new ContentValues();
        final Auth auth = user.getAuth();
        values.put("id", auth.getId());
        values.put("access_token", auth.getToken());
        values.put("refresh_token", auth.getRefreshToken());
        long id = database.insert(TABLE_NAME, sql, values);

        database.close();
        return id > 0;
    }

    @Override
    public boolean update(User user) {
        final SQLiteDatabase database = mySqLiteOpenHelper.getWritableDatabase();

        final Auth auth = user.getAuth();
        ContentValues values = new ContentValues();
        values.put("access_token", auth.getToken());
        values.put("refresh_token", auth.getRefreshToken());
        String whereClause = "id=?";

        String[] whereArgs = new String[]{auth.getId()};
        final int update = database.update(TABLE_NAME, values, whereClause, whereArgs);
        database.close();
        return update > 0;
    }

    @Override
    public User query() {
        String sql = "SELECT * from " + TABLE_NAME + " ";
        final SQLiteDatabase database = mySqLiteOpenHelper.getReadableDatabase();
        final Cursor cursor = database.rawQuery(sql, null);
        Auth auth = null;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String token = cursor.getString(cursor.getColumnIndex("access_token"));
            String refreshToken = cursor.getString(cursor.getColumnIndex("refresh_token"));

            auth = new Auth(String.valueOf(id), token, refreshToken);
        }
        User user = new User(auth);
        cursor.close();
        database.close();
        return user;
    }

    @Override
    public boolean delete(User user) {
        final SQLiteDatabase database = mySqLiteOpenHelper.getWritableDatabase();

        String where = "id=?";

        String[] whereArgs = new String[]{user.getAuth().getId()};
        final int delete = database.delete(TABLE_NAME, where, whereArgs);
        database.close();
        return delete > 0;
    }
}
