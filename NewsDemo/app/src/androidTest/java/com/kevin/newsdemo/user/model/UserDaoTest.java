package com.kevin.newsdemo.user.model;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import com.kevin.newsdemo.data.Auth;
import com.kevin.newsdemo.data.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by kevin on 2019/07/18 10:39.
 */
@RunWith(AndroidJUnit4.class)  //include this line or not has no difference
public class UserDaoTest {
    public static final String ID = "123456";
    public static final String TOKEN = "98908989089";
    public static final String REFRESH_TOKEN = "34545234234";
    private UserDao dao;
    private User user;

    @Before
    public void init() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        dao = new UserDaoImpl(appContext);
        user = new User(new Auth(ID, TOKEN, REFRESH_TOKEN));
    }

    @Test
    public void should_all() {
        should_insert_succeed();
        should_query_succeed();
        should_update_succeed();
        should_delete_succeed();
    }

    public void should_delete_succeed() {
        boolean r = dao.delete(user);
        Assert.assertTrue(r);
    }

    public void should_insert_succeed() {
        boolean r = dao.insert(user);
        Assert.assertTrue(r);
    }

    public void should_query_succeed() {
        User user = dao.query();
        final Auth auth = user.getAuth();
        Assert.assertEquals(ID, auth.getId());
        Assert.assertEquals(TOKEN, auth.getToken());
        Assert.assertEquals(REFRESH_TOKEN, auth.getRefreshToken());
    }

    public void should_update_succeed() {
        Auth auth = new Auth(ID, TOKEN + 1, REFRESH_TOKEN + 1);
        User user = new User(auth);

        boolean r = dao.update(user);
        Assert.assertTrue(r);

        user = dao.query();
        auth = user.getAuth();
        Assert.assertEquals(ID, auth.getId());
        Assert.assertEquals(TOKEN + 1, auth.getToken());
        Assert.assertEquals(REFRESH_TOKEN + 1, auth.getRefreshToken());
    }
}