package com.kevin.newsdemo.user.model;

import android.content.Context;
import android.util.Log;
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
    private static final String TAG = "UserDaoTest";
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
        try {
            User u = dao.query(user.getId());
            Log.d(TAG, "tearDown: user="+u);
        }catch (Exception e) {
            Log.d(TAG, "tearDown: query user error");
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
        User u = dao.query(user.getId());
        final Auth auth = u.getAuth();
        Assert.assertEquals(ID, auth.getId());
        Assert.assertEquals(TOKEN, auth.getToken());
        Assert.assertEquals(REFRESH_TOKEN, auth.getRefreshToken());

        dao.delete(u);
        try {
            dao.query(u.getId());
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
        User u = new User(auth);
        u.setId(user.getId());

        dao.update(u);

        u = dao.query(user.getId());
        auth = u.getAuth();
        Assert.assertEquals(ID, auth.getId());
        Assert.assertEquals(TOKEN + 1, auth.getToken());
        Assert.assertEquals(REFRESH_TOKEN + 1, auth.getRefreshToken());

        dao.delete(u);
        try {
            dao.update(u);
            fail();
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }


}
