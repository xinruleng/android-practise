package com.kevin.newsdemo.user.model;

import com.kevin.newsdemo.data.User;

/**
 * Created by kevin on 2019/07/18 10:26.
 */
public interface UserDao {

    boolean insert(User user);

    boolean update(User user);

    User query();

    boolean delete(User user);
}
