package com.kevin.newsdemo.user.model;

import androidx.room.*;
import com.kevin.newsdemo.data.User;

/**
 * Created by kevin on 2019/07/18 10:26.
 */
@Dao
public interface UserDao {
    @Insert
    long insert(User user) throws Exception;

    @Update
    int update(User user) throws Exception;

    @Query("SELECT * FROM user WHERE id = :id")
    User query(int id) throws Exception;

    @Delete
    int delete(User user) throws Exception;
}
