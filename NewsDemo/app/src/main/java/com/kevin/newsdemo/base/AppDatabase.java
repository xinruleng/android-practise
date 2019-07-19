package com.kevin.newsdemo.base;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.user.model.UserDao;

/**
 * Created by kevin on 2019/07/18 21:35.
 */
@Database(entities = {User.class}, version = 1, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
