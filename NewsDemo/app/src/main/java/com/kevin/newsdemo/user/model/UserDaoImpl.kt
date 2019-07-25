package com.kevin.newsdemo.user.model

import android.content.ContentValues
import android.content.Context
import com.kevin.newsdemo.data.Auth
import com.kevin.newsdemo.data.User

/**
 * Created by kevin on 2019/07/18 10:25.
 */
class UserDaoImpl(private val mContext: Context) : UserDao {
    private val mySqLiteOpenHelper: MySqLiteOpenHelper

    init {
        mySqLiteOpenHelper = MySqLiteOpenHelper(mContext)
    }


    override fun insert(user: User): Long {
        val database = mySqLiteOpenHelper.writableDatabase
        val sql = "INSERT INTO $TABLE_NAME (id_token, access_token, refresh_token), values(?,?, ?)"
        val values = ContentValues()
        val auth = user.auth
        values.put("id_token", auth.idToken)
        values.put("access_token", auth.token)
        values.put("refresh_token", auth.refreshToken)
        val id = database.insert(TABLE_NAME, sql, values)
        database.close()
        if (id <= 0) {
            throw RuntimeException()
        } else {
            user.id = id.toInt()
            return id
        }
    }

    override fun update(user: User): Int {
        val database = mySqLiteOpenHelper.writableDatabase

        val auth = user.auth
        val values = ContentValues()
        values.put("id_token", auth.idToken)
        values.put("access_token", auth.token)
        values.put("refresh_token", auth.refreshToken)
        val whereClause = "id=?"

        val whereArgs = arrayOf(user.id.toString())
        val update = database.update(TABLE_NAME, values, whereClause, whereArgs)
        database.close()
        return update
    }

    override fun query(id: Int): User? {
        var id = id
        val sql = "SELECT * from $TABLE_NAME where id=?"
        val database = mySqLiteOpenHelper.readableDatabase
        val cursor = database.rawQuery(sql, arrayOf(id.toString()))
        var auth: Auth? = null
        if (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("id"))
            val idToken = cursor.getString(cursor.getColumnIndex("id_token"))
            val token = cursor.getString(cursor.getColumnIndex("access_token"))
            val refreshToken = cursor.getString(cursor.getColumnIndex("refresh_token"))

            auth = Auth(idToken, token, refreshToken)
            val user = User(id, auth)
            cursor.close()
            database.close()
            return user
        } else {
            cursor.close()
            database.close()
            return null
        }
    }

    override fun delete(user: User): Int {
        val database = mySqLiteOpenHelper.writableDatabase

        val where = "id=?"

        val whereArgs = arrayOf(user.id.toString())
        val delete = database.delete(TABLE_NAME, where, whereArgs)
        database.close()
        return delete
    }

    companion object {

        val TABLE_NAME = "user"
    }
}
