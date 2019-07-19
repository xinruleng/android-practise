package com.kevin.newsdemo.user.model;

import com.kevin.newsdemo.data.User;

/**
 * Created by kevin on 2019/07/18 10:26.
 */
public interface UserDao {

    void insert(User user) throws Exception;

    void update(User user) throws Exception;

    User query(int id) throws Exception;

    void delete(User user) throws Exception;
}
