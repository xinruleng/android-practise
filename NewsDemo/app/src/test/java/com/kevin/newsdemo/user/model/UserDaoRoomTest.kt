package com.kevin.newsdemo.user.model

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kevin.newsdemo.base.AppDatabase
import com.kevin.newsdemo.data.Auth
import com.kevin.newsdemo.data.User
import org.junit.After
import org.junit.Assert
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kevin on 2019/07/21 12:13.
 */
@RunWith(AndroidJUnit4::class)
class UserDaoRoomTest {
    private var dao: UserDao? = null
    private var user: User? = null
    private var db: AppDatabase? = null

    @Before
    fun init() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        user = User(Auth(ID, TOKEN, REFRESH_TOKEN))

        db = Room.inMemoryDatabaseBuilder(appContext,
                AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        dao = db!!.userDao()
        Log.d(TAG, "init: user=" + user!!)
    }

    @After
    fun tearDown() {
        try {
            dao!!.delete(user!!)
        } catch (e: Exception) {
        }

        try {
            val u = dao!!.query(user!!.id)
            Log.d(TAG, "tearDown: user=" + u!!)
        } catch (e: Exception) {
            Log.d(TAG, "tearDown: query user error")
        }

        db!!.close()
    }

    @Test
    @Throws(Exception::class)
    fun should_delete_succeed() {
        val id = dao!!.insert(user!!)
        user!!.id = id.toInt()
        Log.d(TAG, "insert: user=" + user!!)
        dao!!.delete(user!!)
    }

    @Test
    @Throws(Exception::class)
    fun should_insert_succeed() {
        val id = dao!!.insert(user!!)
        user!!.id = id.toInt()

        try {
            dao!!.insert(user!!)
            fail()
        } catch (e: Exception) {
            Assert.assertTrue(e is RuntimeException)
        }

    }

    @Test
    @Throws(Exception::class)
    fun should_query_succeed() {
        val id = dao!!.insert(user!!)
        user!!.id = id.toInt()
        var u = dao!!.query(user!!.id)
        val auth = u!!.auth
        Assert.assertEquals(ID, auth.idToken)
        Assert.assertEquals(TOKEN, auth.token)
        Assert.assertEquals(REFRESH_TOKEN, auth.refreshToken)

        dao!!.delete(u)
        u = dao!!.query(u.id)
        Assert.assertNull(u)
    }

    @Test
    @Throws(Exception::class)
    fun should_update_succeed() {
        val id = dao!!.insert(user!!)
        user!!.id = id.toInt()

        var auth = Auth(ID, TOKEN + 1, REFRESH_TOKEN + 1)
        var u: User? = User(auth)
        u!!.id = user!!.id

        var update = dao!!.update(u)
        Assert.assertTrue(update > 0)

        u = dao!!.query(user!!.id)
        auth = u!!.auth
        Assert.assertEquals(ID, auth.idToken)
        Assert.assertEquals(TOKEN + 1, auth.token)
        Assert.assertEquals(REFRESH_TOKEN + 1, auth.refreshToken)

        dao!!.delete(u)

        update = dao!!.update(u)
        Assert.assertFalse(update > 0)

    }

    companion object {
        private val TAG = "UserDaoTest"
        val ID = "123456"
        val TOKEN = "98908989089"
        val REFRESH_TOKEN = "34545234234"
    }
}
