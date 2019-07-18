package com.kevin.newsdemo.user.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kevin on 2019/07/18 10:28.
 */
public class MySqLiteOpenHelper extends SQLiteOpenHelper {

    public MySqLiteOpenHelper(Context context) {
        super(context, "my.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE user (\n" +
                "    id          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "    access_token TEXT    NOT NULL,\n" +
                "    refresh_token  TEXT NOT NULL\n" +
                ");";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

}
