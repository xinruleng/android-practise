package com.kevin.newsdemo.user.model;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import com.kevin.newsdemo.data.Auth;
import com.kevin.newsdemo.data.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;

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

    @After
    public void tearDown() {
        try {
            dao.delete(user);
        }
        catch (Exception e) {
        }
    }

    @Test
    public void should_delete_succeed() throws Exception {
        dao.insert(user);
        dao.delete(user);

        try {
            dao.delete(user);
            fail();
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    @Test
    public void should_insert_succeed() throws Exception {
        dao.insert(user);

        try {
            dao.insert(user);
            fail();
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }

    }

    @Test
    public void should_query_succeed() throws Exception {
        dao.insert(user);
        User user = dao.query();
        final Auth auth = user.getAuth();
        Assert.assertEquals(ID, auth.getId());
        Assert.assertEquals(TOKEN, auth.getToken());
        Assert.assertEquals(REFRESH_TOKEN, auth.getRefreshToken());

        dao.delete(user);
        try {
            user = dao.query();
            fail();
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    @Test
    public void should_update_succeed() throws Exception {
        dao.insert(user);

        Auth auth = new Auth(ID, TOKEN + 1, REFRESH_TOKEN + 1);
        User user = new User(auth);

        dao.update(user);

        user = dao.query();
        auth = user.getAuth();
        Assert.assertEquals(ID, auth.getId());
        Assert.assertEquals(TOKEN + 1, auth.getToken());
        Assert.assertEquals(REFRESH_TOKEN + 1, auth.getRefreshToken());

        dao.delete(user);
        try {
            dao.update(user);
            fail();
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }


}
