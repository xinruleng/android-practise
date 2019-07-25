package com.kevin.newsdemo.user.model

import androidx.room.*
import com.kevin.newsdemo.data.User

/**
 * Created by kevin on 2019/07/18 10:26.
 */
@Dao
interface UserDao {
    @Insert
    @Throws(Exception::class)
    fun insert(user: User): Long

    @Update
    @Throws(Exception::class)
    fun update(user: User): Int

    @Query("SELECT * FROM user WHERE id = :id")
    @Throws(Exception::class)
    fun query(id: Int): User?

    @Delete
    @Throws(Exception::class)
    fun delete(user: User): Int
}
