package com.kevin.newsdemo.user.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by kevin on 2019/07/18 10:28.
 */
class MySqLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, "my.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val sql = "CREATE TABLE user (\n" +
                "    id          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "    id_token TEXT    NOT NULL UNIQUE,\n" +
                "    access_token TEXT    NOT NULL,\n" +
                "    refresh_token  TEXT NOT NULL\n" +
                ");"

        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {

    }

}
