package com.kevin.newsdemo.user.model;

import android.content.Context;
import android.util.Log;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import com.kevin.newsdemo.base.AppDatabase;
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
public class UserDaoRoom1Test {
    private static final String TAG = "UserDaoTest";
    public static final String ID = "123456";
    public static final String TOKEN = "98908989089";
    public static final String REFRESH_TOKEN = "34545234234";
    private UserDao dao;
    private User user;
    private AppDatabase db;

    @Before
    public void init() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        user = new User(new Auth(ID, TOKEN, REFRESH_TOKEN));

        db = Room.inMemoryDatabaseBuilder(appContext,
          AppDatabase.class).build();
        dao = db.userDao();
        Log.d(TAG, "init: user=" + user);
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
            Log.d(TAG, "tearDown: user=" + u);
        }
        catch (Exception e) {
            Log.d(TAG, "tearDown: query user error");
        }
        db.close();
    }

    @Test
    public void should_delete_succeed() throws Exception {
        long id = dao.insert(user);
        user.setId((int) id);
        Log.d(TAG, "insert: user=" + user);
        dao.delete(user);
    }

    @Test
    public void should_insert_succeed() throws Exception {
        long id = dao.insert(user);
        user.setId((int) id);

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
        long id = dao.insert(user);
        user.setId((int) id);
        User u = dao.query(user.getId());
        final Auth auth = u.getAuth();
        Assert.assertEquals(ID, auth.getIdToken());
        Assert.assertEquals(TOKEN, auth.getToken());
        Assert.assertEquals(REFRESH_TOKEN, auth.getRefreshToken());

        dao.delete(u);
        u = dao.query(u.getId());
        Assert.assertNull(u);
    }

    @Test
    public void should_update_succeed() throws Exception {
        long id = dao.insert(user);
        user.setId((int) id);

        Auth auth = new Auth(ID, TOKEN + 1, REFRESH_TOKEN + 1);
        User u = new User(auth);
        u.setId(user.getId());

        int update = dao.update(u);
        Assert.assertTrue(update > 0);

        u = dao.query(user.getId());
        auth = u.getAuth();
        Assert.assertEquals(ID, auth.getIdToken());
        Assert.assertEquals(TOKEN + 1, auth.getToken());
        Assert.assertEquals(REFRESH_TOKEN + 1, auth.getRefreshToken());

        dao.delete(u);

        update = dao.update(u);
        Assert.assertFalse(update > 0);

    }


}
